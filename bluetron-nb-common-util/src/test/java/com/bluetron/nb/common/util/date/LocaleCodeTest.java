package com.bluetron.nb.common.util.date;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class LocaleCodeTest {

    @Test
    public void test() {
        assertThat(LocaleCode.getDefaultENlocale().getCountry()).isEqualTo("US");
        assertThat(LocaleCode.getDefaultZHlocale().getCountry()).isEqualTo("CN");
        
        
        assertThat(LocaleCode.getDefaultLocale("abc").getCountry()).isEqualTo("CN");
        
        assertThat(LocaleCode.getDefaultLocale("zh").getCountry()).isEqualTo("CN");
        assertThat(LocaleCode.getDefaultLocale("en").getCountry()).isEqualTo("US");
        assertThat(LocaleCode.getDefaultLocale("EN").getCountry()).isEqualTo("CN"); //写死了
        
        assertThat(LocaleCode.getSuposAcceptLanguage(Locale.CHINESE)).isEqualTo("zh-cn");
        assertThat(LocaleCode.getSuposAcceptLanguage(Locale.ENGLISH)).isEqualTo("en-us");

        assertThat(LocaleCode.getDefaultZHlocale()).isEqualTo(Locale.CHINA);
        
    }

}
