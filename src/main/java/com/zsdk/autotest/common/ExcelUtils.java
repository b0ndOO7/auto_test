package com.zsdk.autotest.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.control.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtils {

	public Object[][] ExcelUtils(String fPath, String sheetName) {
		return getSheetData(fPath, sheetName);
	}

	public static File getExcelFileObj(String fPath) {
		try {
			File fExl = new File(fPath);
			return fExl;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static Map<String, HashMap> toMapBySheet(String fPath, String sheetName) {
		try {
			File fExcel = getExcelFileObj(fPath);
			Workbook book = Workbook.getWorkbook(fExcel);
			Sheet sht = book.getSheet(sheetName);
			int rowCount = sht.getRows();
			int colCount = sht.getColumns();

			Cell cel = null;
			Map<String, HashMap> excelContents = new HashMap();
			if (rowCount < 2) {
				System.out.println("no case");
				return null;
			}

			// 检查是否存在空行
			String rowContents = "";
			for (int i = 2; i < rowCount; i++) {
				if (sht.getCell(0, i).getContents().toString().length() == 0) {
					System.out.println("测试案例文件中存在空行");
					return null;
				} else {
					for (int j = 0; j < colCount; j++) {
						rowContents = rowContents + sht.getCell(j, i).getContents().toString();
					}
					if (rowContents.length() < 20) {
						System.out.println("测试案例文件中存在空行");
						return null;
					}
				}
			}

			// 开始读取内容
			for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
				HashMap<String, String> rowMap = new HashMap();
				String testCaseCode = sht.getCell(0, rowIndex).getContents().toString();
				for (int colIndex = 1; colIndex < colCount; colIndex++) {
					rowMap.put(sht.getCell(colIndex, 0).getContents().toString(),
							sht.getCell(colIndex, rowIndex).getContents().toString());
				}
				excelContents.put(testCaseCode, rowMap);
			}
			book.close();

			return excelContents;
		} catch (Exception e) {
			System.out.println("发生异常：" + e);
		}
		return null;
	}

	/**
	 * 从excel中获取用例--无测试步骤
	 * @param fPath  excel路径
	 * @param sheetName sheet name
	 * @return Map<String, Object>[][]
	 */
	public static Object[][] getSheetData(String fPath, String sheetName) {
		File file = new File(fPath);
		Workbook book = null;
		Sheet sheet = null;
		try {
			book = Workbook.getWorkbook(file);
			sheet = book.getSheet(sheetName);
		} catch (Exception e) {
			System.out.println(fPath + ".xls文件读取失败");
		}
		int rows = sheet.getRows();
		int cols = sheet.getColumns();
		// 如果行数少于2，则没有必要再继续运行下去了，不会返回数据，直接退出程序
		if (rows <= 1) {
			System.out.println(fPath + ".xls的" + sheetName + " sheet没有数据");
			System.exit(0);
		}
		// 为了返回值是Object[][]类型，定义一个多行单列的二维数组
		HashMap<String, String>[][] arrMap = new HashMap[rows-1][1];
		for (int i = 0; i < rows - 1; i++) {
			arrMap[i][0] = new HashMap<>();
		}
		// 存储第一行的标题，作为key用
		List<String> list = new ArrayList<String>();
		for (int c = 0; c < cols; c++) {
			list.add(sheet.getCell(c, 0).getContents());
		}
		// 以键值对的形式，存进一个哈希表里
		for (int r = 1; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				arrMap[r - 1][0].put(list.get(c), sheet.getCell(c, r).getContents());
			}
		}
		book.close();
		return arrMap;
	}
	
	/**
	 * 从excel中获取用例--有测试步骤
	 * @param fPath  excel路径
	 * @param sheetName sheet name
	 * @return Map<String, Object>[][]
	 */
	public static Object[][] getSheetData2(String fPath, String sheetName) {
		File file = new File(fPath);
		Workbook book = null;
		Sheet sheet = null;
		try {
			book = Workbook.getWorkbook(file);
			sheet = book.getSheet(sheetName);
		} catch (Exception e) {
			System.out.println(fPath + ".xls文件读取失败");
		}
		int rows = sheet.getRows();
		int cols = sheet.getColumns();
		
		// 如果行数少于2，则没有必要再继续运行下去了，不会返回数据，直接退出程序
		if (rows <= 1) {
			System.out.println(fPath + ".xls的" + sheetName + " sheet没有数据");
			System.exit(0);
		}
		
		// 存储第一行的标题，作为key用
		List<String> headList = new ArrayList<String>();
		for (int c = 0; c < cols; c++) {
			headList.add(sheet.getCell(c, 0).getContents());
		}
		// 测试用例名称,便于后面用例排序
		List<String> caseNameList = new ArrayList<String>();
		for (int c = 1; c < rows; c++) {
			String caseName = sheet.getCell(0, c).getContents();
			if (caseName.isEmpty() || caseName==null || ("").equals(caseName)) {
				continue;
			}
			caseNameList.add(caseName);
		}
		
		Map<String, Map<String, String>[]> tmpCases = new HashMap<>();
		Map<String, String>[] caseSteps = null;
		// 获取合并单元格
		Range[] rangeCell = sheet.getMergedCells();
		// 合并单元格用例提取
		for (Range m : rangeCell) {
			int mTopRow = m.getTopLeft().getRow();
			int mBottomRow = m.getBottomRight().getRow();
			String caseName = sheet.getCell(m.getTopLeft().getColumn(), m.getTopLeft().getRow()).getContents();
			caseSteps = new HashMap[mBottomRow-mTopRow+1];
			for (int i = 0; i < caseSteps.length; i++) {
				Map<String, String> rowMap = new HashMap<>();
				for (int col = 0; col < cols; col++) {
					rowMap.put(headList.get(col), sheet.getCell(col,mTopRow+i).getContents());
				}
				caseSteps[i] = rowMap;
			}
			tmpCases.put(caseName, caseSteps);
		}
		
		// 非合并单元格用例提取
		for (int row = 1; row < rows; row++) {
			boolean isInRange = false;
			String caseName = null;
			// 判断是否在合并单元格
			for (Range m : rangeCell) {
				int mTopRow = m.getTopLeft().getRow();
				int mBottomRow = m.getBottomRight().getRow();
				if (row>=mTopRow && row<= mBottomRow) { 
					isInRange = true;
					break;
				}
			}
			if (! isInRange) { // 不在合并单元格
				Map<String, String> rowMap = new HashMap<>();
				caseSteps = new HashMap[1];
				for (int col = 0; col < cols; col++) {
					rowMap.put(headList.get(col), sheet.getCell(col,row).getContents());
					caseName = sheet.getCell(0,row).getContents();
				}
				caseSteps[0] = rowMap;
				tmpCases.put(caseName, caseSteps);
			}
		}
		book.close();
		
		//转为map[][]
		Map<String, Object>[][] arrCaseMap = new HashMap[tmpCases.size()][1];
		for (int i = 0; i < arrCaseMap.length; i++) {
			arrCaseMap[i][0] = new HashMap<>();
		}
		int i=0;
//		for (Entry<String, Map<String, String>[]> entry : tmpCases.entrySet()) {
//			arrCaseMap[i][0].put("CaseName", entry.getKey());
//			arrCaseMap[i][0].put("Steps", entry.getValue());
//			++i;
//		}
		for (String key : tmpCases.keySet()) {
			arrCaseMap[i][0].put("CaseName", key);
			arrCaseMap[i][0].put("Steps", tmpCases.get(key));
			++i;
		}
		
		// 用例排序
		Map<String, Object>[][] casesMap = new HashMap[tmpCases.size()][1];
		for (int a=0; a<caseNameList.size();a++) {
			casesMap[a][0] = new HashMap<>();
			for (int j = 0; j < casesMap.length; j++) {
				String caseName = (String) arrCaseMap[j][0].get("CaseName");
				if (caseName.equals(caseNameList.get(a))) {
					casesMap[a][0].put("CaseName", caseName);
					casesMap[a][0].put("Steps", arrCaseMap[j][0].get("Steps"));
				}
			}
		}
		return casesMap;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object>[][] arrMap = (HashMap<String, Object>[][]) getSheetData2(
				"E:/workspace/auto_test/testcasesa.xls", "cases_a");

		for (int i = 0; i < arrMap.length; ++i) {
			HashMap<String, Object> map = arrMap[i][0];

			System.out.println( "CaseName:" + map.get("CaseName"));
			Map<String, String>[] steps = (Map<String, String>[]) map.get("Steps");
			for (int j = 0; j < steps.length; j++) {
				Map<String, String> map2 = steps[j];
				System.out.println("step:" + map2);
//				for (Entry<String, String> entry : map2.entrySet()) {
//					System.out.println(map.get("CaseName") + "===step:" + map2.get("Step") + "==" + entry.getKey() + ":" + entry.getValue());
//				}
			}
		}
	}
}