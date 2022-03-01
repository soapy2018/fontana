package com.bluetron.nb.common.JeeOnline.config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Map;

/**
 * @className: myMaker
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/13 10:02
 */
@Slf4j
public class MyMaker {

    private static final Configuration configuration;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setNumberFormat("0.#####################");
        configuration.setClassForTemplateLoading(MyMaker.class, "/");
    }

    public static String maker(String name, String encoding, Map<String, Object> dataModel) {
        try {
            StringWriter stringWriter = new StringWriter();
            Template template = null;
            template = configuration.getTemplate(name, encoding);
            template.process(dataModel, stringWriter);
            return stringWriter.toString();
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            return exp.toString();
        }
    }

    public static String maker(String name, Map<String, Object> dataModel) {
        return maker(name, "utf-8", dataModel);
    }
}


