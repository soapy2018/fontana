package com.fontana.sb.i18n;

import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.HttpConstants;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @description: 对http header进行解析，header头为“Content-Language”，根据值确定语言类型
 * 如果取值为空，则默认使用spring 提供的header解析
 * @author: Shirman
 * @date: 2020/3/27
 */
public class I18nAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {
    private static final int LANG_LENGTH = 2;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader(HttpConstants.CONTENT_LANGUAGE_HEADER);
        if (acceptLanguage == null || acceptLanguage.trim().isEmpty()) {
            return super.resolveLocale(request);
        }
        String[] split = acceptLanguage.split(CommonConstants.UNDERLINE);
        if (split.length != LANG_LENGTH) {
            return getDefaultLocale();
        }
        return new Locale(split[0], split[1]);
    }
}
