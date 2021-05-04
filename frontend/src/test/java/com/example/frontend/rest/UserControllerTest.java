package com.example.frontend.rest;

import com.example.TestBase;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class UserControllerTest extends TestBase {

    @Test
    void should_register_a_user() {
        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("name", "test1");
                        put("email", "test1@email.com");
                        put("password", "password");
                    }
                })
                .when()
                .post("/users");
        response.then().statusCode(201)
                .body("name", is("test1"))
                .body("id", isA(String.class))
                .body("email", is("test1"))
                .body("name", is("test1"));
    }
}
