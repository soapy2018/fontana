package com.fontana.sb.i18n;

import com.fontana.base.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @description: 提供根据key获取对应语言环境下的value
 * @author: Shirman
 * @date: 2020/3/26
 */

/**
 * @description: 提供根据key获取对应语言环境下的value
 * @author: cqf
 * @modifyDate: 2021/9/24
 */
@Slf4j
public class I18nMessageResourceAccessor {
    public String defaultLocaleName = CommonConstants.DEFAULT_LOCAL_NAME;
    private MessageSourceAccessor messageSourceAccessor;
    private LocaleResolver localeResolver;

    public I18nMessageResourceAccessor(AbstractResourceBasedMessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * 根据key获取文本
     * 优先从head头取Local信息，取不到则用默认Local
     *
     * @param key
     * @return
     */
    public String getMessage(String key) {

        Locale locale = getLocaleFromRequest();
        if (null != locale) {
            log.debug("key is {} locale is {}", key, locale);
            return getMessage(key, locale, null);
        }
        locale = chooseLocale(this.defaultLocaleName);
        return getMessage(key, locale);
    }

    /**
     * @param key
     * @param lang 语言环境，与bundle resource 文件名对应
     * @return
     */
    public String getMessage(String key, String lang) {
        Locale locale = chooseLocale(lang);
        return getMessage(key, locale);
    }

    /**
     * 设置默认的语言环境
     *
     * @param defaultLocaleName
     */
    public void setDefaultLocaleName(String defaultLocaleName) {
        this.defaultLocaleName = defaultLocaleName;
    }

    /**
     * @param key
     * @param locale
     * @return
     */
    public String getMessage(String key, Locale locale) {
        return getMessage(key, locale, null);
    }

    /**
     * @param key        key
     * @param locale     国家
     * @param defaultMsg 默认值
     * @return
     */
    public String getMessage(String key, Locale locale, String defaultMsg) {
        String result = null;
        result = messageSourceAccessor.getMessage(key, null, defaultMsg, locale);
        if (StringUtils.isEmpty(result)) {
            log.warn("未获取到 key:{}----local:{} 国际化资源值", key, locale);
        }
        return result;
    }

    /**
     * 根据传入的语言获取语言环境
     *
     * @param lang
     * @return
     */
    private Locale getLocale(String lang) {
        return new Locale(lang.split(CommonConstants.UNDERLINE)[0], lang.split(CommonConstants.UNDERLINE)[1]);
    }

    /**
     * 选择语言环境
     *
     * @param lang
     * @return
     */
    private Locale chooseLocale(String lang) {
        Locale locale;
        if (!StringUtils.isEmpty(lang)) {
            locale = getLocale(lang);
        } else {
            locale = getLocale(defaultLocaleName);
        }
        return locale;
    }

    /**
     * 从请求中获取Local，spring默认区域解析器是AcceptHeaderLocaleResolver
     * I18nAcceptHeaderLocaleResolver继承AcceptHeaderLocaleResolver，覆盖resolveLocale方法
     */
    private Locale getLocaleFromRequest() {
        ServletRequestAttributes attr = null;
        try {
            attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException e) {
            return null;
        }
        HttpServletRequest request = attr.getRequest();
        Locale locale = localeResolver.resolveLocale(request);
        return locale;
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
}
