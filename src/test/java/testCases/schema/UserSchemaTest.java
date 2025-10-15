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

@Epic("E-Commerce REST API Testing")
@Feature("Schema Validation - User Module")
public class UserSchemaTest extends BaseTest {


    @Test(description = "Validate JSON schema for User API response")
    public void verifyUserSchema(){


        given()
                .spec(requestSpec)

                .when()
                .get(Routes.get_allUsers)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(SCHEMA_PATH +"userSchema.json"));

    }
}
