package com.example.admin.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

class AdminControllerTest extends TestBase {
    private Authorize authorize;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        authorize = this.prepareAuthorize();
    }

    @Test
    void should_reset_password() {
        String newPassword = "newPassword";

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .body(new HashMap<String, Object>() {
                    {
                        put("password", newPassword);
                    }
                })
                .when()
                .put("/admins/password")
                .then().statusCode(200);

        given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("name", "admin");
                        put("password", newPassword);
                    }
                })
                .when()
                .post("/authorizes/admin")
                .then().statusCode(201);
    }

}
