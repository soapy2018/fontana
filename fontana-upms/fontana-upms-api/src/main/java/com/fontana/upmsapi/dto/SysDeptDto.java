package com.fontana.upmsapi.dto;

import com.fontana.base.validate.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SysDeptDto对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@ApiModel("SysDeptDto对象")
@Data
public class SysDeptDto {

    /**
     * 部门Id。
     */
    @ApiModelProperty(value = "部门Id", required = true)
    @NotNull(message = "数据验证失败，部门Id不能为空！", groups = {UpdateGroup.class})
    private Long deptId;

    /**
     * 部门名称。
     */
    @ApiModelProperty(value = "部门名称", required = true)
    @NotBlank(message = "数据验证失败，部门名称不能为空！")
    private String deptName;

    /**
     * 显示顺序。
     */
    @ApiModelProperty(value = "显示顺序", required = true)
    @NotNull(message = "数据验证失败，显示顺序不能为空！")
    private Integer showOrder;

    /**
     * 父部门Id。
     */
    @ApiModelProperty(value = "父部门Id")
    private Long parentId;
}
