package com.bluetron.nb.common.base.dto;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/8/18 9:35
 */
public interface ILoginAccountDTO {

    /**
     * 租户ID
     *
     * @return
     */
    String getTenantId();

    /**
     * 员工ID
     *
     * @return
     */
    Long getPersonnelId();

    /**
     * 编号
     *
     * @return
     */
    String getUsername();

    /**
     * 真实姓名
     *
     * @return
     */
    String getRealname();

    /**
     * 权限列表
     *
     * @return
     */
    Set<String> getRoles();

    String getEssBaseUrl();

}
