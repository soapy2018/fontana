package com.bluetron.nb.common.db.filter;

import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.base.constant.HttpConstants;
import com.bluetron.nb.common.base.context.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 租户过滤器
 *
 * @author cqf
 * @date 2021/9/15
 */
@Component
@ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "enabled", havingValue = "true")
public class TenantFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            //优先获取请求参数中的tenantId值
            String tenantId = request.getParameter(HttpConstants.TENANT_ID);
            if (StringUtils.isEmpty(tenantId)) {
                tenantId = request.getHeader(HttpConstants.TENANT_ID_HEADER);
            }
            //保存租户id
            if (StringUtils.isNotEmpty(tenantId)) {
                TenantContextHolder.setTenant(tenantId);
            } else {
                TenantContextHolder.setDefaultTenant();
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}
