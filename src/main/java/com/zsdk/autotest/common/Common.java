package com.zsdk.autotest.common;

import static org.testng.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Common {

	/**
	 * 字符串转map
	 * @param plain  输入字符串 eg  Userid:7f0pRahZRLc|Sign:4b13dd30592fa564ca24f7bd95a0d972
	 * @param eqaulsType  :
	 * @param spliceType  |
	 * @return
	 */
	public static Map<String, String> stringToMap(String plain, String eqaulsType, String spliceType) {
		if (plain == null || plain.isEmpty()) {
			return null;
		}
		Map<String, String> map = new HashMap<>();
		String[] split = null;
		if (plain.indexOf(spliceType) < 0) {
			String[] kvArr = plain.split(eqaulsType);
			if (kvArr.length == 2) {
				map.put(kvArr[0], kvArr[1]);
			} else {
				map.put(kvArr[0], "");
			}
		}else {
			split = plain.split(spliceType);
			
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
//		String regex = "[\\w]{0,}(?:\\.?[\\w]{1,})+[\\w-_/?&=#%:]*$";
//		boolean isUrl = url.matches(regex);
//		if ( ! isUrl) {
//			return null;
//		}
//		isUrl = url.matches("^(?:https?://)?");

		return url.startsWith("http://")?url:"http://"+url;
	}
	
	public static void main(String[] args) {
		System.out.println(checkAndChangeUrl("http://aaaa"));

	}
	
}
