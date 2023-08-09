package com.solvd.apiassured;

import com.solvd.apiassured.api.Registration;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class ReqresValidateResponseTest {
    private static final String URL = "https://reqres.in/";

    @Test
    public void verifyRegistrationResponseValidationTest() {

        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Registration user = new Registration("eve.holt@reqres.in", "pistol");

        Response response = given()
                .body(user)
                .when()
                .post(URL + "api/register")
                .then()
                .log().all()
                .extract()
                .response();

        JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchemaInClasspath("post_success_registration_rs.schema");
        response.then().assertThat().body(validator);
    }

    @Test
    public void verifyUserListResponseValidationTest() {

        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        Response response = given()
                .when()
                .get(URL + "api/users?page=2")
                .then()
                .log().all()
                .extract()
                .response();

        JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchemaInClasspath("get_user_list_rs.schema");
        response.then().assertThat().body(validator);
    }

    @Test
    public void verifyDelayedResponseValidationTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        Response response = given()
                .when()
                .get(URL + "api/users?delay=3")
                .then()
                .log().all()
                .extract()
                .response();

        JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchemaInClasspath("get_user_list_rs.schema");
        response.then().assertThat().body(validator);
    }
}
