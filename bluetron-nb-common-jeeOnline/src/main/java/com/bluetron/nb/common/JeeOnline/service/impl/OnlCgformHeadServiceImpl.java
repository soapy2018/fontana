package com.bluetron.nb.common.JeeOnline.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.base.exception.GeneralException;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.db.service.IDbTableHandle;
import com.bluetron.nb.common.db.util.DbTypeUtils;
import com.bluetron.nb.common.db.util.JdbcUtil;
import com.bluetron.nb.common.JeeOnline.config.DbOperator;
import com.bluetron.nb.common.JeeOnline.dto.OnlCgformDto;
import com.bluetron.nb.common.JeeOnline.entity.*;
import com.bluetron.nb.common.JeeOnline.mapper.OnlCgformButtonMapper;
import com.bluetron.nb.common.JeeOnline.mapper.OnlCgformEnhanceJsMapper;
import com.bluetron.nb.common.JeeOnline.mapper.OnlCgformHeadMapper;
import com.bluetron.nb.common.JeeOnline.model.TableInfo;
import com.bluetron.nb.common.JeeOnline.service.IOnlCgformFieldService;
import com.bluetron.nb.common.JeeOnline.service.IOnlCgformHeadService;
import com.bluetron.nb.common.JeeOnline.service.IOnlCgformIndexService;
import com.bluetron.nb.common.util.lang.StringUtil;
import com.bluetron.nb.common.util.tools.IdUtil;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @className: OnlCgformHeadServiceImpl
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/11 14:15
 */
public class OnlCgformHeadServiceImpl extends ServiceImpl<OnlCgformHeadMapper, OnlCgformHead> implements IOnlCgformHeadService {

    @Autowired
    private IOnlCgformFieldService fieldService;
    @Autowired
    private IOnlCgformIndexService indexService;
    @Autowired
    private OnlCgformButtonMapper onlCgformButtonMapper;
    @Autowired
    private OnlCgformEnhanceJsMapper onlCgformEnhanceJsMapper;

