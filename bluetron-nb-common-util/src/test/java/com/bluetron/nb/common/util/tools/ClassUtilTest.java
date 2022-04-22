package com.bluetron.nb.common.util.tools;

import com.bluetron.nb.common.util.tools.example.IntefacesA;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassUtilTest {

    @Test
    void getAllClassByInterface() {
        List<Class> classList = ClassUtil.getAllClassByInterface(IntefacesA.class);
        classList.forEach(System.out::println);
    }
}