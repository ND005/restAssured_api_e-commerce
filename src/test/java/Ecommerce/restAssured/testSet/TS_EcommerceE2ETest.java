package Ecommerce.restAssured.testSet;

import org.testng.annotations.Test;

import Ecommerce.restAssured.Pojo.loginRequest;
import Ecommerce.restAssured.Pojo.loginResponce;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

public class TS_EcommerceE2ETest {
	RequestSpecification RequestSpec = null;
	loginResponce loginResp = null;

	@Test
	public void TC01_loginResponce() {
		// Test 01 :- Login to service and generate Access request like Token & UserID
		loginRequest logRep = new loginRequest();
		logRep.setUserEmail("apiTesting@gmail.com");
		logRep.setUserPassword("ABC123abc");
		RequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		loginResp = given().spec(RequestSpec).body(logRep).when().post("/api/ecom/auth/login").then().extract()
				.response().as(loginResponce.class);

		// Access Request generated
		System.out.println(":::Token:::" + loginResp.getToken());
		System.out.println(":::User ID:::" + loginResp.getUserId());
	}

	public void TC02_createProduct() {
		// Test 02 :- Create an order in service
		
	}

}
