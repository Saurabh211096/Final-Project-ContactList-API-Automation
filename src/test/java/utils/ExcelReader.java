package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelReader {

	public static Object[][] getExcelData(String fileName, String sheetName) {
		
		Object[][] data = null;
		
		try {
			FileInputStream file = 
					new FileInputStream("src/test/resources/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			
			int rowCount = sheet.getLastRowNum();
			int colCount = sheet.getRow(0).getLastCellNum();
			
			data = new Object[rowCount][colCount];
			
			DataFormatter formatter = new DataFormatter();
			
			for (int r = 1; r <= rowCount; r++) {
				XSSFRow row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				for (int c = 0; c < colCount; c++) {
					XSSFCell cell = row.getCell(c);
					data[r-1][c] = formatter.formatCellValue(cell);
				}
			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			System.out.println("Error reading Excel file!");
			e.printStackTrace();
		}
		return data;
	}
	
	public static void setCellData(String fileName, String sheetName
			, int rowNum, int colNum, String data) {
		
		try {
			String path = "src/test/resources/" + fileName;
			FileInputStream fis = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			
			XSSFRow row = sheet.getRow(rowNum);
			if (row == null) {
				row = sheet.createRow(rowNum);
			}
			
			XSSFCell cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
			}
			
			cell.setCellValue(data);
			
			fis.close();
			FileOutputStream fos = new FileOutputStream(path);
			
			workbook.close();
			fos.close();
		} catch (Exception e) {
			System.out.println("Error writing to Excel file!");
			e.printStackTrace();
		}
		
	}
	
}

















