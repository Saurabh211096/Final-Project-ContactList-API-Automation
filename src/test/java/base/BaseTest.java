package base;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.restassured.RestAssured;
import utils.ConfigReader;

public class BaseTest {
	
	protected static ExtentReports extent;
	protected ExtentTest test;

	@BeforeSuite
	public void setUp() {
		ExtentSparkReporter spark = 
				new ExtentSparkReporter("reports/API_AutomationReport.html");
		extent = new ExtentReports();
		extent.attachReporter(spark);
		
		RestAssured.baseURI = ConfigReader.getProperty("baseURI");
		System.out.println("Base URI set successfully: " + RestAssured.baseURI);
	}
	@BeforeMethod
	public void startTest(Method method) {
		test = extent.createTest(method.getName());
	}
	@AfterMethod
	public void logTestResult(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.fail("Test Failed! Error: " + result.getThrowable());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass("Test passed successfully!");
		}
	}
	@AfterSuite
	public void tearDownReport() {
		extent.flush();
	}
	
}















