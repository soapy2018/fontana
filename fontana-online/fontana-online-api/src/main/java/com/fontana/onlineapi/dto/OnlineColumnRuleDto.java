package com.fontana.onlineapi.dto;

import com.fontana.base.validate.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 在线表单数据表字段规则和字段多对多关联Dto对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
public class OnlineColumnRuleDto {

    /**
     * 字段Id。
     */
    @NotNull(message = "数据验证失败，字段Id不能为空！", groups = {UpdateGroup.class})
    private Long columnId;

    /**
     * 规则Id。
     */
    @NotNull(message = "数据验证失败，规则Id不能为空！", groups = {UpdateGroup.class})
    private Long ruleId;

    /**
     * 规则属性数据。
     */
    private String propDataJson;
}
