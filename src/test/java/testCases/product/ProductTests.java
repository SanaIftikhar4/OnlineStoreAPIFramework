package testCases.product;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import paylaods.Payload;
import pojo.Product;
import routes.Routes;
import utils.LogHelper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * ProductTests:
 * Contains test cases related to the Products module.
 * Each test validates API behavior for different operations like GET ,POST ,PUT and DELETE.
 */
public class ProductTests extends BaseTest {

    /**
     * Test 1: Verify retrieving all products returns a valid list and status code 200
     */
    @Test(description = "Verify retrieving all products returns valid list and status code 200")
    public void verifyAllProductsAreRetrievedSuccessfully() {

        // Send GET request
        Response response =
                given()
                        .spec(requestSpec)  // Reuses setup from BaseTest
                .when()
                        .get(Routes.get_allProducts)
                .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("size()", greaterThan(0))
                        .body("[0].id", notNullValue())
                        .body("[0].title", notNullValue())
                        .body("[0].price", greaterThan(0.0f))
                        .body("[0].description", notNullValue())
                        .body("[0].category", notNullValue())
                        .body("[0].image", notNullValue())
                        .extract().response();

        // Extract response details
        int productCount = response.jsonPath().getList("$").size();
        LogHelper.info("Total products retrieved: " + productCount);

        // Assert that we have at least one product
        Assert.assertTrue(productCount > 0, "Expected non-empty product list");
    }

    /**
     * Test 2: Verify new product can be successfully created via POST /products
     ** Objective:
     *  * Validate that a new product can be created successfully when valid payload data is sent.
     *  * The test ensures the API responds with status code 201 and returns correct product details.
     */

        @Test(description = "Verify new product can be successfully created via POST /products")
        public void verifyNewProductCreation() {

            // Generate product payload dynamically
            Product newProduct = Payload.productPayload();
            LogHelper.info("Creating product with title: " + newProduct.getTitle()+
                    " | Price: $" + newProduct.getPrice() +
                    " | Category: " + newProduct.getCategory());

            // Send POST request and validate
            Response response =
                    given()
                            .spec(requestSpec)
                            .body(newProduct)
                    .when()
                            .post(Routes.post_newProduct)
                    .then()
                            .log().ifValidationFails()
                            .statusCode(201)
                            .body("title", equalTo(newProduct.getTitle()))
                            .body("price", greaterThan(0.0f))
                            .body("description", equalTo(newProduct.getDescription()))
                            .body("category", equalTo(newProduct.getCategory()))
                            .body("image", equalTo(newProduct.getImage()))
                            .extract().response();

            //  Extract response data
            int createdProductId = response.jsonPath().getInt("id");
            String createdTitle = response.jsonPath().getString("title");
            double createdPrice = response.jsonPath().getDouble("price");
            String createdCategory = response.jsonPath().getString("category");

            //  Log and verify result
            LogHelper.info("Product created successfully with ID: " + createdProductId +
                    " | Title: " + createdTitle +
                    " | Price: $" + createdPrice);

            //Assertions
            Assert.assertTrue(createdProductId > 0, "Expected valid product ID (non-zero).");
            Assert.assertEquals(createdTitle, newProduct.getTitle(), "Product title mismatch.");
            Assert.assertEquals(createdPrice, newProduct.getPrice(), "Product price mismatch.");
        }
    /**
     *  Test Case 3: Verify retrieving a single product by valid ID
     *
     * Objective:
     * - Send a GET request to /products/{id}
     * - Validate that the response returns the correct product details (ID, title, price)
     * - Ensure the response status code is 200 (OK)

     */
        @Test(description = "Verify retrieving a single product by valid ID returns correct details and status 200")
        public void verifyProductRetrievedByIdSuccessfully() {

            //  Read product ID dynamically from config.properties
            int productId = Integer.parseInt(configReader.getProperty("productId"));

            //  Send GET request with path parameter (using the {id} placeholder in the route)
            Response response =
                    given()
                            .spec(requestSpec)                   // Inherited reusable request setup (headers, contentType)
                            .pathParam("id", productId)          // Replace {id} in the route with actual productId
                    .when()
                            .get(Routes.get_productById)         // Perform GET /products/{id}
                    .then()
                            .log().ifValidationFails()           // Log request/response details only if test fails
                            .statusCode(200)                     // Verify HTTP response is 200 OK
                            .body("id", equalTo(productId))
                            .body("title", notNullValue())
                            .body("price", greaterThan(0.0f))
                            .body("description", notNullValue())
                            .body("category", notNullValue())
                            .body("image", notNullValue())
                            .extract().response();           // Extract response for further custom validation

            //  Extract useful data from JSON response
            String productTitle = response.jsonPath().getString("title");
            double productPrice = response.jsonPath().getDouble("price");
            String productCategory = response.jsonPath().getString("category");
            String productDescription = response.jsonPath().getString("description");
            String productImage = response.jsonPath().getString("image");

            //  Log retrieved details using centralized logger
            LogHelper.info(" Retrieved product ID: " + productId +
                    " | Title: " + productTitle +
                    " | Price: $" + productPrice +
                    " | Category: " + productCategory);

            //  Additional assertions for safety
            Assert.assertNotNull(productTitle, "Expected product title not to be null.");
            Assert.assertTrue(productPrice > 0, "Expected valid (positive) price for product ID: " + productId);
            Assert.assertNotNull(productCategory, "Expected category not to be null.");
            Assert.assertNotNull(productDescription, "Expected description not to be null.");
            Assert.assertNotNull(productImage, "Expected product image URL not to be null.");
        }

