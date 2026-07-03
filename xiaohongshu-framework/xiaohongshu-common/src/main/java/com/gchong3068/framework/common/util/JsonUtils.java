package com.gchong3068.framework.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author gc
 * @date 2024年05月05日 20:13
 */
public class JsonUtils {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 配置反序列化和序列化时的一些行为
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * 将对象转换成Json 字符串
     * @author gc
     * @date 2024/5/5 20:26
     * @param obj 父类
     * @return java.lang.String
     */
    @SneakyThrows
    public static String toJsonString(Object obj){
        return OBJECT_MAPPER.writeValueAsString(obj);
    }


    /**
     * 初始化,统一使用 Spring Boot 个性化配置的 ObjectMapper
     * @author gc
     * @date 2024/5/14 19:07
     * @param objectMapper ObjectMapper
     */
    public static void init(ObjectMapper objectMapper){
        OBJECT_MAPPER = objectMapper;
    }

    @SneakyThrows
    public static <T> T parseObject(String json, Class<T> clazz){
        if (StringUtils.isBlank(json)){
            return null;
        }
        return OBJECT_MAPPER.readValue(json,clazz);
    }


    /**
     * json转Map
     * @Auther: gchong3068
     * @Date: 2024/10/11 18:34
 	 * @param jsonStr json字符串
 	 * @param keyClass key类型
 	 * @param valueClass value类型
 	 * @return java.util.Map<K,V>
     **/
    public static <K,V>Map<K,V> parseMap(String jsonStr,Class<K> keyClass,Class<V> valueClass) throws Exception{

        //创建TypeReference
        TypeReference<Map<K,V>> typeRef = new TypeReference<Map<K, V>>() {
        };

        return OBJECT_MAPPER.readValue(jsonStr, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
    }

    /**
     * 将JSON 字符串解析为List
     * @Auther: gchong3068
     * @Date: 2024/11/2 17:14
     * @param jsonStr json字符串
     * @param clazz 转换的对象类型
     * @return java.util.List<T>
     **/
    public static <T>List<T> parseList(String jsonStr,Class<T> clazz) throws Exception{
        // 使用 TypeReference 指定 List<T> 的泛型类型
        return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<List<T>>() {
            @Override
            public Type getType() {
                return OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class,clazz);
            }
        });
    }


    /**
     * 将 JSON 字符串解析为指定类型的 Set 对象
     * @Auther: gchong3068
     * @Date: 2025/2/7 20:25
 	 * @param jsonStr 字符串
 	 * @param clazz 转换的对象类型
 	 * @return java.util.Set<T>
     **/
    public static <T> Set<T> parseSet(String jsonStr,Class<T> clazz) throws Exception{
        return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Set<T>>() {
            @Override
            public CollectionType getType() {
                return OBJECT_MAPPER.getTypeFactory().constructCollectionType(Set.class,clazz);
            }
        });
    }
}
