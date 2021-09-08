package com.example;

import com.example.config.ResetDbListener;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import com.example.domain.user.model.User;
import com.example.domain.user.service.UserService;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {BusinessTestApplication.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        ResetDbListener.class,
        SqlScriptsTestExecutionListener.class,
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class TestBase {
    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizeService authorizeService;

    @BeforeEach
    public void setUp() {
        System.out.println("port:" + port);

        RestAssured.port = port;
        RestAssured.basePath = "/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public User prepareUser(String name, String email) {
        return userService.create(name, email, "password");
    }

    public RequestSpecification givenWithAuthorize(User user) {
        Authorize authorize = authorizeService.create(user, "password");
        return given()
                .header("Authorization", "Bearer " + authorize.getId())
                .contentType("application/json");
    }


    public RequestSpecification givenWithAuthorize(User user, String groupId) {
        return givenWithAuthorize(user)
                .header("Group-ID", groupId);
    }

    public RequestSpecification givenDefault() {
        return given()
                .contentType("application/json");
    }

    public RequestSpecification givenDefault(String groupId) {
        return givenDefault()
                .header("Group-ID", groupId);
    }
}
