package com.example.frontend.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class TagControllerTest extends TestBase {

    private Authorize authorize;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        User user = this.prepareUser("test1", "test1@test.com", "password");
        authorize = this.prepareAuthorize(user);
    }

    @Test
    void should_create_tag() {
        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("name", "testTag");
                    }
                })
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .post("/tags");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("name", is("testTag"));
    }
}
