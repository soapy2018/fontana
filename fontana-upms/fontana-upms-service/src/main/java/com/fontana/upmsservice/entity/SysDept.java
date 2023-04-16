package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.db.model.BaseModel;
import com.fontana.db.mapper.BaseModelMapper;
import com.fontana.upmsapi.dto.SysDeptDto;
import com.fontana.upmsapi.vo.SysDeptVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * SysDept实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bn_sys_dept")
public class SysDept extends BaseModel {

    /**
     * 部门Id。
     */
    @TableId(value = "dept_id")
    private Long deptId;

    /**
     * 部门名称。
     */
    @TableField(value = "dept_name")
    private String deptName;

    /**
     * 显示顺序。
     */
    @TableField(value = "show_order")
    private Integer showOrder;

    /**
     * 父部门Id。
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;

    @Mapper
    public interface SysDeptModelMapper extends BaseModelMapper<SysDeptDto, SysDept, SysDeptVo> {
    }
    public static final SysDeptModelMapper INSTANCE = Mappers.getMapper(SysDeptModelMapper.class);
}
