package com.fontana.swagger2.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.fontana.base.annotation.MyRequestBody;
import com.fontana.base.constant.CommonConstants;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成参数包装类的插件。
 *
 * @author Jerry
 * @date 2020-08-08
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 200)
@ConditionalOnProperty(prefix = CommonConstants.SWAGGER2_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicBodyModelPlugin implements OperationModelsProviderPlugin {

    private final TypeResolver typeResolver;

    public DynamicBodyModelPlugin(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override
    public void apply(RequestMappingContext context) {
        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        if (CollectionUtils.isEmpty(parameterTypes)) {
            return;
        }
        List<ResolvedMethodParameter> bodyParameter = parameterTypes.stream()
                .filter(p -> p.hasParameterAnnotation(MyRequestBody.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bodyParameter)) {
            return;
        }
        String groupName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, context.getGroupName());
        String clazzName = groupName + StringUtils.capitalize(context.getName());
        Class<?> clazz = ByteBuddyUtil.createDynamicModelClass(clazzName, bodyParameter);
        if (clazz != null) {
            context.operationModelsBuilder().addInputParam(typeResolver.resolve(clazz));
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        // 支持2.0版本
        return delimiter == DocumentationType.SWAGGER_2;
    }
}