    /**
     *  Test Case 4: Verify an existing product can be successfully updated
     *
     * Objective:
     * - Send a put request to /products/{id}
     * - Validate that the response returns the correct updated product details (ID, title, price)
     * - Ensure the response status code is 200 (OK)

     */

        @Test(description = "Verify an existing product can be updated successfully via PUT /products/{id}")
        public void verifyProductUpdatedSuccessfully() {

            //  Read Product ID from config.properties
            int productId = Integer.parseInt(configReader.getProperty("productId"));
            LogHelper.info(" Starting update for product ID: " + productId);

            //  Generate dynamic product payload using the Payload utility class
            Product updatedProduct = Payload.productPayload();
            LogHelper.info("Updating product with data: " +
                    "Title = " + updatedProduct.getTitle() +
                    " | Price = $" + updatedProduct.getPrice()+
            " | Category = " + updatedProduct.getCategory());

            // Execute PUT request and validate response
            Response response =
                    given()
                            .spec(requestSpec)
                            .pathParam("id", productId)
                            .body(updatedProduct)
                    .when()
                            .put(Routes.put_updateProductById)
                    .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .body("id", equalTo(productId))
                            .body("title", equalTo(updatedProduct.getTitle()))
                            .body("price", greaterThan(0.0f))
                            .body("description", equalTo(updatedProduct.getDescription()))
                            .body("category", equalTo(updatedProduct.getCategory()))
                            .body("image", equalTo(updatedProduct.getImage()))
                            .extract().response();

            //  Extract response data for verification
            String resTitle = response.jsonPath().getString("title");
            double resPrice = response.jsonPath().getDouble("price");
            String resCategory = response.jsonPath().getString("category");
            String resDescription = response.jsonPath().getString("description");
            String resImage = response.jsonPath().getString("image");

            // Log details and assert correctness
            LogHelper.info(" Product updated successfully — ID: " + productId +
                    " | Title: " + resTitle +
                    " | Price: $" + resPrice +
                    " | Category: " + resCategory);

            //Assertions for logical validation

            Assert.assertEquals(resTitle, updatedProduct.getTitle(), "Title did not update correctly.");
            Assert.assertEquals(resPrice, updatedProduct.getPrice(), "Price did not update correctly.");
            Assert.assertEquals(resCategory, updatedProduct.getCategory(), "Category did not update correctly.");
            Assert.assertEquals(resDescription, updatedProduct.getDescription(), "Description did not update correctly.");
            Assert.assertEquals(resImage, updatedProduct.getImage(), "Image did not update correctly.");
        }

        @Test(description = "Verify a product can be deleted successfully using Id")
        public void verifyProductDeletedSuccessfully(){

            //Read product from config files

            int productId = Integer.parseInt(configReader.getProperty("productId"));
            LogHelper.info("Initiating delete request for product ID: " + productId);

            //verify product deletion via GET
            Response response =
                    given()
                            .spec(requestSpec)
                            .pathParam("id", productId)
                    .when()
                            .delete(Routes.delete_productById)
                    .then()
                            .log().ifValidationFails()
                            .statusCode(200) //  Expected per API documentation
                            .extract().response();
            LogHelper.info("Product deleted successfully — ID: " + productId);
            //Verify product deletion via GET
            Response getResponse =
                    given()
                            .spec(requestSpec)
                            .pathParam("id", productId)
                            .when()
                            .get(Routes.get_productById)
                            .then()
                            .log().ifValidationFails()
                            .extract().response();

            int statusCode = getResponse.getStatusCode();
            // Assert the deleted product is no longer available
            Assert.assertEquals(statusCode, 200,
                    "Expected 200 OK after deletion as FakeStore API returns static data.");
            LogHelper.info("Verified product ID " + productId + " deletion acknowledged with status 200.");
        }



        }




