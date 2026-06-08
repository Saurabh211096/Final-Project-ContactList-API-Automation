package testcases;

import java.util.HashMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.gherkin.model.Given;

import base.BaseTest;
import io.restassured.response.Response;
import utils.ExcelReader;

import static io.restassured.RestAssured.*;

public class UserTests extends BaseTest {

	public static String token;

	@DataProvider(name = "UserData")
	public Object[][] getUserData() {
		return ExcelReader.getExcelData("testData.xlsx", "Users");
	}
	
	@Test(priority = 1, dataProvider = "UserData")
	public void testCreateUser(String fName, String lName, String email
			, String password) {

		String uniqueEmail = System.currentTimeMillis() + email;
		
		HashMap<String, String> payload = new HashMap<>();
		payload.put("firstName", fName);
		payload.put("lastName", lName);
		payload.put("email", uniqueEmail);
		payload.put("password", password);

		Response res = given().contentType("application/json").body(payload).when().post("/users");
		System.out.println("Response Body is: ");
		res.prettyPrint();
		res.then().statusCode(201);
		System.out.println("Status code for testCreateUser: " + res.getStatusCode());

		token = res.jsonPath().getString("token");
		System.out.println("SUCCESS: Extracted Token is: " + token);

		//ExcelReader.setCellData("testData.xlsx", "Users", 1, 4, "PASS");

	}

	@Test(priority = 2)
	public void testGetUserProfile() {

		Response resp = given().header("Authorization", "Bearer " + token).when().get("/users/me");

		System.out.println("\nSUCCESS: Profile Details are: ");
		resp.prettyPrint();

		resp.then().statusCode(200);
		System.out.println("Status code for testGetUserProfile: " + resp.getStatusCode());
	}

}
