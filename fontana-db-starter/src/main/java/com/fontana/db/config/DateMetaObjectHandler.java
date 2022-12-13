package com.fontana.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fontana.db.property.MybatisPlusAutoFillProperties;
import com.fontana.util.request.WebContextUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自动填充公共字段
 * 实现步骤：
 * 1.实现MetaObjectHandler接口：
 * 2. 实体类上添加注解（FieldFill）
 * 不生效场景：
 * 1. 使用mapper.xml的sql不生效
 * 必须使用mybatis的api进行插入和更新操作。
 * 2. boolean update(Wrapper updateWrapper) 不生效
 *
 * @author cqf
 * @date 2021/11/11
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

    private MybatisPlusAutoFillProperties autoFillProperties;

    public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充 配合实体字段@TableField(fill = FieldFill.INSERT)
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(), metaObject);
        Object createUserId = getFieldValByName(autoFillProperties.getCreateUserIdField(), metaObject);
        Object updateUserId = getFieldValByName(autoFillProperties.getUpdateUserIdField(), metaObject);
        Object createUserName = getFieldValByName(autoFillProperties.getCreateUserNameField(), metaObject);
        Object updateUserName = getFieldValByName(autoFillProperties.getUpdateUserNameField(), metaObject);

        Date date = new Date();
        if (createTime == null) {
            setFieldValByName(autoFillProperties.getCreateTimeField(), date, metaObject);
        }
        if (updateTime == null) {
            setFieldValByName(autoFillProperties.getUpdateTimeField(), date, metaObject);
        }
        if (createUserId == null && WebContextUtil.hasRequestContext()) {
            //todo userId统一从哪里拿？
            setFieldValByName(autoFillProperties.getCreateUserIdField(), Long.valueOf(WebContextUtil.getUserId()), metaObject);
        }
        if (createUserName == null && WebContextUtil.hasRequestContext()) {
            //todo userName统一从哪里拿？
            setFieldValByName(autoFillProperties.getCreateUserNameField(), WebContextUtil.getUserShowName(), metaObject);
        }
        if (updateUserId == null && WebContextUtil.hasRequestContext()) {
            //todo userId统一从哪里拿？
            setFieldValByName(autoFillProperties.getUpdateUserIdField(), Long.valueOf(WebContextUtil.getUserId()), metaObject);
        }
        if (updateUserName == null && WebContextUtil.hasRequestContext()) {
            //todo userName统一从哪里拿？
            setFieldValByName(autoFillProperties.getUpdateUserNameField(), WebContextUtil.getUserShowName(), metaObject);
        }

    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
        //todo userId统一从哪里拿？
        if (WebContextUtil.hasRequestContext()) {
            setFieldValByName(autoFillProperties.getUpdateUserIdField(), Long.valueOf(WebContextUtil.getUserId()), metaObject);
        }
        //todo userName统一从哪里拿？
        if (WebContextUtil.hasRequestContext()) {
            setFieldValByName(autoFillProperties.getUpdateUserNameField(), WebContextUtil.getUserShowName(), metaObject);
        }
    }
}