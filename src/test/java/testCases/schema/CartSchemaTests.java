package testCases.schema;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;
import routes.Routes;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

//Genreate JSON  response using POSTMAN ---> sending Get request @ https://fakestoreapi.com/products
//convert JSON to JSON Schema using https://transform.tools/json-to-json-schema

@Epic("Fake Store REST API Testing ===> Schema Validation (Cart)")
@Feature("Schema Validation - Cart Module")
public class CartSchemaTests extends BaseTest {


    @Test(description = "Validate JSON schema for Cart API response")
    public void verifyCartSchema(){

        //int cardId = Integer.parseInt(configReader.getProperty("cartId"));
        given()
                .spec(requestSpec)
              //  .pathParam("id",cardId)
                .when()
                .get(Routes.get_allCarts)
               // .get(Routes.get_cartById)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(SCHEMA_PATH +"cartSchema.json"));

    }

}
