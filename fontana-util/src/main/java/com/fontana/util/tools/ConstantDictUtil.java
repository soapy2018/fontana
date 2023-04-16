package com.fontana.util.tools;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fontana.base.annotation.ConstDict;
import com.fontana.base.object.DictModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量字典加载工具类
 * @Author cqf
 * @Date 2022/7/8 10:40
 **/
@Slf4j
public class ConstantDictUtil {

    private ConstantDictUtil(){}

    /**
     * 枚举字典数据
     */
    private final static Map<String, List<DictModel>> ConstDictData = new HashMap<>(5);

    /**
     * 所有枚举java类
     */

    private final static String CLASS_CONSTDICT_PATTERN="/**/constdict/*.class";

    /**
     * 包路径 org.jeecg
     */
    private final static String BASE_PACKAGE = "cn.grab";

    /**
     * 获取枚举类对应的字典数据 SysDictServiceImpl#queryAllDictItems()
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, List<DictModel>> getConstDictData(){
        if(!ConstDictData.keySet().isEmpty()){
            return ConstDictData;
        }
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + CLASS_CONSTDICT_PATTERN;
        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                ConstDict constDict = clazz.getAnnotation(ConstDict.class);
                if (constDict != null) {
                    ConstDict annotation = clazz.getAnnotation(ConstDict.class);
                    String key = annotation.value();
                    Map<Integer, String> dictMap = (Map<Integer, String>) ReflectUtil.getFieldValue(clazz, "DICT_MAP");

                    if(!ObjectUtil.isEmpty(dictMap)){
                        List<DictModel> dictModels = new ArrayList<>();
                        dictMap.forEach((k, v)->{
                            DictModel dictModel = new DictModel(key, Integer.toString(k), v);
                            dictModels.add(dictModel);
                        });
                        ConstDictData.put(key, dictModels);
                    }
                }
            }
        }catch (Exception e){
            log.error("获取常量类字典数据异常", e.getMessage());
        }
        return ConstDictData;
    }

    /**
     * 用于后端字典翻译 SysDictServiceImpl#queryManyDictByKeys(java.util.List, java.util.List)
     * @param dictCodeList 字典编号list
     * @param keys 字典值list
     * @return 字典编号中
     */
    public static Map<String, List<DictModel>> queryManyDictByKeys(List<String> dictCodeList, List<String> keys){
        if(ConstDictData.keySet().isEmpty()){
            getConstDictData();
        }
        Map<String, List<DictModel>> map = new HashMap<>();
        for (String code : ConstDictData.keySet()) {
            if(dictCodeList.contains(code)){
                List<DictModel> dictModelList = ConstDictData.get(code);
                for(DictModel dm: dictModelList){
                    String value = dm.getValue();
                    if(keys.contains(value)){
                        List<DictModel> list = new ArrayList<>();
                        DictModel sysDictItem = new DictModel(code, value, dm.getText());
                        list.add(sysDictItem);
                        map.put(code,list);
                        break;
                    }
                }
            }
        }
        return map;
    }

}
