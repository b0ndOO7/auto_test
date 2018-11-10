package com.zsdk.autotest.api.testcase;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.testng.Reporter;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zsdk.autotest.common.Common;
import com.zsdk.autotest.common.ExcelUtils;
import com.zsdk.autotest.common.HttpClientUtil;

public class TestCase {

	public Map<String, HashMap> allTestCases;
	// 测试案例文件路径--excel作为存储
	// public String testCasePath = "E:/workspace/auto_test/testcases.xls";
	public String testCasePath = "testcases.xls";

	@BeforeClass
	public void beforeClass() {
		// try {
		// allTestCases = ExcelUtils.toMapBySheet(testCasePath, "cases_a");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	@AfterClass
	public void afterClass() {
	}

	@Test(dataProvider = "case_a")
	public void testApi(Map<String, Object> map) throws ClientProtocolException, IOException, URISyntaxException {
		// CaseName RequestMethod RequestUrl RequestHead RequestData
		// ExpectResult CaseDesc
		String caseName = (String) map.get("CaseName");
		Reporter.log("【用例名称】：" + map.get("CaseName") + "===========================================");

		// 用例步骤
		Map<String, String>[] maps = (Map<String, String>[]) map.get("Steps");
		for (int i = 0; i < maps.length; i++) {
			// Step RequestMethod RequestUrl RequestHead RequestData
			// ResultComparisonType ExpectResult CaseDesc
			String step = maps[i].get("Step");
			Map<String, String> stepMap = new HashMap<>();
			Reporter.log("用例:" + caseName + "  Step:" + step + " 开始执行......");
			Reporter.log("【用例参数】：" + stepMap);
			
			Map<String, String> respMap = null;
			if (i == 0) {
				respMap = doHttpRequest(stepMap);
			}else {
				respMap = doHttpRequest(stepMap);
			}
			
			Reporter.log("用例:" + caseName + "  Step:" + step + " 执行完成");
			
			Reporter.log("【响应结果】：" + map.get("CaseName") + "===========================================");
			Reporter.log("respMap" + respMap);

//			Reporter.log("【结果校验】：校验类型ResultComparisonType:" + resultComparisonType);
//			Reporter.log("【结果校验】：expect:" + expect);

//			compareResult(resultComparisonType, expect, respStr);

		}

		Reporter.log("【用例】：" + map.get("CaseName") + "执行完成！！！");

	}

	@DataProvider(name = "case_a")
	public Object[][] getCaseData() {
		return ExcelUtils.getSheetData(testCasePath, "cases_a");
	}

	/**
	 * 用例格式 CaseName RequestMethod RequestUrl RequestData ExpectResult CaseDesc
	 * 登录 GET test.zbg.com {"name":"aaaa"} TRUE desc1
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 */
	@Test(dataProvider = "case_a")
	public void testZBGApi(HashMap<String, String> map)
			throws ClientProtocolException, IOException, URISyntaxException {
		// CaseName RequestMethod RequestUrl RequestHead RequestData
		// ExpectResult CaseDesc
		Reporter.log("【用例名称】：" + map.get("CaseName") + "===========================================");
		Reporter.log("【用例参数】：");
		for (Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey() == null || entry.getKey().isEmpty()) {
				continue;
			}
			Reporter.log(entry.getKey() + ":" + entry.getValue());
		}

		String url = map.get("RequestUrl");
		url = Common.checkAndChangeUrl(url);
		String method = map.get("RequestMethod");
		String header = map.get("RequestHead");
		String requestData = map.get("RequestData");

		String resultComparisonType = map.get("ResultComparisonType");
		String expect = map.get("ExpectResult");

		Map<String, String> headMap = new HashMap<>();
		String respStr = "";
		if ("POST".equals(method)) {
			if (header == null || header.isEmpty()) {
				respStr = HttpClientUtil.doPostRequst(url, requestData);
			} else {
				headMap = Common.stringToMap(header, ":", '|');
				respStr = HttpClientUtil.doPostRequst(url, requestData, headMap);
			}
		} else if ("GET".equals(method)) {
			if (header == null || header.isEmpty()) {
				respStr = HttpClientUtil.doGetRequest(url);
			} else {
				headMap = Common.stringToMap(header, ":", '|');
				respStr = HttpClientUtil.doGetRequest(url, headMap);
			}
		}
		Reporter.log("【响应结果】：" + map.get("CaseName") + "===========================================");
		Reporter.log(respStr);

		Reporter.log("【结果校验】：校验类型ResultComparisonType:" + resultComparisonType);
		Reporter.log("【结果校验】：expect:" + expect);

		compareResult(resultComparisonType, expect, respStr);
	}

	/**
	 * 响应结果与预期对比
	 * 
	 * @param resultComparisonType
	 *            对比类型 ：contain,equal,>,<
	 * @param expect
	 *            期望值
	 * @param respStr
	 *            响应结果
	 */
	private void compareResult(String resultComparisonType, String expect, String respStr) {
		switch (resultComparisonType) {
		case "contain":
			if (respStr.indexOf(expect) < 0) {
				assertTrue(false);
				Reporter.log("【结果校验】：fail");
			} else {
				assertTrue(true);
				Reporter.log("【结果校验】：pass");
			}
			break;

		case "equal":
			if (expect.equals(respStr)) {
				assertTrue(true);
				Reporter.log("【结果校验】：pass");
			} else {
				assertTrue(false);
				Reporter.log("【结果校验】：fail");
			}
			break;

		case ">":
			if (respStr.compareTo(expect) > 0) {
				assertTrue(true);
				Reporter.log("【结果校验】：pass");
			} else {
				assertTrue(true);
				Reporter.log("【结果校验】：pass");
			}
			break;

		case "<":
			if (respStr.compareTo(expect) < 0) {
				assertTrue(true);
				Reporter.log("【结果校验】：pass");
			} else {
				assertTrue(true);
				Reporter.log("【结果校验】：pass");
			}
			break;

		default:
			break;
		}
	}

	private static Map<String, String> doHttpRequest(Map<String, String> map)
			throws ClientProtocolException, IOException, URISyntaxException {

		String url = map.get("RequestUrl");
		url = Common.checkAndChangeUrl(url);
		String method = map.get("RequestMethod");
		String header = map.get("RequestHead");
		String requestData = map.get("RequestData");

		String resultComparisonType = map.get("ResultComparisonType");
		String expect = map.get("ExpectResult");

		Map<String, String> headMap = new HashMap<>();
		String respStr = "";
		if ("POST".equals(method)) {
			if (header == null || header.isEmpty()) {
				respStr = HttpClientUtil.doPostRequst(url, requestData);
			} else {
				headMap = Common.stringToMap(header, ":", '|');
				respStr = HttpClientUtil.doPostRequst(url, requestData, headMap);
			}
		} else if ("GET".equals(method)) {
			if (header == null || header.isEmpty()) {
				respStr = HttpClientUtil.doGetRequest(url);
			} else {
				headMap = Common.stringToMap(header, ":", '|');
				respStr = HttpClientUtil.doGetRequest(url, headMap);
			}
		}
		//  response to map 
		//  to-do 
		
		
		return null;
	}

	@Test(parameters = "param")
	public String f3(String param) {

		if (!param.equals("aaaaa")) {
			Reporter.log("f3() again");
			f3(param + "a");
		}

		Reporter.log("testNG testcase f3()");
		assertTrue(true);

		return "ok";
	}

}
