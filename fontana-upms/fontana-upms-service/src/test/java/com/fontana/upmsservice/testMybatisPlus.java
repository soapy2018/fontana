package com.fontana.upmsservice;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.Arrays;

/**
 * @className: testMybatisPlus
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/9/27 9:52
 */
public class testMybatisPlus {
    @Test
    @SneakyThrows
    public void testResolve() {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] res;
        res = resourceResolver.getResources("classpath:com/fontana/upmsservice/mapper/xml/*Mapper.xml");
        Arrays.stream(res).count();
    }
}


