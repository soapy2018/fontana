package com.fontana.util.tools;

import com.fontana.base.constant.StringPool;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/17 16:27
 */
public class YamlUtil {
  // ${} 占位符 正则表达式
  private static Pattern p1 = Pattern.compile("\\$\\{.*?\\}");

  private YamlUtil(){
    throw new AssertionError();
  }

  /**
   * key:文件索引名
   * value：配置文件内容
   */
  private static Map<String , LinkedHashMap> ymls = new HashMap<>();
  /**
   * String:当前线程需要查询的文件名
   */
  private static ThreadLocal<String> nowFileName = new InheritableThreadLocal<>();

  private static ThreadLocal<String> profileLocal = new InheritableThreadLocal<>();

  /**
   * 主动设置，初始化当前线程的环境
   * @param profile
   */
  public static void setProfile(String profile) {
    profileLocal.set(profile);
  }

  /**
   * 加载配置文件
   * @param fileName
   */
  private static void loadYml(String fileName){
    nowFileName.set(fileName);
    if (!ymls.containsKey(fileName)){
      ymls.put(fileName , new Yaml().loadAs(YamlUtil.class.getResourceAsStream(StringPool.SLASH + fileName),
          LinkedHashMap.class));
    }
  }

  /**
   * 读取yml文件中的某个value。
   * 支持解析 yml文件中的 ${} 占位符
   * @param key
   * @return Object
   */
  private static Object getValue(String key){
    String[] keys = key.split("[.]");
    Map ymlInfo = (Map) ymls.get(nowFileName.get()).clone();
    for (int i = 0; i < keys.length; i++) {
      Object value = ymlInfo.get(keys[i]);
      if (i < keys.length - 1){
        ymlInfo = (Map) value;
      }else if (value == null){
        throw new RuntimeException("key不存在");
      }else {
        String g;
        String keyChild;
        String v1 = value != null ? value.toString() : "";
        for (Matcher m = p1.matcher(v1); m.find();
            value = v1.replace(g, (String) getValue(keyChild))) {
          g = m.group();
          keyChild = g.replaceAll("\\$\\{", "").replaceAll("\\}", "");
        }
        return value != null ? value.toString() : "";
      }
    }
    return "";
  }

  /**
   * 读取yml文件中的某个value
   * @param fileName  yml名称
   * @param key
   * @return Object
   */
  public static Object getValue(String fileName , String key){
    loadYml(fileName);
    return getValue(key);
  }

  /**
   * 框架私有方法，非通用。
   * 获取 spring.profiles.active的值: test/prod 测试环境/生成环境
   * @return
   */
  public static String getProfiles(){
    if (profileLocal.get() == null) {
      String value = (String) getValue("application.yml", "spring.profiles.active");
      setProfile(value);
    }
    return profileLocal.get();
  }

  /**
   * 读取yml文件中的某个value，返回String
   * @param fileName
   * @param key
   * @return String
   */
  public static String getValueToString(String fileName , String key){
    return (String)getValue(fileName , key);
  }

  /**
   *  获取 application.yml 的配置
   * @param key
   * @return
   */
  public static String getValueToString(String key){
    return (String)getValue("application.yml" , key);
  }

  /**
   *  获取 application-test/prod.yml 的配置
   * @param key
   * @return
   */
  public static String getProfileValueToString(String key){
    String fileName = "application-" + getProfiles() + ".yml";
    return (String)getValue(fileName , key);
  }
}
