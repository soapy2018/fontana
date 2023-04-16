package com.fontana.util.tools;

import org.junit.Test;

public class IdUtilTest {

    @Test
    public void testCreateRandomUUID() {
        String uuid = IdUtil.randomUUID();
        System.out.println(uuid);
    }

    @Test
    public void testCreateSimpleUUID() {
        String uuid = IdUtil.simpleRandomUUID();
        System.out.println(uuid);
    }

    @Test
    public void testNextLongId() {
        Long uuid = IdUtil.nextLongId();
        System.out.println(uuid);
        uuid = IdUtil.nextLongId();
        System.out.println(uuid);
    }

    @Test
    public void testNextStringId() {
        String uuid = IdUtil.nextStringId();
        System.out.println(uuid);
        uuid = IdUtil.nextStringId();
        System.out.println(uuid);
    }

}
