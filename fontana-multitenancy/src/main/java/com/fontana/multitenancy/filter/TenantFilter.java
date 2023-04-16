package com.fontana.multitenancy.filter;

import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.HttpConstants;
import com.fontana.base.context.TenantContextHolder;
import com.fontana.util.request.WebContextUtil;
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
            String tenantId = WebContextUtil.getTenantId(request);
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
