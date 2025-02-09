package Ecommerce.restAssured.testSet;

import org.testng.Assert;
import org.testng.annotations.Test;
import Ecommerce.restAssured.Pojo.createOrderRequest;
import Ecommerce.restAssured.Pojo.createOrderResponce;
import Ecommerce.restAssured.Pojo.createOrderSubRequest;
import Ecommerce.restAssured.Pojo.createProductResponce;
import Ecommerce.restAssured.Pojo.deleteResponce;
import Ecommerce.restAssured.Pojo.loginRequest;
import Ecommerce.restAssured.Pojo.loginResponce;
import Ecommerce.restAssured.Pojo.validateOrderResponce;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TS_EcommerceE2ETest {

	loginResponce loginResp = null;
	createProductResponce CreateResp = null;
	createOrderResponce OrderResp = null;
	validateOrderResponce OrderValidationResp = null;
	deleteResponce DeleteResp = null;

	@Test
	public void TC01_loginResponce() {
		// Test 01 :- Login to service and generate Access request like Token & UserID
		System.out.println(":::LOGIN:::STARTED:::");
		loginRequest logRep = new loginRequest();
		logRep.setUserEmail("apiTesting@gmail.com");
		logRep.setUserPassword("ABC123abc");

		RequestSpecification RequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		loginResp = given().spec(RequestSpec).body(logRep).when().post("/api/ecom/auth/login").then().extract()
				.response().as(loginResponce.class);

		// Access Request generated
		System.out.println(":::TOKEN:::" + loginResp.getToken());
		System.out.println(":::USER ID:::" + loginResp.getUserId());
		System.out.println(":::LOGIN:::SUCCESSFULLY:::");
	}

	@Test
	public void TC02_createProduct() {
		// Test 02 :- Create an order in service
		System.out.println(":::PRODUCT ADDING:::STARTED:::");
		RequestSpecification RequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResp.getToken().toString()).build();

		RequestSpecification addProductinSite = given().spec(RequestSpec).param("productName", "Jaguar Shoes")
				.param("productAddedBy", loginResp.getUserId().toString()).param("productSubCategory", "fashion")
				.param("productCategory", "shoes").param("productPrice", "6969")
				.param("productDescription", "Puma Nike Jaguar").param("productFor", "men")
				.multiPart("productImage", new File(new java.io.File("").getAbsolutePath() + "/pumaShoes.png"));

		CreateResp = addProductinSite.when().post("/api/ecom/product/add-product").then().extract()
				.as(createProductResponce.class);

		Assert.assertTrue(
				CreateResp.getMessage().contains("Product Added Successfully") && CreateResp.getProductId() != null);
		System.out.println(":::PRODUCT ID:::" + CreateResp.getProductId());
		System.out.println(":::PRODUCT ADDED:::SUCCESSFULLY:::");
	}

	@Test
	public void TC03_createOrder() {
		// Test 03 :- Created Order using API
		System.out.println(":::CREATE ORDER PROCESS:::STARTED:::");
		// Adding values in sub list
		createOrderSubRequest SubRequest = new createOrderSubRequest();

		SubRequest.setCountry("India");
		SubRequest.setProductOrderedId(CreateResp.getProductId().toString());

		// Converting that sublist into array
		List<createOrderSubRequest> cosr = new ArrayList<createOrderSubRequest>();
		// Adding that array value into class with return type of array
		cosr.add(SubRequest);

		// Now calling the main order class
		createOrderRequest CreateOrderReq = new createOrderRequest();
		CreateOrderReq.setOrders(cosr);

		RequestSpecification RequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResp.getToken().toString()).setContentType(ContentType.JSON).build();
		RequestSpecification createOrder = given().spec(RequestSpec).body(CreateOrderReq);
		OrderResp = createOrder.when().post("api/ecom/order/create-order").then().extract()
				.as(createOrderResponce.class);
		System.out.println(":::PRODUCT ORDER ID:::" + OrderResp.getProductOrderId().get(0));
		System.out.println(":::PRODUCT ORDER:::" + OrderResp.getOrders().get(0));
		System.out.println(":::ORDER CREATED:::DONE:::");
	}

	@Test
	public void TC04_getOrderDetails() {
		// Test 04:- Verifying the created order using API
		System.out.println(":::VERIFY ORDER PROCESS:::STARTED:::");
		RequestSpecification RequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResp.getToken().toString())
				.addQueryParam("id", OrderResp.getOrders().get(0).toString()).build();

		OrderValidationResp = given().spec(RequestSpec).when().get("/api/ecom/order/get-orders-details").then()
				.extract().as(validateOrderResponce.class);
		// Verify data using assertion.
		System.out.println(":::ORDERED BY ID:::" + OrderValidationResp.getData().getOrderById());
		System.out.println(":::PRODUCT NAME:::" + OrderValidationResp.getData().getProductName());
		System.out.println(":::VERIFIED ORDER PROCESS:::DONE:::");
	}

	@Test
	public void TC05_deleteOrder() {
		System.out.println(":::ORDER DELETE PROCESS:::STARTED:::");

		RequestSpecification RequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResp.getToken().toString())
				.addPathParam("PRODUCTID", CreateResp.getProductId()).setContentType(ContentType.JSON).build();

		DeleteResp = given().spec(RequestSpec).when().delete("/api/ecom/product/delete-product/{PRODUCTID}").then()
				.extract().as(deleteResponce.class);
		/*
		 * RequestSpecification RequestSpecorder = new
		 * RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		 * .addHeader("Authorization", loginResp.getToken().toString())
		 * .addPathParam("ORDERID",
		 * OrderResp.getProductOrderId().get(0)).setContentType(ContentType.JSON).build(
		 * );
		 * 
		 * String Resp = given().log().all().spec(RequestSpecorder).when().log().all()
		 * .delete("/api/ecom/order/delete-order/{ORDERID}").then().log().all().
		 * assertThat().statusCode(200) .extract().asString();
		 */
		Assert.assertTrue(DeleteResp.getMessage().contains("Deleted Successfully"));
		System.out.println(":::ORDER DELETED:::DONE:::");
	}
}
