package com.fontana.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fontana.db.property.MybatisPlusAutoFillProperties;
import com.fontana.util.request.WebContextUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 自定义填充公共字段
 *
 * @author cqf
 * @date 2021/11/11
 * <p>
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

        Date date = new Date();
        if (createTime == null) {
            setFieldValByName(autoFillProperties.getCreateTimeField(), date, metaObject);
        }
        if (updateTime == null) {
            setFieldValByName(autoFillProperties.getUpdateTimeField(), date, metaObject);
        }
        if (createUserId == null) {
            setFieldValByName(autoFillProperties.getCreateUserIdField(), WebContextUtil.takeTokenFromRequest().getUserId(), metaObject);
        }
        if (updateUserId == null) {
            setFieldValByName(autoFillProperties.getUpdateUserIdField(), WebContextUtil.takeTokenFromRequest().getUserId(), metaObject);
        }

    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
        setFieldValByName(autoFillProperties.getUpdateUserIdField(), WebContextUtil.takeTokenFromRequest().getUserId(), metaObject);
    }
}