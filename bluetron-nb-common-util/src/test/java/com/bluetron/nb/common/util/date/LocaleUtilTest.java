package com.bluetron.nb.common.util.date;

import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class LocaleUtilTest {

    @Test
    public void test() {
        assertThat(LocaleUtil.getDefaultENlocale().getCountry()).isEqualTo("US");
        assertThat(LocaleUtil.getDefaultZHlocale().getCountry()).isEqualTo("CN");


        assertThat(LocaleUtil.getDefaultLocale("abc").getCountry()).isEqualTo("CN");

        assertThat(LocaleUtil.getDefaultLocale("zh").getCountry()).isEqualTo("CN");
        assertThat(LocaleUtil.getDefaultLocale("en").getCountry()).isEqualTo("US");
        assertThat(LocaleUtil.getDefaultLocale("EN").getCountry()).isEqualTo("CN"); //写死了

        assertThat(LocaleUtil.getSuposAcceptLanguage(Locale.CHINESE)).isEqualTo("zh-cn");
        assertThat(LocaleUtil.getSuposAcceptLanguage(Locale.ENGLISH)).isEqualTo("en-us");

        assertThat(LocaleUtil.getDefaultZHlocale()).isEqualTo(Locale.CHINA);

    }

}
