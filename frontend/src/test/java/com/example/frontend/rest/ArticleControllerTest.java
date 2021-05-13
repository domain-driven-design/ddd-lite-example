package com.example.frontend.rest;

import com.example.TestBase;
import com.example.domain.article.model.Article;
import com.example.domain.article.model.Tag;
import com.example.domain.article.service.ArticleService;
import com.example.domain.article.service.TagService;
import com.example.domain.auth.model.Authorize;
import com.example.domain.user.model.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

class ArticleControllerTest extends TestBase {
    private Authorize authorize;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        User user = this.prepareUser("test1", "test1@test.com", "password");
        authorize = this.prepareAuthorize(user);
    }

    @Test
    void should_create_article() {
        Tag testTag = tagService.create("testTag", authorize.getUserId());
        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("title", "testArticle");
                        put("content", "testArticleContent");
                        put("tagIds", Arrays.asList(testTag.getId()));
                    }
                })
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .post("/articles");
        response.then().statusCode(201)
                .body("id", isA(String.class))
                .body("title", is("testArticle"))
                .body("content", is("testArticleContent"));

        // TODO 创建完成后，需要到数据库查询确认

    }

    @Test
    void should_get_article_detail() {
        Article article = articleService.create("testArticle", "testArticleContent", authorize.getUserId());
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/articles/" + article.getId());
        response.then().statusCode(200)
                .body("id", isA(String.class))
                .body("title", is("testArticle"))
                .body("content", is("testArticleContent"));
    }

    @Test
    void should_get_articles() {
        Article article1 = articleService.create("testArticle1",
                "testArticleContent1", authorize.getUserId()
        );
        Article article2 = articleService.create(
                "testArticle2", "testArticleContent2", authorize.getUserId()
        );
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/articles");
        response.then().statusCode(200)
                .body("content.size", is(2))
                .body("content.title", hasItems("testArticle1", "testArticle1"))
                .body("content.content", hasItems("testArticleContent1", "testArticleContent2"));
    }

    @Test
    void should_add_tags_for_article() {
        Tag testTag = tagService.create("testTag", authorize.getUserId());
        Article article = articleService.create("testArticle",
                "testArticleContent", authorize.getUserId()
        );
        Response response = given()
                .contentType("application/json")
                .body(new HashMap<String, Object>() {
                    {
                        put("tagId", testTag.getId());
                    }
                })
                // TODO 测试中Authorization统一设置
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .post("/articles/" + article.getId() + "/tags");
        response.then().statusCode(201)
                .body("tagId", isA(String.class))
                .body("articleId", is(article.getId()))
                .body("createdAt", notNullValue());

    }

    @Test
    void should_get_tags_for_article() {
        Tag testTag = tagService.create("testTag", authorize.getUserId());
        Article article = articleService.create("testArticle",
                "testArticleContent", authorize.getUserId()
        );
        articleService.addTag(article.getId(), testTag.getId(), authorize.getUserId());

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authorize.getId())
                .when()
                .get("/articles/" + article.getId() + "/tags");
        response.then().statusCode(200)
                .body("$.size()", is(1))
                .body("name", hasItem("testTag"));
    }
}
