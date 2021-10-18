package com.bluetron.nb.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 泛型工具类
 * @author genx
 * @date 2021/6/24 18:31
 */
public class GenericTypeKitUtils {

    private static Map<Class, Map<Class, Class[]>> CACHE = new ConcurrentHashMap();

    /**
     * 获取泛型 针对当前实例 具体的类
     * @param instanceType 实例的类
     * @param genericBeanType 泛型所在的类
     * @param index 泛型下标
     * @return
     */
    public static Class getGenericType(Class instanceType, Class genericBeanType, int index) {
        if (instanceType == null || genericBeanType == null || index < 0) {
            return Object.class;
        }
        Map<Class, Class[]> map = CACHE.get(instanceType);
        if (map == null) {
            map = readInstanceType(instanceType);
            CACHE.putIfAbsent(instanceType, map);
        }
        Class[] types = map.get(genericBeanType);
        if (types.length > index && types[index] != null) {
            return types[index];
        }
        return Object.class;
    }

    private static Map<Class, Class[]> readInstanceType(Class instanceType) {
        Map<Class, Class[]> map = new HashMap(16);
        readGeneric(instanceType, new Type[0], map);
        return map;
    }

    /**
     *
     * @param cls 当前的类
     * @param actualTypeArguments 从前面传过来的具体类型
     * @param map 当前 实例对象的map
     */
    private static void readGeneric(Class cls, Type[] actualTypeArguments, Map<Class, Class[]> map) {
        if (cls == Object.class) {
            return;
        }
        //当前类的 泛型定义 与 具体类型 的对应关系
        Map<String, Type> typeVariableMap = new HashMap(8);
        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
            map.put(cls, readActualTypeArguments(actualTypeArguments));
            TypeVariable[] typeVariables = cls.getTypeParameters();
            for (int i = 0; i < actualTypeArguments.length & i < typeVariables.length; i++) {
                typeVariableMap.put(typeVariables[i].getName(), actualTypeArguments[i]);
            }
        }
        //解析父类
        readGeneric(cls.getGenericSuperclass(), typeVariableMap, map);
        //解析接口
        for (Type genType : cls.getGenericInterfaces()) {
            readGeneric(genType, typeVariableMap, map);
        }
    }

    /**
     * 将 Type 解析为 具体的类
     * @param actualTypeArguments
     * @return
     */
    private static Class[] readActualTypeArguments(Type[] actualTypeArguments) {
        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
            Class[] data = new Class[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type type = actualTypeArguments[i];
                if (type instanceof Class) {
                    data[i] = (Class) type;
                } else if (type instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) type).getRawType();
                    if (rawType instanceof Class) {
                        data[i] = (Class) rawType;
                    }
                }
            }
            return data;
        }
        return new Class[9];
    }

    private static void readGeneric(Type genType, Map<String, Type> typeVariableMap, Map<Class, Class[]> map) {
        if (genType == null) {
            return;
        }
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof TypeVariable) {
                    Type type = typeVariableMap.get(((TypeVariable<?>) params[i]).getName());
                    if (type != null) {
                        params[i] = type;
                    }
                }
            }
            if (((ParameterizedType) genType).getRawType() instanceof Class) {
                readGeneric((Class) ((ParameterizedType) genType).getRawType(), params, map);
            }
        } else if (genType instanceof Class) {
            readGeneric((Class) genType, new Type[0], map);
        }
    }
}
