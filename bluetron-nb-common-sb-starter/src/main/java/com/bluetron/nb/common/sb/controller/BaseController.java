package com.bluetron.nb.common.sb.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bluetron.nb.common.base.constant.AggregationKind;
import com.bluetron.nb.common.base.context.DataFilterThreadLocal;
import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.base.constant.AggregationType;
import com.bluetron.nb.common.db.mapper.BaseModelMapper;
import com.bluetron.nb.common.db.object.*;
import com.bluetron.nb.common.db.service.IBaseService;
import com.bluetron.nb.common.db.util.MyModelUtil;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.util.lang.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 控制器Controller的基类。
 *
 * @param <M> 主Model实体对象类型。
 * @param <V> 主Model的DomainVO域对象类型。
 * @param <K> 主键类型。
 * @author cqf
 * @date 2020-08-08
 */
@Slf4j
public abstract class BaseController<M, V, K extends Serializable> {

    /**
     * 当前Service关联的主Model实体对象的Class。
     */
    protected final Class<M> modelClass;
    /**
     * 当前Service关联的主model的VO对象的Class。
     */
    protected final Class<V> domainVoClass;
    /**
     * 当前Service关联的主Model对象主键字段名称。
     */
    protected String idFieldName;

    /**
     * 获取子类中注入的IBaseService接口。
     *
     * @return 子类中注入的BaseService类。
     */
    protected abstract IBaseService<M, K> service();

    /**
     * 构造函数。
     */
    @SuppressWarnings("unchecked")
    public BaseController() {
        modelClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        domainVoClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Field[] fields = ReflectUtil.getFields(modelClass);
        for (Field field : fields) {
            if (null != field.getAnnotation(TableId.class)) {
                idFieldName = field.getName();
                break;
            }
        }
    }

