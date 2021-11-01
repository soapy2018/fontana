package com.bluetron.nb.common.sb.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    public SpringContextHolder() {

    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException{
        return applicationContext.getBeansOfType(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

    /**
     * 当前生效profile
     * @return
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 当前生效profiles
     * @return
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }
}