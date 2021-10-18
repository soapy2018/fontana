package com.bluetron.nb.common.util.codec;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AESUtilsTest {

    @Test
    public void test() {
        
        String encryptPasswrod = AESUtils.aesCBCEncrypt("Supos1304@" , "EfoUm8Qda0BmCwfc");
        assertThat(encryptPasswrod).isEqualTo("Tu5FcaWt/eyWp9wDyXzByw==");
        
        String raw = AESUtils.aesCBCDecrypt("Tu5FcaWt/eyWp9wDyXzByw==", "EfoUm8Qda0BmCwfc");
        assertThat(raw).isEqualTo("Supos1304@");
       
    }
    

}
