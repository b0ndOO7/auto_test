package com.zsdk.autotest.api.testcase;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.Reporter;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import com.zsdk.autotest.common.ExcelUtils;

//import com.zsdk.autotest.common.ExcelUtils;

public class TestCase {

	private Assertion assertion;

	public Map<String, HashMap> allTestCases;
	// 测试案例文件路径--excel作为存储
	public String testCasePath = "E:/workspace/auto_test/testcases.xls";

	@BeforeClass
	public void beforeClass() {
		try {
			allTestCases = ExcelUtils.toMapBySheet(testCasePath, "cases_a");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertion = new Assertion();
	}

	@AfterClass
	public void afterClass() {
	}
	
	@DataProvider(name = "case_a")
	public Object[][] getCaseData(){
		return ExcelUtils.getSheetData(testCasePath, "cases_a");
	}
	
	/** 用例格式
	 * CaseName	RequestMethod	RequestUrl	RequestData	ExpectResult	CaseDesc
	 *   登录	GET	           test.zbg.com	{"name":"aaaa"}	TRUE	    desc1
	 * @return
	 */
	@Test(dataProvider = "case_a")
	public void f1(HashMap<String, String> map){
		Reporter.log("testNG testcase f1()");
		for (Entry<String, String> entry : map.entrySet()) {
			Reporter.log(entry.getKey() + ":" + entry.getValue());
		}
		
		assertion.assertTrue(false);
	}

	@Test
	public void f2() {
		Reporter.log("testNG testcase f2()");
		assertion.assertTrue(true);
	}

	@Test
	public void f3() {
		Reporter.log("testNG testcase f3()");
		assertion.assertTrue(true);
	}

}
