package com.bluetron.nb.common.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonMapperTest {
	
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
	@NoArgsConstructor
	public static class ComplexClass {
		
		private List<Person> persons;
		private String teacher;
	}
	
	@Test
	public void testParseNull() {
	    assertThat(JacksonMapper.fromJson("", Person.class)).isNull();
	}
	
	@Test
	public void testPersonToJson() {
		
		String personStr = "{\"age\":50,\"name\":\"jack\"}";
		
		Person person = 
				new Person("jack", 50);
		
		String jsonStr = 
				JacksonMapper.toJson(person); //按照字母顺序排列
		
		assertThat(jsonStr).isEqualTo(personStr);
		
		
		Person fromJsonPerson = 
				JacksonMapper.fromJson(personStr , Person.class);
		
		assertThat(fromJsonPerson.getAge()).isEqualTo(50);
		assertThat(fromJsonPerson.getName()).isEqualTo("jack");
	}
	
	@Test
	public void testPersonToJsonNull() {
		
		Person person = 
				new Person(50);
		
		String personStr = "{\"age\":50,\"name\":\"\"}";
		
		String jsonStr = 
				JacksonMapper.toJson(person);
		
		assertThat(jsonStr).isEqualTo(personStr);
	}
	
	@Test
	public void testJsonToPerson() {
		
		// 少字段测试
		String personStr = "{\"age\":50}";
		Person person = JacksonMapper.fromJson(personStr , Person.class);
		assertThat(JacksonMapper.toJson(person)).isEqualTo("{\"age\":50,\"name\":\"\"}");
		
		// 多字段测试
		String personStr1 = "{\"age\":50 , \"p\":1}";
		Person person1 = JacksonMapper.fromJson(personStr1 , Person.class);
		assertThat(JacksonMapper.toJson(person1)).isEqualTo("{\"age\":50,\"name\":\"\"}");
		
	}
	
	@Test
	public void testUpdate() {
		Person person = 
				new Person("jack" , 50);
		String personUpdateStr = "{\"age\":20}";
		Person personUpdated = JacksonMapper.getInstance().update(personUpdateStr , person);
		assertThat(personUpdated.getAge()).isEqualTo(20);
		assertThat(personUpdated.getName()).isEqualTo("jack");
	}
	
	
	@Test
	public void testJsonToComplexObject() {

		ComplexClass class1 = new ComplexClass();
		class1.setTeacher("allen");
		Person person1 = 
				new Person("jack" , 20);
		Person person2 = 
				new Person("bob" , 22);
		
		List<Person> persons = Lists.newArrayList();
		persons.add(person1);
		persons.add(person2);
		
		class1.setPersons(persons);
		
		// {"persons":[{"age":20,"name":"jack"},{"age":22,"name":"bob"}],"teacher":"allen"}
		assertThat(JacksonMapper.toJson(class1)).isEqualTo("{\"persons\":[{\"age\":20,\"name\":\"jack\"},{\"age\":22,\"name\":\"bob\"}],\"teacher\":\"allen\"}");
		String jsonStr = "{\"persons\":[{\"age\":20,\"name\":\"jack\"},{\"age\":22,\"name\":\"bob\"}],\"teacher\":\"allen\"}";
		ComplexClass class2 = JacksonMapper.fromJson(jsonStr, new TypeReference<ComplexClass>(){});
		
		assertThat(class2.getTeacher()).isEqualTo("allen");
		assertThat(class2.getPersons().get(0).getName()).isEqualTo("jack");
		assertThat(class2.getPersons().get(1).getName()).isEqualTo("bob");

	}
	
	
}
