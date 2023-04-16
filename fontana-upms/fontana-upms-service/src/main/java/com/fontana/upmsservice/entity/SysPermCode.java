package com.fontana.upmsservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.base.annotation.RelationManyToMany;
import com.fontana.db.model.BaseModel;
import com.fontana.db.mapper.BaseModelMapper;
import com.fontana.upmsapi.dto.SysPermCodeDto;
import com.fontana.upmsapi.vo.SysPermCodeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * 权限字实体对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bn_sys_perm_code")
public class SysPermCode extends BaseModel {

    /**
     * 主键Id。
     */
    @TableId(value = "perm_code_id")
    private Long permCodeId;

    /**
     * 上级权限字Id。
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 权限字标识(一般为有含义的英文字符串)。
     */
    @TableField(value = "perm_code")
    private String permCode;

    /**
     * 权限类型(0: 表单 1: UI片段 2: 操作)。
     */
    @TableField(value = "perm_code_type")
    private Integer permCodeType;

    /**
     * 显示名称。
     */
    @TableField(value = "show_name")
    private String showName;

    /**
     * 显示顺序(数值越小，越靠前)。
     */
    @TableField(value = "show_order")
    private Integer showOrder;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;

    @RelationManyToMany(
            relationMapperName = "sysPermCodePermMapper",
            relationMasterIdField = "permCodeId",
            relationModelClass = SysPermCodePerm.class)
    @TableField(exist = false)
    private List<SysPermCodePerm> sysPermCodePermList;

    @Mapper
    public interface SysPermCodeModelMapper extends BaseModelMapper<SysPermCodeDto, SysPermCode, SysPermCodeVo> {
        /**
         * 转换DTO对象到实体对象。
         *
         * @param sysPermCodeDto 域对象。
         * @return 实体对象。
         */
        @Override
        SysPermCode toModel(SysPermCodeDto sysPermCodeDto);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysPermCode 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "sysPermCodePermList", expression = "java(beanToMap(sysPermCode.getSysPermCodePermList(), false))")
        @Override
        SysPermCodeVo fromModel(SysPermCode sysPermCode);
    }
    public static final SysPermCodeModelMapper INSTANCE = Mappers.getMapper(SysPermCodeModelMapper.class);
}
