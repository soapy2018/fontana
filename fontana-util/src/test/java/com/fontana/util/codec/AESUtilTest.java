package com.fontana.util.codec;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AESUtilTest {

    @Test
    public void test() {

        String encryptPasswrod = AESUtil.aesCBCEncrypt("Supos1304@", "EfoUm8Qda0BmCwfc");
        assertThat(encryptPasswrod).isEqualTo("Tu5FcaWt/eyWp9wDyXzByw==");

        String raw = AESUtil.aesCBCDecrypt("Tu5FcaWt/eyWp9wDyXzByw==", "EfoUm8Qda0BmCwfc");
        assertThat(raw).isEqualTo("Supos1304@");

    }


}