    /**
     * 根据主键Id集合，获取数据对象集合。仅限于微服务间远程接口调用。
     *
     * @param filterIds   主键Id集合。
     * @param withDict    是否包含字典关联。
     * @param modelMapper 对象映射函数对象。如果为空，则使用MyModelUtil中的缺省转换函数。
     * @return 应答结果对象，包含主对象集合。
     */
    public Result<List<V>> baseListByIds(
            Set<K> filterIds, Boolean withDict, BaseModelMapper<V, M> modelMapper) {
        if (ObjectUtil.isAnyBlankOrNull(filterIds, withDict)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        List<M> resultList = service().getInList(idFieldName, filterIds);
        List<V> resultVoList = null;
        if (CollectionUtils.isEmpty(resultList)) {
            return Result.succeed(resultVoList);
        }
        if (Boolean.TRUE.equals(withDict)) {
            service().buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        }
        resultVoList = convertToVoList(resultList, modelMapper);
        return Result.succeed(resultVoList);
    }

    /**
     * 根据主键Id，获取数据对象。仅限于微服务间远程接口调用。
     *
     * @param id          主键Id。
     * @param withDict    是否包含字典关联。
     * @param modelMapper 对象映射函数对象。如果为空，则使用MyModelUtil中的缺省转换函数。
     * @return 应答结果对象，包含主对象数据。
     */
    public Result<V> baseGetById(K id, Boolean withDict, BaseModelMapper<V, M> modelMapper) {
        if (ObjectUtil.isAnyBlankOrNull(id, withDict)) {
            return Result.failed(ResultCode.PARAM_IS_BLANK);
        }
        M resultObject = service().getById(id);
        V resultVoObject = null;
        if (resultObject == null) {
            return Result.succeed(resultVoObject);
        }
        if (Boolean.TRUE.equals(withDict)) {
            service().buildRelationForData(resultObject, MyRelationParam.dictOnly());
        }
        resultVoObject = this.convertToVo(resultObject, modelMapper);
        return Result.succeed(resultVoObject);
    }

    /**
     * 判断参数列表中指定的主键Id集合，是否全部存在。仅限于微服务间远程接口调用。
     *
     * @param filterIds 主键Id集合。
     * @return 应答结果对象，包含true全部存在，否则false。
     */
    public Result<Boolean> baseExistIds(Set<K> filterIds) {
        return Result.succeed(CollectionUtils.isNotEmpty(filterIds)
                && service().existUniqueKeyList(idFieldName, filterIds));
    }

    /**
     * 判断参数列表中指定的主键Id集合，是否全部存在。仅限于微服务间远程接口调用。
     *
     * @param id 主键Id。
     * @return 应答结果对象，包含true全部存在，否则false。
     */
    public Result<Boolean> baseExistId(K id) {
        return Result.succeed(
                !ObjectUtil.isAnyBlankOrNull(id) && service().getById(id) != null);
    }

    /**
     * 删除符合过滤条件的数据。
     *
     * @param filter 过滤对象。
     * @return 删除数量。
     */
    public Result<Integer> baseDeleteBy(M filter) throws Exception {
        return Result.succeed(service().removeBy(filter));
    }

    /**
     * 自定义过滤条件、显示字段和排序字段的单表查询。主要用于微服务间远程过程调用。
     * NOTE: 和baseListMapBy方法的差别只是返回的数据形式不同，该接口以对象列表的形式返回数据。
     *
     * @param queryParam  查询参数。
     * @param modelMapper 对象映射函数对象。如果为空，则使用MyModelUtil中的缺省转换函数。
     * @return 分页数据集合对象。如MyQueryParam参数的分页属性为空，则不会执行分页操作，只是基于Pagination对象返回数据结果。
     * @throw  buildRelationForDataList会抛出此异常。
     */
    public Result<Pagination<V>> baseListBy(MyQueryParam queryParam, BaseModelMapper<V, M> modelMapper) {
        boolean dataFilterEnabled = DataFilterThreadLocal.setDataFilter(queryParam.getUseDataFilter());
        if (CollectionUtils.isNotEmpty(queryParam.getSelectFieldList())) {
            for (String fieldName : queryParam.getSelectFieldList()) {
                String columnName = MyModelUtil.mapToColumnName(fieldName, modelClass);
                if (columnName == null) {
                    String errorMessage = "数据验证失败，实体对象 ["
                            + modelClass.getSimpleName() + "] 中不存在字段 [" + fieldName + "]!";
                    return Result.failed(ResultCode.DATA_VALIDATED_FAILED, errorMessage);
                }
            }
        }
        M filter = queryParam.getFilterDto(modelClass);
        String whereClause = MyWhereCriteria.makeCriteriaString(queryParam.getCriteriaList(), modelClass);
        String orderBy = MyOrderParam.buildOrderBy(queryParam.getOrderParam(), modelClass);
        MyPageParam pageParam = queryParam.getPageParam();
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        List<M> resultList = service().getListByCondition(
                queryParam.getSelectFieldList(), filter, whereClause, orderBy);
        if (CollectionUtils.isEmpty(resultList)) {
            return Result.succeed(Pagination.emptyPageData());
        }
        long totalCount;
        if (resultList instanceof Page) {
            totalCount = ((Page<M>) resultList).getTotal();
        } else {
            totalCount = resultList.size();
        }
        if (queryParam.getWithDict()) {
            service().buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        }
        List<V> resultVoList = convertToVoList(resultList, modelMapper);
        DataFilterThreadLocal.setDataFilter(dataFilterEnabled);
        return Result.succeed(new Pagination<>(resultVoList, totalCount ));
    }

    /**
     * 自定义过滤条件、显示字段和排序字段的单表查询。主要用于微服务间远程过程调用。
     * NOTE: 和baseListBy方法的差别只是返回的数据形式不同，该接口以Map列表的形式返回数据。
     *
     * @param queryParam     查询参数。
     * @param modelMapper    对象映射函数对象。如果为空，则使用MyModelUtil中的缺省转换函数。
     * @return 分页数据集合对象。如MyQueryParam参数的分页属性为空，则不会执行分页操作，只是基于Pagination对象返回数据结果。
     */
    public Result<Pagination<Map<String, Object>>> baseListMapBy(
            MyQueryParam queryParam, BaseModelMapper<V, M> modelMapper) {
        Result<Pagination<V>> result = this.baseListBy(queryParam, modelMapper);
        if (!result.isSuccess()) {
            return Result.failed(result);
        }
        List<Map<String, Object>> resultMapList =
                result.getData().getDataList().stream().map(BeanUtil::beanToMap).collect(Collectors.toList());
        return Result.succeed(new Pagination<>(resultMapList, result.getData().getTotalCount()));
    }

    /**
     * 自定义过滤条件、显示字段，并返回单条记录。主要用于微服务间远程过程调用。
     *
     * @param queryParam  查询参数。
     * @param modelMapper 对象映射函数对象。如果为空，则使用MyModelUtil中的缺省转换函数。
     * @return 应答结果对象，包含符合查询过滤条件的单条实体对象。
     */
    public Result<V> baseGetBy(MyQueryParam queryParam, BaseModelMapper<V, M> modelMapper) {
        Result<Pagination<V>> result = baseListBy(queryParam, modelMapper);
        if (!result.isSuccess()) {
            return Result.failed(result);
        }
        List<V> dataList = result.getData().getDataList();
        V data = null;
        if (CollectionUtils.isNotEmpty(dataList)) {
            data = dataList.get(0);
        }
        return Result.succeed(data);
    }

    /**
     * 自定义过滤条件的记录数量统计。主要用于微服务间远程过程调用。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含符合查询过滤条件的记录数量。
     */
    public Result<Integer> baseCountBy(MyQueryParam queryParam) {
        boolean dataFilterEnabled = DataFilterThreadLocal.setDataFilter(queryParam.getUseDataFilter());
        String whereClause = MyWhereCriteria.makeCriteriaString(queryParam.getCriteriaList(), modelClass);
        Integer count = service().getCountByCondition(whereClause);
        DataFilterThreadLocal.setDataFilter(dataFilterEnabled);
        return Result.succeed(count);
    }

    /**
     * 获取远程对象中符合查询条件的分组聚合计算Map列表。
     *
     * @param param 聚合参数。
     * @return 应该结果对象，包含聚合计算后的分组Map列表。
     */
    public Result<List<Map<String, Object>>> baseAggregateBy(MyAggregationParam param) {
        boolean dataFilterEnabled = DataFilterThreadLocal.setDataFilter(param.getUseDataFilter());
        // 完成一些共同性规则的验证。
        VerifyAggregationInfo verifyInfo = this.verifyAndParseAggregationParam(param);
        if (!verifyInfo.isSuccess) {
            return Result.failed(ResultCode.DATA_VALIDATED_FAILED, verifyInfo.errorMsg);
        }
        // 构建SelectList
        StringBuilder selectList = new StringBuilder(64);
        // 一对多场景相对比较简单，直接基于从表的关联键进行group，同时对聚合字段进行聚合计算即可。
        // SQL返回的数据，可直接返回给调用端服务。
        if (param.getAggregationKind() == AggregationKind.ONE_TO_MANY) {
            selectList.append(verifyInfo.groupColumn).append(" ").append(MyAggregationParam.KEY_NAME).append(", ");
        }
        selectList.append(AggregationType.getAggregationFunction(verifyInfo.aggregationType))
                .append("(")
                .append(verifyInfo.aggregationColumn)
                .append(") ")
                .append(MyAggregationParam.VALUE_NAME);
        String whereClause = MyWhereCriteria.makeCriteriaString(param.getWhereCriteriaList(), modelClass);
        List<Map<String, Object>> resultMapList = null;
        // 一对多场景直接返回分组查询计算结果即可。
        if (param.getAggregationKind() == AggregationKind.ONE_TO_MANY) {
            resultMapList = service().getGroupedListByCondition(
                    selectList.toString(), whereClause, verifyInfo.groupColumn);
        } else if (param.getAggregationKind() == AggregationKind.MANY_TO_MANY) {
            boolean stringKey = true;
            if (param.getGroupedInFilterValues().entrySet().iterator().next().getKey() instanceof Number) {
                stringKey = false;
            }
            resultMapList = new LinkedList<>();
            // 迭代分组map，通过多次查询的方式进行数据分组。通过该方式可以避免多次rpc调用，以提升性能。
            for (Map.Entry<Object, Set<Object>> entry : param.getGroupedInFilterValues().entrySet()) {
                StringBuilder groupedSelectList = new StringBuilder(64);
                if (stringKey) {
                    groupedSelectList.append("'").append(entry.getKey()).append("' ");
                } else {
                    groupedSelectList.append(entry.getKey()).append(" ");
                }
                groupedSelectList.append(MyAggregationParam.KEY_NAME).append(", ").append(selectList);
                MyWhereCriteria criteria = new MyWhereCriteria();
                criteria.setModelClazz(modelClass);
                criteria.setCriteria(param.getInFilterField(), MyWhereCriteria.OPERATOR_IN, entry.getValue());
                StringBuilder groupedClause = new StringBuilder(128);
                groupedClause.append(criteria.makeCriteriaString());
                if (StringUtils.isNotBlank(whereClause)) {
                    groupedClause.append(" AND ").append(whereClause);
                }
                List<Map<String, Object>> subResultMapList = service().getGroupedListByCondition(
                        groupedSelectList.toString(), groupedClause.toString(), null);
                resultMapList.addAll(subResultMapList);
            }
        }
        DataFilterThreadLocal.setDataFilter(dataFilterEnabled);
        return Result.succeed(resultMapList);
    }

    /**
     * 将Model实体对象的集合转换为DomainVO域对象的集合。
     * 如果Model存在该实体的ModelMapper，就用该ModelMapper转换，否则使用缺省的基于字段反射的copy。
     *
     * @param modelList   实体对象列表。
     * @param modelMapper 从实体对象到VO对象的映射对象。
     * @return 转换后的VO域对象列表。
     */
    protected List<V> convertToVoList(List<M> modelList, BaseModelMapper<V, M> modelMapper) {
        List<V> resultVoList;
        if (modelMapper != null) {
            resultVoList = modelMapper.fromModelList(modelList);
        } else {
            resultVoList = MyModelUtil.copyCollectionTo(modelList, domainVoClass);
        }
        return resultVoList;
    }

    /**
     * 将Model实体对象转换为DomainVO域对象。
     * 如果Model存在该实体的ModelMapper，就用该ModelMapper转换，否则使用缺省的基于字段反射的copy。
     *
     * @param model       实体对象。
     * @param modelMapper 从实体对象到VO对象的映射对象。
     * @return 转换后的VO域对象。
     */
    protected V convertToVo(M model, BaseModelMapper<V, M> modelMapper) {
        V resultVo;
        if (modelMapper != null) {
            resultVo = modelMapper.fromModel(model);
        } else {
            resultVo = MyModelUtil.copyTo(model, domainVoClass);
        }
        return resultVo;
    }

    private VerifyAggregationInfo verifyAndParseAggregationParam(MyAggregationParam param) {
        VerifyAggregationInfo verifyInfo = new VerifyAggregationInfo();
        if (!AggregationKind.isValid(param.getAggregationKind())) {
            verifyInfo.errorMsg = "参数验证失败，聚合类别 [MyAggregationParam.AggregationKind] 数值无效！";
            return verifyInfo;
        }
        Integer aggregationType = param.getAggregationType();
        if (!AggregationType.isValid(aggregationType)) {
            verifyInfo.errorMsg = "参数验证失败，聚合类型 [MyAggregationParam.AggregationType] 数值无效！";
            return verifyInfo;
        }
        String aggregationColumn = MyModelUtil.mapToColumnName(param.getAggregationField(), modelClass);
        if (StringUtils.isBlank(aggregationColumn)) {
            verifyInfo.errorMsg = "参数验证失败，聚合字段 [MyAggregationParam.AggregationField] 为非法值！";
            return verifyInfo;
        }
        // 一对多场景相对比较简单，直接基于从表的关联键进行group，同时对聚合字段进行聚合计算即可。
        // SQL返回的数据，可直接返回给调用端服务。
        if (param.getAggregationKind() == AggregationKind.ONE_TO_MANY) {
            verifyInfo.groupColumn = MyModelUtil.mapToColumnName(param.getGroupField(), modelClass);
            if (StringUtils.isBlank(verifyInfo.groupColumn)) {
                verifyInfo.errorMsg = "参数验证失败，一对多聚合 [MyAggregationParam.GroupField] 分组字段为非法值！";
                return verifyInfo;
            }
        } else {
            String inFilterColumn = MyModelUtil.mapToColumnName(param.getInFilterField(), modelClass);
            if (StringUtils.isBlank(inFilterColumn)) {
                verifyInfo.errorMsg = "参数验证失败，多对多 [MyAggregationParam.InfilterField] 过滤字段为非法值！";
                return verifyInfo;
            }
            if (MapUtils.isEmpty(param.getGroupedInFilterValues())) {
                verifyInfo.errorMsg = "参数验证失败，多对多 [MyAggregationParam.GroupedInFilterValues] 数据集合不能为空！";
                return verifyInfo;
            }
        }
        verifyInfo.isSuccess = true;
        verifyInfo.aggregationType = aggregationType;
        verifyInfo.aggregationColumn = aggregationColumn;
        return verifyInfo;
    }

    private static final class VerifyAggregationInfo {
        private boolean isSuccess = false;
        private String errorMsg;
        private Integer aggregationType;
        private String aggregationColumn;
        private String groupColumn;
    }
}
