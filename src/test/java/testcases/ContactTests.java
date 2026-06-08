package testcases;

import static io.restassured.RestAssured.*;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import io.restassured.response.Response;

public class ContactTests extends BaseTest {

	public static String contactID;

	@Test(priority = 1)
	public void testAddContact() {

		HashMap<String, Object> payload = new HashMap<>();
		payload.put("firstName", "John");
		payload.put("lastName", "Doe");
		payload.put("birthdate", "1970-01-01");
		payload.put("email", "jdoe@fake.com");
		payload.put("phone", "8005555555");
		payload.put("street1", "1 Main St.");
		payload.put("street2", "Apartment A");
		payload.put("city", "Anytown");
		payload.put("stateProvince", "KS");
		payload.put("postalCode", "12345");
		payload.put("country", "USA");

		Response resp = given().contentType("application/json").header("Authorization", "Bearer " + UserTests.token)
				.body(payload).when().post("/contacts");

		System.out.println("\nSUCCESS: Contact Created Details: ");
		resp.prettyPrint();

		resp.then().statusCode(201);
		System.out.println("Status code for testAddContact: " + resp.getStatusCode());

		contactID = resp.jsonPath().getString("_id");
		System.out.println("SUCCESS: Extracted Contact ID: " + contactID);
	}

	@Test(priority = 2)
	public void testGetContactList() {

		Response res = given().header("Authorization", "Bearer " + UserTests.token).when().get("/contacts");

		System.out.println("\nSUCCES: All Contacts in List: ");
		res.prettyPrint();

		res.then().statusCode(200);
		System.out.println("Status code matched for testGetContactList: " + res.getStatusCode());

	}

	@Test(priority = 3)
	public void getSingleContact() {

		Response resp = given().header("Authorization", "Bearer " + UserTests.token).when()
				.get("/contacts/" + contactID);

		System.out.println("\nSUCCESS: Single Contact Details: ");
		resp.prettyPrint();

		resp.then().statusCode(200);
		System.out.println("Status code for getSingleContact: " + resp.getStatusCode());

	}

	@Test(priority = 4)
	public void testUpdateContactPut() {

		HashMap<String, Object> payload = new HashMap<>();
		payload.put("firstName", "Amy");
		payload.put("lastName", "Miller");
		payload.put("birthdate", "1992-02-02");
		payload.put("email", "amiller@fake.com");
		payload.put("phone", "8005554242");
		payload.put("street1", "13 School St.");
		payload.put("street2", "Apt. 5");
		payload.put("city", "Washington");
		payload.put("stateProvince", "QC");
		payload.put("postalCode", "A1A1A1");
		payload.put("country", "Canada");

		Response res = given().contentType("application/json").header("Authorization", "Bearer " + UserTests.token)
				.body(payload).when().put("/contacts/" + contactID);

		res.then().statusCode(200);
		System.out.println("Status code for testUpdateContactPut:" + res.getStatusCode());

		String updatedFirstName = res.jsonPath().getString("firstName");
		Assert.assertEquals(updatedFirstName, "Amy", "Error: First name was not updated to Amy!");
		System.out.println("\nSUCCESS: PUT Update verified. Name is now: " + updatedFirstName);
	}

	@Test(priority = 5)
	public void testUpdateContactPartial() {

		HashMap<String, Object> payload = new HashMap<>();
		payload.put("firstName", "Anna");

		Response resp = given().contentType("application/json").header("Authorization", "Bearer " + UserTests.token)
				.body(payload).when().patch("/contacts/" + contactID);

		resp.then().statusCode(200);

		String updatedFirstName = resp.jsonPath().getString("firstName");
		Assert.assertEquals(updatedFirstName, "Anna", "Error: First name was not updated to Anna!");
		System.out.println("\nSUCCESS: PATCH Update verified. Name is now: " + updatedFirstName);

	}
	
	@Test(priority = 6)
	public void testLogoutUser() {
		
		Response res = given()
				.header("Authorization", "Bearer " + UserTests.token)
				.when().post("/users/logout");
		res.then().statusCode(200);
		System.out.println("Status code for testLogoutUser: "
				+ res.getStatusCode());
		System.out.println("\nSUCCESS: User logged out. Token invalidated successfully!");
		
	}

}




