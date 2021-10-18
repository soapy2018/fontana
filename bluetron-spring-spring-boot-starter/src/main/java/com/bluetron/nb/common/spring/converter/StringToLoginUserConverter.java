package com.bluetron.nb.common.spring.converter;

import com.alibaba.fastjson.JSON;
import com.bluetron.nb.common.spring.dto.LoginUserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component
public class StringToLoginUserConverter implements Converter<String, LoginUserDTO> {

    public StringToLoginUserConverter(GenericConversionService conversionService) {
        conversionService.addConverter(this);
    }

    @Override
    public LoginUserDTO convert(String text) {
        try {
            return JSON.parseObject(URLDecoder.decode(text, "UTF-8"), LoginUserDTO.class);
        } catch (UnsupportedEncodingException e) {
            //throw new AutoPartsException("解析LoginUserDTO失败");
            throw null;
        }
    }

}