package com.kevin.loopview.internal;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 版权所有：XXX有限公司</br>
 * 
 * JsonTool </br>
 * 
 * @author zhou.wenkai ,Created on 2014-9-2 15:09:40</br>
 * Major Function：<b>Gson 操作Json 工具类</b> </br>
 * 该工具类依托于Gosn开源库</br>
 * 
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class JsonTool {

	/**
	 * 将指定的 {@link java.util.HashMap}<String, Object>对象转成json数据
	 */
	public static String fromHashMap(HashMap<String, Object> map) {
		try {
			return getJSONObject(map).toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "";
	}

	/**
	 * 将制定 Map对象封装到 JSONObject
	 *
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject getJSONObject(HashMap<String, Object> map)
			throws JSONException {
		JSONObject json = new JSONObject();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>) value);
			} else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>) value);
			}
			json.put(entry.getKey(), value);
		}
		return json;
	}

	/**
	 * 将指定 ArrayList集合封装到JSONArray对象
	 *
	 * @param list
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private static JSONArray getJSONArray(ArrayList<Object> list)
			throws JSONException {
		JSONArray array = new JSONArray();
		for (Object value : list) {
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>) value);
			} else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>) value);
			}
			array.put(value);
		}
		return array;
	}

	public static HashMap<String, String> getMapFromJsonObject(JSONObject object) {
		HashMap<String, String> map = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = object.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			try {
				String value = object.getString(key);
				if ("null".equals(value)) {
					value = "";
				}
				map.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return map;
	}

	/**
	 * 将json数据封装到HashMap对象
	 *
	 * @param json
	 * @return
	 */
	public static HashMap<String, Object> parseJson(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			if (json != null) {
				JSONObject object = new JSONObject(json);
				Log.i("SQ", object.toString());
				Iterator<String> iterator = object.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					Object obj = object.get(key);
					if (obj instanceof String) {
						String value = obj.toString();
						if ("null".equals(value)) {
							value = "";
						}
						map.put(key, value);
					} else if (obj instanceof JSONObject) {
						HashMap<String, Object> map2 = parseJson(obj.toString());
						if (map2 != null) {
							map.put(key, map2);
						}
					} else if (obj instanceof Integer) {
						int value = Integer.parseInt(obj.toString());
						map.put(key, value);
					} else if (obj instanceof Long) {
						long value = Long.parseLong(obj.toString());
						map.put(key, value);
					} else if (obj instanceof Float) {
						float value = Float.parseFloat(obj.toString());
						map.put(key, value);
					} else if (obj instanceof Boolean) {
						boolean value = (Boolean) obj;
						map.put(key, value);
					} else if (obj instanceof JSONArray) {
						ArrayList<HashMap<String, Object>> mapList = getArrayList((JSONArray) obj);
						map.put(key, mapList);
					} else {
						map.put(key, "");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 将json封装为Array对象
	 *
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, Object>> getArrayList(
			JSONArray jsonArray) throws JSONException {
		ArrayList<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
		int m = jsonArray.length();
		if (m > 0) {
			for (int j = 0; j < m; j++) {
				Object object2 = jsonArray.get(j);
				if (object2 instanceof JSONObject) {
					JSONObject o = jsonArray.getJSONObject(j);
					mapList.add(parseJson(o.toString()));
				} else if (object2 instanceof String) {
					HashMap<String, Object> map2 = new HashMap<String, Object>();
					map2.put("picURL", object2.toString());
					mapList.add(map2);
				}
			}
		}
		return mapList;
	}

	/**
	 * 将json封装为HashMap对象
	 *
	 * @param json
	 * @return
	 */
	public static HashMap<String, String> parseJsonOnlyString(String json) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(json);
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = object.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = object.getString(key);
				map.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
