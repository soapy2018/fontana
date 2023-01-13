package com.fontana.flowservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.db.mapper.BaseModelMapper;
import com.fontana.flowapi.dto.FlowEntryVariableDto;
import com.fontana.flowapi.vo.FlowEntryVariableVo;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

/**
 * 流程变量实体对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@TableName(value = "bn_flow_entry_variable")
public class FlowEntryVariable {

    /**
     * 主键Id。
     */
    @TableId(value = "variable_id")
    private Long variableId;

    /**
     * 流程Id。
     */
    @TableField(value = "entry_id")
    private Long entryId;

    /**
     * 变量名。
     */
    @TableField(value = "variable_name")
    private String variableName;

    /**
     * 显示名。
     */
    @TableField(value = "show_name")
    private String showName;

    /**
     * 流程变量类型。
     */
    @TableField(value = "variable_type")
    private Integer variableType;

    /**
     * 绑定数据源Id。
     */
    @TableField(value = "bind_datasource_id")
    private Long bindDatasourceId;

    /**
     * 绑定数据源关联Id。
     */
    @TableField(value = "bind_relation_id")
    private Long bindRelationId;

    /**
     * 绑定字段Id。
     */
    @TableField(value = "bind_column_id")
    private Long bindColumnId;

    /**
     * 是否内置。
     */
    @TableField(value = "builtin")
    private Boolean builtin;

    /**
     * 创建时间。
     */
    @TableField(value = "create_time")
    private Date createTime;

    @Mapper
    public interface FlowEntryVariableModelMapper extends BaseModelMapper<FlowEntryVariableDto, FlowEntryVariable, FlowEntryVariableVo> {
    }
    public static final FlowEntryVariableModelMapper INSTANCE = Mappers.getMapper(FlowEntryVariableModelMapper.class);
}
