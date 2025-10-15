package testCases.product;
import static io.restassured.RestAssured.given;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Product;
import utils.ProductDataProvider;

@Epic("E-Commerce REST API Testing")
@Feature("Product Module - Data Driven Testing")
public class ProductDataDrivenTesting extends BaseTest {
    @Test(dataProvider = "positiveProductData", dataProviderClass = ProductDataProvider.class)

    public void verifyNewProductCreation(Product product) {

        Response response =
                given()
                        .spec(requestSpec)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(201)
                        .extract().response();

        Assert.assertTrue(response.asString().contains(product.getTitle()), "Product title not found in response.");
    }


}
