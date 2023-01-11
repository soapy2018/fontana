package com.fontana.onlineservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.base.annotation.RelationOneToMany;
import com.fontana.db.mapper.BaseModelMapper;
import com.fontana.onlineapi.dto.OnlineTableDto;
import com.fontana.onlineapi.vo.OnlineTableVo;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 在线表单的数据表实体对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@TableName(value = "bn_online_table")
public class OnlineTable {

    /**
     * 主键Id。
     */
    @TableId(value = "table_id")
    private Long tableId;

    /**
     * 表名称。
     */
    @TableField(value = "table_name")
    private String tableName;

    /**
     * 实体名称。
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * 数据库链接Id。
     */
    @TableField(value = "dblink_id")
    private Long dblinkId;

    /**
     * 更新时间。
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建时间。
     */
    @TableField(value = "create_time")
    private Date createTime;

    @RelationOneToMany(
            masterIdField = "tableId",
            slaveServiceName = "onlineColumnService",
            slaveModelClass = OnlineColumn.class,
            slaveIdField = "tableId")
    @TableField(exist = false)
    private List<OnlineColumn> columnList;

    /**
     * 该字段会被缓存，因此在线表单执行操作时可以从缓存中读取该数据，并可基于columnId进行快速检索。
     */
    @TableField(exist = false)
    private Map<Long, OnlineColumn> columnMap;

    /**
     * 当前表的主键字段，该字段仅仅用于动态表单运行时的SQL拼装。
     */
    @TableField(exist = false)
    private OnlineColumn primaryKeyColumn;

    /**
     * 当前表的逻辑删除字段，该字段仅仅用于动态表单运行时的SQL拼装。
     */
    @TableField(exist = false)
    private OnlineColumn logicDeleteColumn;

    @Mapper
    public interface OnlineTableModelMapper extends BaseModelMapper<OnlineTableDto, OnlineTable, OnlineTableVo> {
        /**
         * 转换Dto对象到实体对象。
         *
         * @param onlineTableDto 域对象。
         * @return 实体对象。
         */
        @Override
        OnlineTable toModel(OnlineTableDto onlineTableDto);
        /**
         * 转换实体对象到VO对象。
         *
         * @param onlineTable 实体对象。
         * @return 域对象。
         */
        @Override
        OnlineTableVo fromModel(OnlineTable onlineTable);
    }
    public static final OnlineTableModelMapper INSTANCE = Mappers.getMapper(OnlineTableModelMapper.class);
}
