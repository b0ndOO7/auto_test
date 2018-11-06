package com.zsdk.autotest.common;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

public class Common {

	/**
	 * 字符串转map
	 * 
	 * @param plain
	 *            输入字符串 eg
	 *            Userid:7f0pRahZRLc|Sign:4b13dd30592fa564ca24f7bd95a0d972
	 * @param eqaulsType
	 *            :
	 * @param spliceType
	 *            |
	 * @return
	 */
	public static Map<String, String> stringToMap(String plain, String eqaulsType, char spliceType) {
		if (plain == null || plain.isEmpty()) {
			return null;
		}
//		System.out.println(plain);
		Map<String, String> map = new HashMap<>();
		String[] split = null;
		if (plain.indexOf(spliceType)<0) {
			String[] kvArr = plain.split(eqaulsType);
			if (kvArr.length == 2) {
				map.put(kvArr[0], kvArr[1]);
			} else {
				map.put(kvArr[0], "");
			}
		} else {
			split = plain.split("\\" + spliceType);

			for (String kv : split) {
				if ("|".equals(kv)) {
					continue;
				}
				String[] kvArr = kv.split(eqaulsType);
				if (kvArr.length == 2) {
					map.put(kvArr[0], kvArr[1]);
				} else {
					map.put(kvArr[0], "");
				}
			}
		}

		return map;
	}

	public static String checkAndChangeUrl(String url) {
		// String regex = "[\\w]{0,}(?:\\.?[\\w]{1,})+[\\w-_/?&=#%:]*$";
		// boolean isUrl = url.matches(regex);
		// if ( ! isUrl) {
		// return null;
		// }
		// isUrl = url.matches("^(?:https?://)?");

		return url.startsWith("http://") ? url : "http://" + url;
	}
	
	
	public Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	public static String getStrTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		calendar.setTimeInMillis(millis);
		return format.format(calendar.getTime());
	}
	
	public static String getStrTime2(long millis) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
		calendar.setTimeInMillis(millis);
		return format.format(calendar.getTime());
	}
	
	
	public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> object2Map(Object obj) {
		Map<String, Object> map = new HashMap<>();
//		JSONObject json = JSONObject.fromObject(obj);
		map = (Map<String, Object>)JSONObject.fromObject(obj);
		return map;
	}
	

	public static void main(String[] args) {
		System.out.println(checkAndChangeUrl("http://aaaa"));

		Map<String, String> map = stringToMap("Userid:7f0pRahZRLc|Sign:4b13dd30592fa564ca24f7bd95a0d972", ":", '|');
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}

	}

}
