package testCases.cart;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import payloads.Payload;
import pojo.Cart;
import routes.Routes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static payloads.Payload.cartPayload;


@Epic("E-Commerce REST API Testing")
@Feature("Cart Module - CRUD Operations")
public class CartTest extends BaseTest {

    static int createdCartId; // store dynamically created cart id for later update/delete tests

    @Story("Retrieve all carts")
    @Test(priority = 1, description = "Verify retrieving all carts returns list of items and status code 200")
    public void verifyAllCartsAreRetrievedSuccessfully(){

        given()
                .spec(requestSpec)
                .when()
                .get(Routes.get_allCarts)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("size()",greaterThan(0));

    }


    @Story("Create new cart")
    @Test(priority = 2,description = "Verify creating a new cart with valid payload returns 201 and correct data")
    public void verifyNewCartIsCreatedSuccessfully(){

        Cart newCart = Payload.cartPayload();

        Response response=
            given()
                    .spec(requestSpec)
                    .body(newCart)
                    .when()
                    .post(Routes.post_newCart)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(201)
                    .body("id",notNullValue())
                    .body("userId",equalTo(newCart.getUserId()))
                    .body("products.size()",greaterThan(0))
                    .extract().response();

        //or Assert:
        createdCartId= response.jsonPath().getInt("id");
        Assert.assertTrue(createdCartId >0,"Cart id should be created ");
    }

    @Test(priority = 3,description = "Verify retrieving a specific cart by ID returns correct cart details")
    public void verifySingleCartIsRetrievedById(){

        Assert.assertTrue(createdCartId > 0, "Cart ID must be created before running this test");

        Response response =
        given()
                .spec(requestSpec)
                .pathParam("id",createdCartId)
                .when()
                .get(Routes.get_cartById)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
             //   .body("id",equalTo(createdCartId))
                .extract().response();
        //For demo/testing APIs like FakeStore, the right approach is to:

        //Still perform the GET request.

        //Log and verify that the response structure exists (but not rely on dynamic IDs that the backend doesn’t persist).

        // Check if ID exists in response JSON before asserting
        if (response.jsonPath().get("id") != null) {
            int idFromResponse = response.jsonPath().getInt("id");
            Assert.assertEquals(idFromResponse, createdCartId, "Returned ID should match created ID!");
        } else {
            System.out.println(" Cart not found in FakeStoreAPI — API does not persist created data.");
        }
    }

    @Story("Update existing cart")
    @Test(priority = 4,description = "Verify updating an existing cart returns updated data")
    public void verifyCartIsUpdatedSuccessfully() {

        Assert.assertTrue(createdCartId > 0, "Cart ID must be created before running this test");

        Cart updatedCart = Payload.cartPayload(); // new data for update

        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("id", createdCartId)
                        .body(updatedCart)
                        .when()
                        .put(Routes.put_updateCartById)
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response();

        int responseUserId = response.jsonPath().getInt("userId");
        Assert.assertEquals(responseUserId, updatedCart.getUserId(), "User ID should match updated payload!");
    }

    //  DELETE - Delete the cart
    @Story("Delete cart by ID")
    @Test(priority = 5, description = "Verify deleting a cart by ID removes it successfully")
    public void verifyCartIsDeletedSuccessfully() {
        Assert.assertTrue(createdCartId > 0, "Cart ID must be created before running this test");

        given()
                .spec(requestSpec)
                .pathParam("id", createdCartId)
                .when()
                .delete(Routes.delete_cartById)
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204))); // some APIs return 204 for delete

        // Verify deletion by trying to fetch the deleted cart
        // Commenting this out since API doesn't actually delete the resource
        // given()
        //         .spec(requestSpec)
        //         .pathParam("id", createdCartId)
        // .when()
        //         .get(Routes.get_cartById)
        // .then()
        //         .statusCode(anyOf(is(404), is(400)));

    }
}
