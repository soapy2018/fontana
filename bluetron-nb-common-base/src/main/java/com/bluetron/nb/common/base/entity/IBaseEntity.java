package com.bluetron.nb.common.base.entity;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/8/11 9:45
 */
public interface IBaseEntity {

    Long getId();

    String getTenantId();

    void setTenantId(String tenantId);

}
