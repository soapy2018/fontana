package com.bluetron.nb.common.spring.resolver;

import com.bluetron.nb.common.spring.context.RequestLoginContextHolder;
import com.bluetron.nb.common.spring.dto.LoginPersonnelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Description:
 * @author genx
 * @date 2021/4/22 15:33
 */
public class LoginPersonnelArgumentResolver implements HandlerMethodArgumentResolver {

 private Logger logger = LoggerFactory.getLogger(getClass());

 @Override
 public boolean supportsParameter(MethodParameter methodParameter) {
  return LoginPersonnelDTO.class.isAssignableFrom(methodParameter.getParameterType());
 }

 @Override
 public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
  return RequestLoginContextHolder.getCurrentLoginPersonnel();
 }
}



