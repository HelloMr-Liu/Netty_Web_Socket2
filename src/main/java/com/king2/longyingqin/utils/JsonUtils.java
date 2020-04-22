package com.king2.longyingqin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/*================================================================
说明：操作json的工具类

作者          时间            注释
温为菁       2018.5.22	     创建
==================================================================*/
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**-----------------------------------------------------------------
     * 功能：将Object对象转换成json数据
     * 
     * 参数：data		Object		要转化的对象
     * 返回：String	json数据
     -------------------------------------------------------------------*/
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**-----------------------------------------------------------------
     * 功能：将json结果集转化为对象
     * 
     * 参数：jsonData		String		json数据
     *  	beanType 	Class<T>	要转化的bean类型
     * 返回：T
     -------------------------------------------------------------------*/
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**-----------------------------------------------------------------
     * 功能：将json数据转换成pojo对象list
     * 
     * 参数：jsonData		String		json数据
     * 		beanType	Class<T>	要转化的bean类型	
     * 返回: List<T>
     -------------------------------------------------------------------*/
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
}
