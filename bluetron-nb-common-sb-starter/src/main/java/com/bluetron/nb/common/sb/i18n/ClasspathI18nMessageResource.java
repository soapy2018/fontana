package com.bluetron.nb.common.sb.i18n;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: 在classpath路径中寻找资源
 * @author: Shirman
 * @date: 2020/3/27
 */
@Slf4j
public class ClasspathI18nMessageResource extends ReloadableResourceBundleMessageSource {

    private BasenameSort basenameSort;

    public ClasspathI18nMessageResource() {
        init();
    }

    protected void init() {
        log.info("start init AppI18nMessageResource...");
        try {
            this.setBasenames(getAllBasename());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        this.setDefaultEncoding(CommonConstants.UTF8);
        log.info("finish init AppI18nMessageResource...");
    }

    public void setBasenameSort(BasenameSort basenameSort) {
        this.basenameSort = basenameSort;
    }

    /**
     * 获取默认路径下国际化资源文件名
     * 
     * @return
     * @throws IOException
     */
    private String[] getAllBasename() throws IOException {
        List<String> baseNames = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(CommonConstants.I18N_PATH_PARENT);
        String defaultBaseName = "";
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            String fileName = resource.getFilename();
            if (!fileName.contains(CommonConstants.UNDERLINE)) {
                String filePath = resource.getURL().toString();
                int lastIndex = filePath.lastIndexOf(CommonConstants.DOT);
                String basename = filePath.substring(0, lastIndex);
                if (CommonConstants.DEFAULT_PROPERTY_FILE.equals(fileName)) {
                    defaultBaseName = basename;
                } else {
                    baseNames.add(basename);
                }
            }
        }
        sortBasenames(baseNames, defaultBaseName);
        log.info("排序后国际资源文件:");
        baseNames.forEach(t -> {
            log.info("{} ", t);
        });
        return baseNames.toArray(new String[baseNames.size()]);
    }

    /**
     * 排序结果影响键值冲突时的覆盖 覆盖规则，前覆盖后 开放了排序接口，用户只需要实现这个排序能力即可使用
     * 
     * @param baseNames       国际化资源文件的路径名
     * @param defaultBaseName
     */
    private void sortBasenames(List<String> baseNames, String defaultBaseName) {
        if (basenameSort != null) {
            // 系统属性的优先级也交给用户定义
            baseNames.add(defaultBaseName);
            basenameSort.sort(baseNames);
        } else {
            Collections.sort(baseNames);
            // 默认的系统定义允许被覆盖
            baseNames.add(defaultBaseName);
        }
    }

    public static void main(String[] args) {
        ClasspathI18nMessageResource appI18nMessageResource = new ClasspathI18nMessageResource();
    }
}
