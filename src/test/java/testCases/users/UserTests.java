package testCases.users;
import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Users;
import payloads.Payload;
import routes.Routes;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

import base.BaseTest;

import java.util.List;

@Epic("E-Commerce REST API Testing")
@Feature("User Module - CRUD Operations")
public class UserTests extends BaseTest {

    // Test 1: To Validates that API successfully returns the complete list of users
    @Test(priority = 1, description = "Verify that all users are retrieved successfully from the API.")
    public void verifyGetAllUsers() {
        Response response =
                given()
                        .spec(requestSpec)
                        .when()
                        .get(Routes.get_allUsers)
                        .then()
                        .log().ifValidationFails()
                        .log().body()
                        .statusCode(200)
                        .extract().response();

        int userCount = response.jsonPath().getList("$").size();
        Assert.assertTrue(userCount > 0, "No users found!");
    }

    /**
     *  Test 2: Get all users with limit
     * Validates that limiting results works correctly via query parameter.
     */

    @Test(priority = 2, description = "Verify that the API limits user results correctly when 'limit' parameter is used.")
    public void verifyGetAllUsersWithLimit() {

        int limit = Integer.parseInt(configReader.getProperty("limit"));
        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("limit", limit)
                        .when()
                        .get(Routes.get_allUsers_Limit)
                        .then()
                        .statusCode(200)
                        //.body("size()",equalTo(limit))
                        .extract().response();

        int limitedUsers = response.jsonPath().getList("$").size();
        Assert.assertTrue(limitedUsers <= 5, "More users returned than expected!");
    }

    /**
     *  Test 3: Get all users sorted
     * Ensures sorting functionality (asc/desc) returns a valid list.
     */
    @Test(priority = 3, description = "Verify that users can be retrieved in sorted order (descending).")
    public void verifyGetAllUsersSortedDescending() {
        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("order", "desc")
                        .when()
                        .get(Routes.get_allUsers_Sorted)
                        .then()
                        .statusCode(200)
                        .extract().response();

                List<Integer> userIds = response.jsonPath().getList("id",Integer.class);
                assertThat(isSortedDescending(userIds),is(true));
    }

    /**
     *  Test 4: Get all users sorted
     * Ensures sorting functionality (asc/desc) returns a valid list.
     */
    @Test(priority = 4, description = "Verify that users can be retrieved in sorted order (ascending ).")
    public void verifyGetAllUsersSortedAscending() {
        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("order", "asc")
                        .when()
                        .get(Routes.get_allUsers_Sorted)
                        .then()
                        .statusCode(200)
                        .extract().response();

        List<Integer> userIds = response.jsonPath().getList("id",Integer.class);
        assertThat(isSortedAscending(userIds),is(true));
    }

/*    Test 5: Get user by ID
    Confirms API returns correct user details for a given ID.*/

    @Test(priority = 5, description = "Verify that user details are returned correctly for a valid user ID.")
    public void verifyGetUserById() {

       // Read product ID dynamically from config.properties
        int userId = Integer.parseInt(configReader.getProperty("userId"));
        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("id", userId)
                        .when()
                        .get(Routes.get_userById)
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();

        String username = response.jsonPath().getString("username");
        Assert.assertNotNull(username, "Username not found for user ID 1");
    }


    /**
     * Test 6: Create a new user (POST)
     **/
    @Test(priority = 6)
    public void verifyCreateNewUser() {
        Users newUser = Payload.usersPayload();

        Response response =
                given()
                        .spec(requestSpec)
                        .body(newUser)
                        .when()
                        .post(Routes.post_newUser)
                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().response();

        // Verify ID is returned
        int id = response.jsonPath().getInt("id");
        Assert.assertTrue(id > 0, "User ID was not generated!");
    }

    /**
     *  Test 7: Update user via PUT
     * Replaces entire user object with new payload data.
     */
    @Test(priority = 7, description = "Verify that an existing user can be fully updated via PUT /users/{id}.")
    public void verifyUpdateUserById() {
        Users updateUser = Payload.usersPayload();
        int userId = Integer.parseInt(configReader.getProperty("userId"));

        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("id", userId)
                        .body(updateUser)
                        .when()
                        .put(Routes.put_updateUserById)
                        .then()
                        .statusCode(200)
                        .extract().response();

        Assert.assertEquals(response.jsonPath().getString("username"), updateUser.getUsername(),
                "Username not updated correctly!");
    }



    /**
     *  Test 8: Delete user by ID
     * Ensures that a user can be successfully deleted.
     */
    @Test(priority = 8, description = "Verify that a user can be deleted successfully using DELETE /users/{id}.")
    public void verifyDeleteUserById() {

        int userId = Integer.parseInt(configReader.getProperty("userId"));

        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("id", userId)
                        .when()
                        .delete(Routes.delete_userById)
                        .then()
                        .statusCode(200)
                        .extract().response();

        Assert.assertTrue(response.asString().contains("id"), "User delete response invalid!");
    }




}
