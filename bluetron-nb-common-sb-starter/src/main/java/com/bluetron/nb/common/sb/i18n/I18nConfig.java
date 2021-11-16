package com.bluetron.nb.common.sb.i18n;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * @description: 国际化核心配置类
 * @author: Shirman
 * @date: 2020/3/26
 */
@Configuration
@Slf4j
public class I18nConfig {

    public static final int LANG_LENGTH = 2;
    @Value("${spring.mvc.locale:zh_CN}")
    private String locale;

    @Bean
    @ConditionalOnMissingBean(LocaleResolver.class)
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new I18nAcceptHeaderLocaleResolver();
        String[] localeSplit = this.locale.split(CommonConstants.UNDERLINE);
        if (localeSplit.length != LANG_LENGTH) {
            log.error("国际化参数传入错误，参数{}", this.locale);
            throw new IllegalArgumentException("国际化参数传入错误");
        }
        localeResolver.setDefaultLocale(new Locale(localeSplit[0], localeSplit[1]));
        return localeResolver;
    }

    @Bean
    @ConditionalOnMissingBean(AbstractResourceBasedMessageSource.class)
    public ClasspathI18nMessageResource messageResource() {
        ClasspathI18nMessageResource classpathI18nMessageResource = new ClasspathI18nMessageResource();
        return classpathI18nMessageResource;
    }

    @Bean
    public I18nMessageResourceAccessor messageResourceAccessor(ClasspathI18nMessageResource messageResource,
                                                               LocaleResolver localeResolver) {
        I18nMessageResourceAccessor i18nMessageResourceAccessor = new I18nMessageResourceAccessor(messageResource);
        i18nMessageResourceAccessor.setLocaleResolver(localeResolver);
        i18nMessageResourceAccessor.setDefaultLocaleName(this.locale);
        return i18nMessageResourceAccessor;
    }

}
