package com.bluetron.nb.common.util.tools;

import com.bluetron.nb.common.base.exception.GeneralException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @author
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    public SpringContextHolder() {

    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }

    /**
     * 获取应用上下文对象。
     *
     * @return 应用上下文。
     */
    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        assertApplicationContext();
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 根据Bean的ClassType，获取Bean对象列表。
     *
     * @param beanType Bean的Class类型。
     * @param <T>      返回的Bean类型。
     * @return Bean对象列表。
     */
    public static <T> Collection<T> getBeanListOfType(Class<T> beanType) {
        assertApplicationContext();
        Map<String, T> beanMap = applicationContext.getBeansOfType(beanType);
        return beanMap == null ? null : beanMap.values();
    }

    /**
     * 根据BeanName，获取Bean对象。
     *
     * @param beanName Bean名称。
     * @param <T> 返回的Bean类型。
     * @return Bean对象。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 当前生效profile
     *
     * @return
     */
    public static String getActiveProfile() {
        assertApplicationContext();
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 当前生效profiles
     *
     * @return
     */
    public static String[] getActiveProfiles() {
        assertApplicationContext();
        return applicationContext.getEnvironment().getActiveProfiles();
    }



    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new GeneralException("applicaitonContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }
}