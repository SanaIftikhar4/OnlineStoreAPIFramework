package testCases.auth;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import payloads.Payload;
import pojo.Auth;
import routes.Routes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static payloads.Payload.authPayload;

@Epic("Fake Store REST API Testing ===> Auth Test Cases")
@Feature("Auth Module")
public class LoginTest extends BaseTest {

    @Test(description = "Login using invalid credentials")
    public void verifyLoginWithInvalidCredentials(){

        Auth authNewUser =  Payload.authPayload();
        Response response =
                given()
                        .spec(requestSpec)
                        .body(authNewUser)

                .when()

                        .post(Routes.post_auth)
                .then()
                        .log().ifValidationFails()
                        .statusCode(401)
                        .body(equalTo("username or password is incorrect")) // or .body(containsString("username or password is incorrect"))
                        .extract().response();

        //USING ASSERTION:

        String errorMsg = response.asString(); // get full body as text
        Assert.assertTrue(errorMsg.contains("username or password is incorrect"),
                "Expected error message not found in response body");

    }

    @Test(description = "verify that a user can login using valid credentials")
    public void verifyLoginWithValidCredentials(){

        //Reading valid data from config.properties file
        String username= configReader.getProperty("username");
        String password= configReader.getProperty("password");

        Auth login = new Auth(username,password);
        Response response=
                given()
                        .spec(requestSpec)
                        .body(login)
                .when()
                        .post(Routes.post_auth)
                .then()
                        .log().ifValidationFails()
                        .statusCode(201)
                        .body("token", notNullValue())
                        .extract().response();

        //or using Assertion
        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token,"Token should not be null after valid login");





    }



}
