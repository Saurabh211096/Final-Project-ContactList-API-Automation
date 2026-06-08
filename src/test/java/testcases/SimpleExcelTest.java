package testcases;

import org.apache.poi.xssf.usermodel.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class SimpleExcelTest extends BaseTest {

	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	
	int rowIndex = 1;
	
	@BeforeTest
	public void setupExcel() throws IOException {
		fis = new FileInputStream("src/test/resources/testData.xlsx");
		wb = new XSSFWorkbook(fis);
		sheet = wb.getSheet("Users");
	}
	
	@DataProvider(name = "SimpleData")
	public Object[][] getExcelData() {
		int rows = sheet.getPhysicalNumberOfRows();
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		
		Object[][] data = new Object[rows - 1][cells];
		
		for (int i = 1; i < rows; i++) {
			for (int j = 0; j < cells; j++) {
				data[i - 1][j] = sheet.getRow(i).getCell(j).getStringCellValue();
			}
		}
		return data;
	}
	
	@Test(dataProvider = "SimpleData")
	public void testWithExcel(String fName, String lName, String email
			, String password) {
		
		HashMap<String, String> payload = new HashMap<>();
		payload.put("firstName", fName);
		payload.put("lastName", lName);
		payload.put("email", email);
		payload.put("password", password);
		
		Response resp = given()
				.contentType("application/json")
				.body(payload)
				.when().post("/users");
		resp.then().statusCode(201);
		System.out.println("SUCCESS: Created user " + fName);
		
		// Writing to Excel
		sheet.getRow(rowIndex).createCell(4).setCellValue("PASS");
		rowIndex++;
	}
	
	@AfterTest
	public void saveExcel() throws IOException {
		fis.close();
		FileOutputStream fos = new FileOutputStream("src/test/resources/testData.xlsx");
		wb.write(fos);
		
		wb.close();
		fos.close();
		System.out.println("SUCCESS: Excel file saved permanently!");
	}
}












