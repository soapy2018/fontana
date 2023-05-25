package com.fontana.util.codec;

import com.fontana.base.constant.CommonConstants;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class RsaUtilTest {

    @Test
    public void test() throws Exception {
        String encrypt = RsaUtil.encrypt("123456",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRN9TqD1KQSIPMWpFuz26tgVqJO23e4TyMcKPC3uzvpzmSAraDjpOFwX5jKGXaHGiLsHElKZWbh4e3yzvRWLFYhmmS3W5ls2nCwvmfWZ+rPR+13n9hSgvUCvavPdIkHOEMMyoHe6O43PXffh5d9FpEmnVB8GeomijnEdLe2aelwQIDAQAB");

        System.out.println(encrypt);
        String password = "$2a$10$BGsgE5KA/hKV95.kCZmMleiUssNRpOGE0qMTkCGHsSiugnqSsaSFS";
        password = RsaUtil.decrypt("Cda81EughzRSrUCUpSl5dYz40VotiQtGpefR6kG4PWyWdlfIuCWE2FNg7quF650VqsZQ0dEG5t6O+H+Wn6mlr3X2HyeDWdP3s4L9pL988tfa9sxf7xJ5Ma8FnWYn2xG0hHarR9DHRMzBljJOoA6Qw0n8kTuJBRC02+RS5o8sPp4=", CommonConstants.PRIVATE_KEY);
        System.out.println(password);
    }

}