    @Override
    public void initCopyState(List<OnlCgformHead> headList) {
        List physicIdList = this.baseMapper.queryCopyPhysicId();
        Iterator it = headList.iterator();

        while (it.hasNext()) {
            OnlCgformHead onlCgformHead = (OnlCgformHead) it.next();
            if (physicIdList.contains(onlCgformHead.getId())) {
                onlCgformHead.setHascopy(1);
            } else {
                onlCgformHead.setHascopy(0);
            }
        }

    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public Result<?> addAll(OnlCgformDto onlCgformDto) {
        String headId = IdUtil.generateUUID();
        OnlCgformHead onlCgformHead = onlCgformDto.getHead();
        List fieldList = onlCgformDto.getFields();
        List indexList = onlCgformDto.getIndexs();
        onlCgformHead.setId(headId);

        for (int i = 0; i < fieldList.size(); ++i) {
            OnlCgformField onlCgformField = (OnlCgformField) fieldList.get(i);
            onlCgformField.setId(null);
            onlCgformField.setCgformHeadId(headId);
            if (onlCgformField.getOrderNum() == null) {
                onlCgformField.setOrderNum(i);
            }

            this.addition(onlCgformField);
        }

        Iterator iterator = indexList.iterator();

        while (iterator.hasNext()) {
            OnlCgformIndex onlCgformIndex = (OnlCgformIndex) iterator.next();
            onlCgformIndex.setId(null);
            onlCgformIndex.setCgformHeadId(headId);
        }

        onlCgformHead.setIsDbSynch("N");
        onlCgformHead.setQueryMode("single");
        onlCgformHead.setTableVersion(1);
        onlCgformHead.setCopyType(0);
        if (onlCgformHead.getTableType() == 3 && onlCgformHead.getTabOrderNum() == null) {
            onlCgformHead.setTabOrderNum(1);
        }

        super.save(onlCgformHead);
        this.fieldService.saveBatch(fieldList);
        this.indexService.saveBatch(indexList);
        //若是附属表需更新主表
        this.updateMainOnlCgformHead(onlCgformHead, fieldList);
        return Result.succeed("添加成功");
    }

    @Override
    public void doDbSynch(String code, String synMethod) throws /*HibernateException,*/ IOException, TemplateException, SQLException {
        OnlCgformHead onlCgformHead = (OnlCgformHead)this.getById(code);
        if (onlCgformHead == null) {
            throw new GeneralException("实体配置不存在");
        } else {
            String tableName = onlCgformHead.getTableName();
            LambdaQueryWrapper<OnlCgformField> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, code);
            lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
            List fieldList = this.fieldService.list(lambdaQueryWrapper);
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTableName(tableName);
            tableInfo.setJformPkType(onlCgformHead.getIdType());
            tableInfo.setJformPkSequence(onlCgformHead.getIdSequence());
            tableInfo.setContent(onlCgformHead.getTableTxt());
            tableInfo.setColumns(fieldList);


            DbType dbType = JdbcUtil.getDatabaseTypeEnum();
            if ("normal".equals(synMethod) && !dbType.equals(DbType.SQLITE)) {
                long var22 = System.currentTimeMillis();
                boolean isTableExit = JdbcUtil.isTableExist(tableName, dbType);
                if (isTableExit) {

                    DbOperator dbOperator = new DbOperator(dbType);

                    List alterTableSql = dbOperator.buildAlterTableSql(tableInfo, dbType);

                    Iterator iterator = alterTableSql.iterator();

                    while(iterator.hasNext()) {
                        String sqlStr = (String)iterator.next();
                        if (!StringUtil.isEmpty(sqlStr) && !StringUtil.isEmpty(sqlStr.trim())) {
                            this.baseMapper.executeDDL(sqlStr);
                        }
                    }

                    List onlCgformIndexList = this.indexService.list(new LambdaQueryWrapper<OnlCgformIndex>().eq(OnlCgformIndex::getCgformHeadId, code));

                    iterator = onlCgformIndexList.iterator();

                    label63:
                    while(true) {
                        OnlCgformIndex onlCgformIndex;
                        do {
                            if (!iterator.hasNext()) {
                                break label63;
                            }

                            onlCgformIndex = (OnlCgformIndex)iterator.next();
                        } while(!"N".equals(onlCgformIndex.getIsDbSynch()) && !CommonConstants.DEL_FLAG_1.equals(onlCgformIndex.getDelFlag()));

                        String countIndexSql = dbOperator.buildCountIndexSql(onlCgformIndex.getIndexName(), tableName);
                        if (this.indexService.isExistIndex(countIndexSql)) {
                            String dropIndexSql = dbOperator.buildDropIndexSql(onlCgformIndex.getIndexName(), tableName);

                            try {
                                this.baseMapper.executeDDL(dropIndexSql);
                                if (CommonConstants.DEL_FLAG_1.equals(onlCgformIndex.getDelFlag())) {
                                    this.indexService.removeById(onlCgformIndex.getId());
                                }
                            } catch (Exception exp) {
                                log.error("删除表【" + tableName + "】索引(" + onlCgformIndex.getIndexName() + ")失败!", exp);
                            }
                        } else if (CommonConstants.DEL_FLAG_1.equals(onlCgformIndex.getDelFlag())) {
                            this.indexService.removeById(onlCgformIndex.getId());
                        }
                    }
                } else {
                    //创建表
                    DbOperator.createTable(tableInfo);
                }
            } else if ("force".equals(synMethod) || dbType.equals(DbType.SQLITE)) {
                IDbTableHandle dbTableHandle = JdbcUtil.getDbTableHandle(dbType);
                String dropTableSQL = dbTableHandle.dropTableSQL(tableName);
                this.baseMapper.executeDDL(dropTableSQL);
                DbOperator.createTable(tableInfo);
            }

            this.indexService.createIndex(code, DbTypeUtils.getDbTypeString(dbType), tableName);
            onlCgformHead.setIsDbSynch("Y");
            if (onlCgformHead.getTableVersion() == 1) {
                onlCgformHead.setTableVersion(2);
            }

            this.updateById(onlCgformHead);
        }
    }

    private void addition(OnlCgformField onlCgformField) {
        if ("Text".equals(onlCgformField.getDbType()) || "Blob".equals(onlCgformField.getDbType())) {
            onlCgformField.setDbLength(0);
            onlCgformField.setDbPointLength(0);
        }
    }

