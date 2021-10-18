package com.bluetron.nb.common.base.dto;

import lombok.Data;

import java.util.Set;

/**
 * 登录用户信息
 *
 * @author xingyue.wang
 */
@Data
public class LoginUserDTO implements ILoginAccountDTO {

    private String username;

    /**
     * 对应supOS的staffName
     */
    private String realname;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 员工ID
     * 部分账号没有对应的员工 填0
     */
    private Long personnelId;

    private String accessToken;
    private String essBaseUrl;

    @Override
    public Long getPersonnelId() {
        return personnelId != null ? personnelId : 0L;
    }
}
