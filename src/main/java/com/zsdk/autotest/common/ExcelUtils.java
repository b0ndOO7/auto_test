package com.zsdk.autotest.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.control.Cell;
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
			Workbook wb = Workbook.getWorkbook(fExcel);
			Sheet sht = wb.getSheet(sheetName);
			int rowCount = sht.getRows();
			int colCount = sht.getColumns();
			System.out.println("中文");
			System.out.println("rowCount:" + rowCount + ",colCount:" + colCount);
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
					rowMap.put(sht.getCell(colIndex, 0).getContents().toString(), sht.getCell(colIndex, rowIndex).getContents().toString());
				}
				excelContents.put(testCaseCode, rowMap);
			}
			wb.close();
			// HashMap<String, String> tmpMap = new HashMap();
			// tmpMap.put("count", "" + (rowCount-2));
			// excelContents.put("testsCount", tmpMap);
			return excelContents;
		} catch (Exception e) {
			System.out.println("发生异常：" + e);
		}
		return null;
	}

	
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
                arrMap[r-1][0].put(list.get(c), sheet.getCell(c, r).getContents());
            }
        }
        book.close();
        return arrMap;
	}
	
	
	
	
	
	public static void main(String[] args) {
		HashMap<String, String>[][] arrMap = (HashMap<String, String>[][]) getSheetData("E:/workspace/auto_test/testcases.xls", "cases_a");
		
		for(int i=0; i< arrMap.length; ++i) {
			HashMap<String, String> map = arrMap[i][0];
			System.out.println(map);
			
			for(Entry<String, String> entry : map.entrySet()){
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
			System.out.println(map.get("casename"));
		}
	}
}