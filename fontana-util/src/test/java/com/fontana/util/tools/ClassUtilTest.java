package com.fontana.util.tools;

import com.fontana.util.tools.example.IntefacesA;
import org.junit.jupiter.api.Test;

import java.util.List;

class ClassUtilTest {

    @Test
    void getAllClassByInterface() {
        List<Class> classList = ClassUtil.getAllClassByInterface(IntefacesA.class);
        classList.forEach(System.out::println);
    }
}