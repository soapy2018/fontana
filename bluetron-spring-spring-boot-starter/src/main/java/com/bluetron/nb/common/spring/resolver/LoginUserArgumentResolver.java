package com.bluetron.nb.common.spring.resolver;

import com.bluetron.nb.common.spring.context.RequestLoginContextHolder;
import com.bluetron.nb.common.spring.dto.LoginUserDTO;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 解析 LoginUserDTO 对象
 *
 * @author genx
 * @date 2021/4/2 9:53
 */
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

 @Override
 public boolean supportsParameter(MethodParameter methodParameter) {
  return LoginUserDTO.class.isAssignableFrom(methodParameter.getParameterType());
 }

 @Override
 public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
  return RequestLoginContextHolder.getCurrentLoginUser();
 }
}



