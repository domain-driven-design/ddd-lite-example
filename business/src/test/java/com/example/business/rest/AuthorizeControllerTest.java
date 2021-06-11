package com.example.business.rest;

import com.example.TestBase;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class AuthorizeControllerTest extends TestBase {

    @Test
    void should_login_in() {
        User user = this.prepareUser("test1", "test1@test.com", "password");
        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("email", "test1@test.com");
                        put("password", "password");
                    }
                })
                .when()
                .post("/authorizes");
        response.then().statusCode(201)
                .body("userId", is(user.getId()))
                .body("token", isA(String.class))
                .body("expire", isA(Integer.class));
    }
}
