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

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Response response0 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "any")
                .when()
                .get("/management/users");
        response0.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.name", hasItems("anyName", "anyName"))
                .body("content.email", hasItems("anyEmail0", "anyEmail1"));

        Response response1 = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .param("keyword", "Email0")
                .when()
                .get("/management/users");
        response1.then().statusCode(200)
                .body("content.size", is(1))
                .body("content.name", hasItems("anyName"))
                .body("content.email", hasItems("anyEmail0"))
                .body("content.status", hasItems(User.Status.NORMAL.name()));
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
    void should_create_user() {
        String name = "test";
        String email = "test@email.com";
        String password = "password";

        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("name", name);
                        put("email", email);
                        put("password", password);
                    }
                })
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .post("/management/users");
        response.then().statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is(name))
                .body("email", is(email));

        boolean exists = userRepository.exists(Example.of(User.builder().name(name).email(email).build()));
        assertTrue(exists);
    }

    @Test
    void should_update_user_status() {
        User user = userService.create("anyName", "anyEmail0", "anyPassword");

        given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("status", User.Status.FROZEN);
                    }
                })
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .put("/management/users/" + user.getId() + "/status")
                .then().statusCode(200)
                .body("id", is(user.getId()))
                .body("status", is(User.Status.FROZEN.name()));

        User frozenUser = userRepository.findById(user.getId()).get();
        assertThat(frozenUser.getStatus(), is(User.Status.FROZEN));


        given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("status", User.Status.NORMAL);
                    }
                })
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .put("/management/users/" + user.getId() + "/status")
                .then().statusCode(200)
                .body("id", is(user.getId()))
                .body("status", is(User.Status.NORMAL.name()));

        User normalUser = userRepository.findById(user.getId()).get();
        assertThat(normalUser.getStatus(), is(User.Status.NORMAL));

    }

}
