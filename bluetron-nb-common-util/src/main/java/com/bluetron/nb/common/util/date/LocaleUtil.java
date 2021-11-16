package com.bluetron.nb.common.util.date;

import java.util.Locale;

public class LocaleUtil {

    public static final String DEFAULT_EN = "en_US";

    public static final String DEFAULT_ZH = "zh_CN";

    public static final String EN = "en";

    public static final String ZH = "zh";

    public static final String SUPOS_EN_US = "en-us";

    public static final String SUPOS_ZH_CN = "zh-cn";

    public static Locale getDefaultLocale(Locale locale) {
        if (EN.equals(locale.getLanguage())) {
            return getDefaultENlocale();
        } else if (ZH.equals(locale.getLanguage())) {
            return getDefaultZHlocale();
        } else {
            return locale;
        }

    }

    public static Locale getDefaultLocale(String languageCode) {
        if (EN.equals(languageCode)) {
            return getDefaultENlocale();
        } else if (ZH.equals(languageCode)) {
            return getDefaultZHlocale();
        } else {
            return getDefaultZHlocale();
        }

    }

    public static Locale getDefaultENlocale() {
        return new Locale("en", "US");
    }

    public static Locale getDefaultZHlocale() {
        return new Locale("zh", "CN");
    }

    public static String getSuposAcceptLanguage(Locale locale) {
        if (EN.equals(locale.getLanguage())) {
            return SUPOS_EN_US;
        } else if (ZH.equals(locale.getLanguage())) {
            return SUPOS_ZH_CN;
        } else {
            return SUPOS_ZH_CN;
        }
    }

}
