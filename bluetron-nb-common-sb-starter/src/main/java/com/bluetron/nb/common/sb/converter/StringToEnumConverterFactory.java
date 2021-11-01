package com.bluetron.nb.common.sb.converter;

import com.bluetron.nb.common.base.constant.ICodeAndNameEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 枚举类 请求参数解析 尝试通过中文名匹配
 * @author genx
 * @date 2021/4/12 13:56
 */
@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    public StringToEnumConverterFactory(GenericConversionService conversionService) {
        conversionService.addConverterFactory(this);
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum(getEnumType(targetType));
    }

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if(StringUtils.isBlank(source)){
                return null;
            }
            source = source.trim();
            T data = null;
            try {
                data = (T) Enum.valueOf(enumType, source);
            }catch ( Exception e){

            }
            if(data == null && ICodeAndNameEnum.class.isAssignableFrom(enumType)){
                T[] values = enumType.getEnumConstants();
                for (T value : values) {
                    if(source.equals(((ICodeAndNameEnum)value).getChineseName())){
                        //尝试通过中文名来匹配 枚举
                        return value;
                    }
                }
            }
            return data;
        }
    }

    /**
     * copy from org.springframework.core.convert.support.ConversionUtils#getEnumType(Class)
     * @param targetType
     * @return
     */
    public static Class<?> getEnumType(Class<?> targetType) {
        Class enumType;
        for (enumType = targetType; enumType != null && !enumType.isEnum(); enumType = enumType.getSuperclass()) {}
        Assert.notNull(enumType, () -> {
            return "The target type " + targetType.getName() + " does not refer to an enum";
        });
        return enumType;
    }
}
