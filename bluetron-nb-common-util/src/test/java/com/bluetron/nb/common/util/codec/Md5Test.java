package com.bluetron.nb.common.util.codec;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Md5Test {

    @Test
    public void testMd5() {
        assertThat(Md5Util.md5("12345")).isEqualTo("827ccb0eea8a706c4c34a16891f84e7b");
    }

    @Test
    public void testFileMd5() throws Exception {

        assertThat(Md5Util.md5(
                new FileInputStream(new File("src/test/resources/google.png"))))
                .isEqualTo("80fa4bcab0351fdccb69c66fb55dcd00");
    }


}
