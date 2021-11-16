package com.bluetron.nb.common.util.tools;

import org.junit.Test;

public class IdUtilTest {

    @Test
    public void testCreateRandomUUID() {
        String uuid = IdUtil.randomUUID();
        System.out.println(uuid);
    }

    @Test
    public void testCreateSimpleUUID() {
        String uuid = IdUtil.simpleUUID();
        System.out.println(uuid);
    }

}
