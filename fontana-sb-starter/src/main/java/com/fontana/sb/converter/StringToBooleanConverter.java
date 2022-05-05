package com.fontana.sb.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 *
 * @author genx
 * @date 2021/4/30 8:35
 */
@Component
public class StringToBooleanConverter implements Converter<String, Boolean> {

    private static final Set<String> TRUE_VALUES = new HashSet(8);
    private static final Set<String> FALSE_VALUES = new HashSet(8);

    static {
        TRUE_VALUES.add("true");
        TRUE_VALUES.add("on");
        TRUE_VALUES.add("yes");
        TRUE_VALUES.add("1");
        TRUE_VALUES.add("是");

        FALSE_VALUES.add("false");
        FALSE_VALUES.add("off");
        FALSE_VALUES.add("no");
        FALSE_VALUES.add("0");
        FALSE_VALUES.add("否");
    }

    public StringToBooleanConverter(GenericConversionService conversionService) {
        if (conversionService != null) {
            conversionService.addConverter(this);
        }
    }


    @Override
    public Boolean convert(String source) {
        String value = source.trim();
        if (value.isEmpty()) {
            return null;
        } else {
            value = value.toLowerCase();
            if (TRUE_VALUES.contains(value)) {
                return Boolean.TRUE;
            } else if (FALSE_VALUES.contains(value)) {
                return Boolean.FALSE;
            } else {
                throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
            }
        }
    }

}
