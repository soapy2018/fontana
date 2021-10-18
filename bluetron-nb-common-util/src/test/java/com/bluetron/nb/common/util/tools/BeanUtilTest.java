package com.bluetron.nb.common.util.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanUtilTest {

    private Person person = null;

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

    @Before
    public void init() {
        this.person = new Person("jack", 20);
    }

    @Test
    public void beanToMapObjectTest() {
        Map<String, Object> map = BeanUtil.beanToMapObject(person);
        assertThat(map.get("age")).isEqualTo(20);
        assertThat(map.get("age").getClass()).isEqualTo(Integer.class);
    }

    @Test
    public void beanToMapStringTest() {
        Map<String, String> map = BeanUtil.beanToMapString(person);
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
    
    @Data
    @AllArgsConstructor
    public static class Position {
        
        public Position() {
            this.isMainPosition = Boolean.FALSE;
        }
        
        private String name;
        private Long companyId;
        private Boolean isMainPosition;    
    }
    
    @Data
    @AllArgsConstructor
    public static class SuposOrganizeVO {
        
        public SuposOrganizeVO() {
            
        }
        
        private String fullPath;
        private Long companyId;
        private Boolean isMainPosition;
        private String code;
        private String showName;
        private String name;
        private String Id;
        private String depId;
    }
    
    @Test
    public void copyPropertiesNullTest() {

        SuposOrganizeVO vo = new SuposOrganizeVO();
        vo.setName("123");
        vo.setIsMainPosition(null);
        
        assertThat(vo.isMainPosition).isNull();
        
        Position target = new Position();
        BeanUtil.copyProperties(vo , target);
        
        assertThat(target.getName()).isEqualTo("123");
        assertThat(target.isMainPosition).isNull();
        
        Position target1 = new Position();
        BeanUtil.copyProperties(vo , target1 , true);
        assertThat(target1.getName()).isEqualTo("123");
        assertThat(target1.isMainPosition).isFalse();
    }


}
