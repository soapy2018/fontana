package com.fontana.redis.util;

import com.fontana.base.object.DictModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Session数据缓存辅助类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SuppressWarnings("unchecked")
public class DictCacheHelper {

    private static final int DEFAULT_TTL = 3600000;
    private static final String DICT_KEY = "sys:dict";

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    /**
     * 定义cache名称、超时时长(毫秒)。
     */
    public enum CacheEnum {
        /**
         * session下上传文件名的缓存(时间是24小时)。
         */
        UPLOAD_FILENAME_CACHE(86400000),
        /**
         * 缺省全局缓存(时间是24小时)。
         */
        GLOBAL_CACHE(86400000);

        /**
         * 缓存的时长(单位：毫秒)
         */
        private int ttl = DEFAULT_TTL;

        CacheEnum() {
        }

        CacheEnum(int ttl) {
            this.ttl = ttl;
        }

        public int getTtl() {
            return ttl;
        }
    }

    /**
     * 更新缓存的所有字典
     * @param dictMap
     */
    public void refreshAllDict(Map<String, List<DictModel>> dictMap) {
        redisTemplateUtil.del(DICT_KEY);
        redisTemplateUtil.putHashValues(DICT_KEY, dictMap);
    }

    /**
     * 判断字典是否存在。
     * @param dictCode 字典编码
     * @return true表示该文件是由当前session上传并存储在本地的，否则false。
     */
    public boolean existDict(String dictCode) {
        if (dictCode == null) {
            return false;
        }
        return redisTemplateUtil.opsForHash().hasKey(DICT_KEY, dictCode);
    }

    /**
     * 新增或更新字典
     * @param dictCode 字典编码
     * @param dictModelList 字典项list
     * @return
     */
    public void putDict(String dictCode, List<DictModel> dictModelList) {
        if (dictCode == null) {
            return;
        }
        redisTemplateUtil.opsForHash().put(DICT_KEY, dictCode, dictModelList);
    }

    /**
     * 新增或更新字典项
     * @param dictModel 字典项
     */
    public void putDictItem(DictModel dictModel) {
        List<DictModel> dictModelList = getDictItems(dictModel.getCode());
        //对于已存在的字典项，先删除
        dictModelList.removeIf(t->t.getValue().equals(dictModel.getValue()));
        //新增字典项
        dictModelList.add(dictModel);
        putDict(dictModel.getCode(), dictModelList);
    }

    /**
     * 删除字典项
     * @param dictModel 字典项
     */
    public void removeDictItem(DictModel dictModel) {
        List<DictModel> dictModelList = getDictItems(dictModel.getCode());
        dictModelList.removeIf(t->t.getValue().equals(dictModel.getValue()));
        putDict(dictModel.getCode(), dictModelList);
    }

    /**
     * 插入字典，如果字典编号不存在
     * @param dictCode 字典编码
     * @param dictModelList 字典项list
     * @return
     */
    public boolean putDictIfAbsent(String dictCode, List<DictModel> dictModelList) {
        if (dictCode == null) {
            return false;
        }
        return redisTemplateUtil.opsForHash().putIfAbsent(DICT_KEY, dictCode, dictModelList);
    }

    /**
     * 删除字典
     * @param dictCode 字典编码
     * @return
     */
    public void removeDict(Object... dictCode) {
        if (dictCode == null) {
            return;
        }
        redisTemplateUtil.opsForHash().delete(DICT_KEY, dictCode);
    }

    /**
     * 获取字典项list。
     * @param dictCode 字典编码
     * @return 字典项列表，获取不到则返回EMPTY_LIST
     */
    public List<DictModel> getDictItems(String dictCode) {
        if (dictCode == null) {
            return Collections.emptyList();
        }
        List<DictModel> dictModelList = (List<DictModel>) redisTemplateUtil.getHashValue(DICT_KEY, dictCode);
        if(dictModelList == null){
            return Collections.emptyList();
        }
        return dictModelList;
    }

    /**
     * 根据字典编码和字典值，获取字典文本
     * @param dictCode
     * @param itemValue
     * @return
     */
    public DictModel getDictItemByValue(String dictCode, String itemValue) {
        List<DictModel> dictModelList = getDictItems(dictCode);
        //list转map，该方法要求itemValue是唯一的，大数据量下这个方法效率更高
//        Map<String, DictModel> dictModelMap = dictModelList.stream().collect(Collectors.toMap(DictModel::getValue, Function.identity()));
//        return dictModelMap.get(itemValue);
        //这个方法更加通俗易理解
        return dictModelList.stream().filter(s->s.getValue().equals(itemValue)).findFirst().orElse(null);
    }

    /**
     * 根据字典编码和字典文本，获取字典值
     * @param dictCode
     * @param itemText
     * @return
     */
    public DictModel getDictItemByName(String dictCode, String itemText) {

        List<DictModel> dictModelList = getDictItems(dictCode);
        //list转map，该方法要求itemText是唯一的，大数据量下这个方法效率更高
//        Map<String, DictModel> dictModelMap = dictModelList.stream().collect(Collectors.toMap(DictModel::getText, Function.identity()));
//        return dictModelMap.get(itemText);
        //这个方法更加通俗易理解
        return dictModelList.stream().filter(s->s.getText().equals(itemText)).findFirst().orElse(null);
    }

}
