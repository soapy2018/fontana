package com.bluetron.nb.common.sb.converter;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/7/28 18:40
 */
@Component
public class ConverterUtil {

    private static GenericConversionService CONVERSION_SERVICE;

    public ConverterUtil(GenericConversionService conversionService) {
        CONVERSION_SERVICE = conversionService;
    }

    public static Object convert(Object value, Class targetType) {
        if (CONVERSION_SERVICE != null) {
            return CONVERSION_SERVICE.convert(value, targetType);
        } else {
            return value;
        }
    }

}
