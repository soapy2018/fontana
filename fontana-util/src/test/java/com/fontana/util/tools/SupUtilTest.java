package com.fontana.util.tools;


import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;


public class SupUtilTest {

    @Test
    @DisplayName("Should throws an exception when the object is null")
    public void testRequireNotNullWhenObjectIsNullThenThrowsException() {

        Assert.assertThrows(NullPointerException.class, () -> {
            SupUtil.requireNotNull(null);
        });
    }

    @Test
    @DisplayName("Should throws an exception when the object is null and message is not null")
    public void testRequireNotNullWhenObjectIsNullAndMessageIsNotNullThenThrowsException() {

        String message = "message";
        Assert.assertThrows(NullPointerException.class, () -> {
            SupUtil.requireNotNull(null, message);
        });
    }


}