    private void updateMainOnlCgformHead(OnlCgformHead onlCgformHead, List<OnlCgformField> onlCgformFieldList) {
        if (onlCgformHead.getTableType() == 3) {
            onlCgformHead = this.baseMapper.selectById(onlCgformHead.getId());

            for (int i = 0; i < onlCgformFieldList.size(); ++i) {
                OnlCgformField onlCgformField = onlCgformFieldList.get(i);
                String mainTable = onlCgformField.getMainTable();
                if (!StringUtil.isEmpty(mainTable)) {
                    OnlCgformHead onlCgformHeadMain = this.baseMapper.selectOne(new LambdaQueryWrapper<OnlCgformHead>().eq(OnlCgformHead::getTableName, mainTable));
                    if (onlCgformHeadMain != null) {
                        String subTableStr = onlCgformHeadMain.getSubTableStr();
                        if (StringUtil.isEmpty(subTableStr)) {
                            subTableStr = onlCgformHead.getTableName();
                        } else if (!subTableStr.contains(onlCgformHead.getTableName())) {
                            ArrayList subTableList = new ArrayList(Arrays.asList(subTableStr.split(",")));

                            for (int j = 0; j < subTableList.size(); ++j) {
                                String subTable = (String) subTableList.get(j);
                                OnlCgformHead onlCgformHeadSub = this.baseMapper.selectOne(new LambdaQueryWrapper<OnlCgformHead>().eq(OnlCgformHead::getTableName, subTable));
                                if (onlCgformHeadSub != null && onlCgformHead.getTabOrderNum() < onlCgformHeadSub.getTabOrderNum()) {
                                    subTableList.add(j, onlCgformHead.getTableName());
                                    break;
                                }
                            }

                            if (!subTableList.contains(onlCgformHead.getTableName())) {
                                subTableList.add(onlCgformHead.getTableName());
                            }

                            subTableStr = String.join(",", subTableList);
                        }

                        onlCgformHeadMain.setSubTableStr(subTableStr);
                        this.baseMapper.updateById(onlCgformHeadMain);
                        break;
                    }
                }
            }
        } else {
            List onlCgformHeadList = this.baseMapper.selectList(new LambdaQueryWrapper<OnlCgformHead>().like(OnlCgformHead::getSubTableStr, onlCgformHead.getTableName()));
            if (onlCgformHeadList != null && onlCgformHeadList.size() > 0) {
                Iterator iterator = onlCgformHeadList.iterator();

                while (iterator.hasNext()) {
                    OnlCgformHead onlCgformHeadMain = (OnlCgformHead) iterator.next();
                    String subTableStr = onlCgformHeadMain.getSubTableStr();
                    if (onlCgformHeadMain.getSubTableStr().equals(onlCgformHead.getTableName())) {
                        subTableStr = "";
                    } else if (onlCgformHeadMain.getSubTableStr().startsWith(onlCgformHead.getTableName() + ",")) {
                        subTableStr = subTableStr.replace(onlCgformHead.getTableName() + ",", "");
                    } else if (onlCgformHeadMain.getSubTableStr().endsWith("," + onlCgformHead.getTableName())) {
                        subTableStr = subTableStr.replace("," + onlCgformHead.getTableName(), "");
                    } else if (onlCgformHeadMain.getSubTableStr().contains("," + onlCgformHead.getTableName() + ",")) {
                        subTableStr = subTableStr.replace("," + onlCgformHead.getTableName() + ",", ",");
                    }

                    onlCgformHeadMain.setSubTableStr(subTableStr);
                    this.baseMapper.updateById(onlCgformHeadMain);
                }
            }
        }

    }

    @Override
    public List<OnlCgformButton> queryButtonList(String code, boolean isListButton) {
        LambdaQueryWrapper<OnlCgformButton> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformButton::getButtonStatus, "1");
        lambdaQueryWrapper.eq(OnlCgformButton::getCgformHeadId, code);
        if (isListButton) {
            lambdaQueryWrapper.in(OnlCgformButton::getButtonStyle, new Object[]{"link", "button"});
        } else {
            lambdaQueryWrapper.eq(OnlCgformButton::getButtonStyle, "form");
        }

        lambdaQueryWrapper.orderByAsc(OnlCgformButton::getOrderNum);
        return this.onlCgformButtonMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public OnlCgformEnhanceJs queryEnhanceJs(String formId, String cgJsType) {
        LambdaQueryWrapper<OnlCgformEnhanceJs> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformEnhanceJs::getCgformHeadId, formId);
        lambdaQueryWrapper.eq(OnlCgformEnhanceJs::getCgJsType, cgJsType);
        return (OnlCgformEnhanceJs)this.onlCgformEnhanceJsMapper.selectOne(lambdaQueryWrapper);
    }
}


