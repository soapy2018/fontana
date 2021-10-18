package com.bluetron.nb.common.spring.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author genx
 * @date 2021/4/16 17:02
 */
@Component
public class BigDecimalToStringConverter implements Converter<BigDecimal, String> {

    public BigDecimalToStringConverter(GenericConversionService conversionService) {
        if (conversionService != null) {
            conversionService.addConverter(this);
        }
    }

    @Override
    public String convert(BigDecimal num) {
        return num != null ? num.stripTrailingZeros().toPlainString() : "";
    }
}
