package com.bluetron.nb.common.util.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bluetron.nb.common.base.constant.ICodeAndNameEnum;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取excel数据时 enum类型的转换器
 * @author genx
 * @date 2021/4/12 14:39
 */
public class ExcelEnumConverterFactory {

    private static ConcurrentHashMap<Class<? extends Enum>, Converter> CACHE = new ConcurrentHashMap();

    public static <T extends Enum> Converter<T> getConverter(Class targetType) {
        targetType = getEnumType(targetType);
        Converter converter = CACHE.get(targetType);
        if (converter == null) {
            converter = new StringToEnum(targetType);
            CACHE.putIfAbsent(targetType, converter);
        }
        return converter;
    }

    static class StringToEnum<T extends Enum> implements Converter<T> {

        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public Class supportJavaTypeKey() {
            return enumType;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public T convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            T data = null;
            String source = cellData.getStringValue().trim();
            try {
                data = (T) Enum.valueOf(enumType, source);
            } catch (Exception e) {

            }
            if (data == null && ICodeAndNameEnum.class.isAssignableFrom(enumType)) {
                T[] values = enumType.getEnumConstants();
                for (T value : values) {
                    if (source.equals(((ICodeAndNameEnum) value).getChineseName())) {
                        //尝试通过中文名来匹配 枚举
                        return value;
                    }
                }
            }
            return null;
        }

        @Override
        public CellData convertToExcelData(T value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            if (value != null) {
                if (ICodeAndNameEnum.class.isAssignableFrom(enumType)) {
                    return new CellData(((ICodeAndNameEnum) value).getChineseName());
                } else {
                    return new CellData(((Enum) value).name());
                }
            }
            return new CellData("");
        }
    }

    /**
     * copy from org.springframework.core.convert.support.ConversionUtils#getEnumType(Class)
     * @param targetType
     * @return
     */
    public static Class<? extends Enum> getEnumType(Class<? extends Enum> targetType) {
        Class enumType;
        for (enumType = targetType; enumType != null && !enumType.isEnum(); enumType = enumType.getSuperclass()) {
        }
        Assert.notNull(enumType, () -> {
            return "The target type " + targetType.getName() + " does not refer to an enum";
        });
        return enumType;
    }

}
