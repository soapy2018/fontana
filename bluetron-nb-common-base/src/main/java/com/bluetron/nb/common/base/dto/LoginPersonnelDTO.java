package com.bluetron.nb.common.base.dto;

import lombok.Data;

import java.util.Set;


/**
 * 登录人员信息（对应personnel）
 *
 */
@Data
public class LoginPersonnelDTO implements ILoginAccountDTO {
    private Long personnelId;
    private String tenantId;
    private String personnelCode;
    private String realname;
    private Set<String> roles;

    private String essBaseUrl;

    @Override
    public String getUsername() {
        return personnelCode;
    }
}
