package com.example.admin.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.service.UserService;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

class UserManagementControllerTest extends TestBase {

    private Authorize authorize;

    @Autowired
    private UserService userService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        authorize = this.prepareAuthorize();
    }

    @Test
    void should_get_user_list() {
        // Given
        userService.create("anyName", "anyEmail0", "anyPassword");
        userService.create("anyName", "anyEmail1", "anyPassword");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/management/users");
        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.name", hasItems("anyName", "anyName"))
                .body("content.email", hasItems("anyEmail0", "anyEmail1"));
    }
}
