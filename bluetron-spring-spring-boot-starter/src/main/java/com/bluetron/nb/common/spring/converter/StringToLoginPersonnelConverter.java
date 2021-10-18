package com.bluetron.nb.common.spring.converter;

import com.alibaba.fastjson.JSON;
import com.bluetron.nb.common.spring.dto.LoginPersonnelDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

@Component
public class StringToLoginPersonnelConverter implements Converter<String, LoginPersonnelDTO> {

    public StringToLoginPersonnelConverter(GenericConversionService conversionService) {
        conversionService.addConverter(this);
    }

    @Override
    public LoginPersonnelDTO convert(String text) {
        return JSON.parseObject(text, LoginPersonnelDTO.class);
    }

}