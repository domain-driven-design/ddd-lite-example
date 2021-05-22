package com.example.admin.rest;

import com.example.TestBase;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.isA;

class AdminAuthorizeControllerTest extends TestBase {

    @Test
    void should_log_in() {
        // Given
        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("name", "admin");
                        put("password", "password");
                    }
                })
                .when()
                .post("/authorizes/admin");
        response.then().statusCode(201)
                .body("userId", isA(String.class))
                .body("token", isA(String.class));

    }
}
