package com.bluetron.nb.common.util.bean;

import com.bluetron.nb.common.util.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanUtilTest {

    private Person person = null;

    @Before
    public void init() {
        this.person = new Person("jack", 20);
    }

    @Test
    public void beanToMapObjectTest() {
        Map<String, Object> map = BeanUtil.toMap(person);
        assertThat(map.get("age")).isEqualTo(20);
        assertThat(map.get("age").getClass()).isEqualTo(Integer.class);
    }

    @Test
    public void beanToMapStringTest() {
        Map<String, String> map = BeanUtil.toMapString(person);
        assertThat(map.get("age")).isEqualTo("20");
        assertThat(map.get("age").getClass()).isEqualTo(String.class);
    }

    @Test
    public void copyProperties() {

        Person target = new Person();
        BeanUtil.copyProperties(person, target);

        assertThat(target.getAge()).isEqualTo(20);
        assertThat(target.getName()).isEqualTo("jack");

        Person target1 = new Person();

        BeanUtil.copyProperties(person, target1, "age");
        assertThat(target1.getAge()).isEqualTo(null); // not copy
        assertThat(target1.getName()).isEqualTo("jack");
    }

    @Test
    public void copyPropertiesNullTest() {

        SuposOrganizeVO vo = new SuposOrganizeVO();
        vo.setName("123");
        vo.setIsMainPosition(null);

        assertThat(vo.isMainPosition).isNull();

        Position target = new Position();
        BeanUtil.copyProperties(vo, target);

        assertThat(target.getName()).isEqualTo("123");
        assertThat(target.isMainPosition).isNull();

        Position target1 = new Position();
        BeanUtil.copyProperties(vo, target1, true);
        assertThat(target1.getName()).isEqualTo("123");
        assertThat(target1.isMainPosition).isFalse();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {
        private String name;
        private Integer age;

        public Person(int age) {
            this.age = age;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Position {

        private String name;
        private Long companyId;
        private Boolean isMainPosition;
        public Position() {
            this.isMainPosition = Boolean.FALSE;
        }
    }

    @Data
    @AllArgsConstructor
    public static class SuposOrganizeVO {

        private String fullPath;
        private Long companyId;
        private Boolean isMainPosition;
        private String code;
        private String showName;
        private String name;
        private String Id;
        private String depId;
        public SuposOrganizeVO() {

        }
    }


}
