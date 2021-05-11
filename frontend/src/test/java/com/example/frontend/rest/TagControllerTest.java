package com.example.frontend.rest;

import com.example.TestBase;
import com.example.domain.article.model.Tag;
import com.example.domain.article.service.TagService;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class TagControllerTest extends TestBase {

    private Authorize authorize;

    @Autowired
    private TagService tagService;

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

    @Test
    void get_tag_detail() {
        Tag testTag = tagService.create("testTag", authorize.getUserId());
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/tags/" + testTag.getId());
        response.then().statusCode(200)
                .body("id", is(testTag.getId()))
                .body("name", is(testTag.getName()));
                // TODO 时间格式精确度不一致
//                .body("createdAt", is(testTag.getCreatedAt()))
//                .body("updatedAt", is(testTag.getUpdatedAt()));
    }


    @Test
    void should_get_tags() {
        Tag testTag1 = tagService.create("testTag1", authorize.getUserId());
        Tag testTag2 = tagService.create("testTag2", authorize.getUserId());

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/tags");
        response.then().statusCode(200)
                .body("$.size()", is(2))
                .body("name", hasItems("testTag1", "testTag2"));
    }
}
