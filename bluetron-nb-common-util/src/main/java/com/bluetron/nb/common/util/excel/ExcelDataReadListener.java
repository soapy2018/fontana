package com.bluetron.nb.common.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * excel数据读取
 * @author genx
 * @date 2021/4/16 13:30
 */
public class ExcelDataReadListener<T> extends AnalysisEventListener<T> {

    private final Class<? extends T> beanType;
    private final Map<String, String> labelToFieldCodeMap;
    private final ExcelDataVisitor excelDataVisitor;

    public ExcelDataReadListener(Class<? extends T> beanType, Map<String, String> labelToFieldCodeMap, ExcelDataVisitor<T> excelDataVisitor) {
        this.beanType = beanType;
        this.labelToFieldCodeMap = labelToFieldCodeMap;
        this.excelDataVisitor = excelDataVisitor;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //根据labelToFieldCodeMap 重新将 中文名与英文名对应上
        Map<Integer, Head> heads = new TreeMap();

        Map<String, Integer> fieldCodeIndexMap = new HashMap(32);
        for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
            excelDataVisitor.addHeader(entry.getValue());
            String code = this.labelToFieldCodeMap.get(entry.getValue());
            if (code != null) {
                //force 强制模式
                heads.put(entry.getKey(), new Head(entry.getKey(), code, null, true, true));
                fieldCodeIndexMap.put(code, entry.getKey());
            }
        }
        context.currentReadHolder().excelReadHeadProperty().setHeadMap(heads);

        Map<Integer, ExcelContentProperty> contentPropertyMap = new TreeMap();
        for (ExcelContentProperty value : context.currentReadHolder().excelReadHeadProperty().getContentPropertyMap().values()) {
            Integer index = fieldCodeIndexMap.get(value.getHead().getFieldName());
            if (index != null) {
                contentPropertyMap.put(index, value);
                //枚举类解析器
                if (value.getField().getType().isEnum()) {
                    value.setConverter(ExcelEnumConverterFactory.getConverter(value.getField().getType()));
                }
            }
        }
        context.currentReadHolder().excelReadHeadProperty().setContentPropertyMap(contentPropertyMap);

    }

    /**
     * 这个每一条数据解析都会来调用
     * @param o
     * @param analysisContext
     */
    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        excelDataVisitor.addData(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}