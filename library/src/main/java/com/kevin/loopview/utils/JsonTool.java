package com.kevin.loopview.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 版权所有：XXX有限公司</br>
 * 
 * JsonTool </br>
 * 
 * @author zhou.wenkai ,Created on 2015-10-8 09:04:23</br> 
 * 		   Major Function：<b>JSON 操作 工具类</b> </br>
 * 
 *         注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class JsonTool<T> {
	
	private static boolean DEBUG = false;
	
	/**
	 * 将JSON字符串封装到对象
	 * 
	 * @param jsonStr 待封装的JSON字符串
	 * @param clazz 待封装的实例字节码
	 * @return T: 封装JSON数据的对象
	 * @version 1.0 
	 * @date 2015-10-9
	 * @Author zhou.wenkai
	 */
	public static <T> T toBean(String jsonStr, Class<T> clazz) {
		try {
			JSONObject job = new JSONObject(jsonStr);
			return parseObject(job, clazz, null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * JSONObject 封装到 对象实例
	 * 
	 * @param job 待封装的JSONObject
	 * @param c 待封装的实例对象class
	 * @param v	待封装实例的外部类实例对象</br>只有内部类存在,外部类时传递null
	 * @return T:封装数据的实例对象
	 * @version 1.0 
	 * @date 2015-10-9
	 * @Author zhou.wenkai
	 */
	@SuppressWarnings("unchecked")
	private static <T, V> T parseObject(JSONObject job, Class<T> c, V v) {
		T t = null;
		try {
			if(null == v) {
				t = c.newInstance();
			} else {
				Constructor<?> constructor = c.getDeclaredConstructors()[0];
				constructor.setAccessible(true);
				t = (T) constructor.newInstance(v);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e(JsonTool.class.getSimpleName(),
					c.toString() + " should provide a default constructor " +
							"(a public constructor with no arguments)");
		} catch (Exception e) {
			if(DEBUG)
				e.printStackTrace();
		}
		
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Class<?> type = field.getType();
			String name = field.getName();
			
			if(type.getName().equalsIgnoreCase("java.lang.String")) {
				try {
					String value = job.getString(name);
					if (value != null && value.equals("null")) {
						value = "";
					}
					field.set(t, value);
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
					try {
						field.set(t, "");
					} catch (Exception e1) {
						if(DEBUG)
							e1.printStackTrace();
					}
				}
			} else if(type.getName().equalsIgnoreCase("int")) {
				try {
					field.set(t, job.getInt(name));
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
				}
			} else if(type.getName().equalsIgnoreCase("boolean")) {
				try {
					field.set(t, job.getBoolean(name));
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
				}
			} else if(type.getName().equalsIgnoreCase("float")) {
				try {
					field.set(t, Float.valueOf(job.getString(name)));
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
				}
			} else if(type.getName().equalsIgnoreCase("double")) {
				try {
					field.set(t, job.getDouble(name));
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
				}
			} else if(type.getName().equalsIgnoreCase("java.util.List") ||
					type.getName().equalsIgnoreCase("java.util.ArrayList")){
				try {
					Object obj = job.get(name);
					Type genericType = field.getGenericType();
					String className = genericType.toString().replace("<", "")
							.replace(type.getName(), "").replace(">", "");
					Class<?> clazz = Class.forName(className);
					if(obj instanceof JSONArray) {
						ArrayList<?> objList = parseArray((JSONArray)obj, clazz, t);
						field.set(t, objList);
					}
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
				}
			} else {
				try {
					Object obj = job.get(name);
					String className = type.getName();
					Class<?> clazz = Class.forName(className);
					if(obj instanceof JSONObject) {
						Object parseJson = parseObject((JSONObject)obj, clazz, t);
						field.set(t, parseJson);
					}
				} catch (Exception e) {
					if(DEBUG)
						e.printStackTrace();
				}
				
			}
		}

		return t;
	}
	
	/**
	 * 将 JSONArray 封装到 ArrayList 对象
	 * 
	 * @param array 待封装的JSONArray
	 * @param c 待封装实体字节码
	 * @param v 待封装实例的外部类实例对象</br>只有内部类存在,外部类时传递null
	 * @return ArrayList<T>: 封装后的实体集合
	 * @version 1.0 
	 * @date 2015-10-8
	 */
	@SuppressWarnings("unchecked")
	private static <T, V> ArrayList<T> parseArray(JSONArray array, Class<T> c, V v) {
		ArrayList<T> list = new ArrayList<T>(array.length());
		try {
			for (int i = 0; i < array.length(); i++) {
				if(array.get(i) instanceof JSONObject) {
					T t = parseObject(array.getJSONObject(i), c, v);
					list.add(t);
				} else {
					list.add((T) array.get(i));
				}
				
			}
		} catch (Exception e) {
			if(DEBUG)
				e.printStackTrace();
		}
		return list;
	}

}
