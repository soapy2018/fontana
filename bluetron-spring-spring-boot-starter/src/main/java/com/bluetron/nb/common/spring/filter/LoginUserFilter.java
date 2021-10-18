package com.bluetron.nb.common.spring.filter;


import com.alibaba.fastjson.JSON;
import com.bluetron.nb.common.base.constant.HttpConstants;
import com.bluetron.nb.common.spring.context.RequestLoginContextHolder;
import com.bluetron.nb.common.spring.dto.LoginPersonnelDTO;
import com.bluetron.nb.common.spring.dto.LoginUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/7/30 10:19
 */
@Component
public class LoginUserFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest) {
                String text = ((HttpServletRequest) servletRequest).getHeader(HttpConstants.AUTHORIZATION_HEADER);
                if (StringUtils.isNotBlank(text)) {
                    try {
                        LoginUserDTO loginUserDTO = JSON.parseObject(URLDecoder.decode(text, "UTF-8"), LoginUserDTO.class);
                        if (StringUtils.isNotBlank(loginUserDTO.getTenantId())) {
                            RequestLoginContextHolder.setCurrentLoginUser(loginUserDTO);
                        }
                    } catch (Exception e) {
                        logger.warn("LoginUserDTO解析失败", e);
                    }
                }

                text = ((HttpServletRequest) servletRequest).getHeader(HttpConstants.PERSONNEL_AUTH);
                if (StringUtils.isNotBlank(text)) {
                    try {
                        LoginPersonnelDTO loginPersonnelDTO = JSON.parseObject(URLDecoder.decode(text, "UTF-8"), LoginPersonnelDTO.class);
                        if (loginPersonnelDTO.getPersonnelId() != null && StringUtils.isNotBlank(loginPersonnelDTO.getTenantId())) {
                            RequestLoginContextHolder.setCurrentLoginPersonnel(loginPersonnelDTO);
                        }
                    } catch (Exception e) {
                        logger.warn("LoginPersonnelDTO解析失败", e);
                    }
                }
            }

            //执行
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            //完成请求后 将 ThreadLocal 值置空
            RequestLoginContextHolder.setCurrentLoginUser(null);
            RequestLoginContextHolder.setCurrentLoginPersonnel(null);
        }
    }

    @Override
    public void destroy() {

    }
}
