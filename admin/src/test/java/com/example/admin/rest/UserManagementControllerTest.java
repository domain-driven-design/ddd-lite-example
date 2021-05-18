package com.example.admin.rest;

import com.example.TestBase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import com.example.domain.user.service.UserService;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class UserManagementControllerTest extends TestBase {

    private Authorize authorize;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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


    @Test
    void should_get_user_detail() {
        // Given
        User user = userService.create("anyName", "anyEmail0", "anyPassword");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/management/users/" + user.getId());
        response.then().statusCode(200)
                .body("id", is(user.getId()))
                .body("name", is(user.getName()))
                .body("email", is(user.getEmail()));
    }

    @Test
    void should_create_admin() {
        // Given
        String name = "adminName";
        String password = "password";

        long originalCount = userRepository.count(Example.of(User.builder().role(User.UserRole.ADMIN).build()));

        // When
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .body(new HashMap<String, Object>() {
                    {
                        put("name", name);
                        put("password", password);
                    }
                })
                .post("/management/users/admins");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("name", is(name))
                .body("role", is(User.UserRole.ADMIN.name()));

        long finishCount = userRepository.count(Example.of(User.builder().role(User.UserRole.ADMIN).build()));

        assertThat(finishCount - originalCount, is(1L));
    }
}
