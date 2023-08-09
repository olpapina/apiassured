package com.solvd.apiassured;

import com.solvd.apiassured.api.Login;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ReqresLoginTest {
    private static final String URL = "https://reqres.in/";

    @Test
    public void verifyLoginSuccessfulTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        String token = "QpwL5tke4Pnpja7X4";

        Login login = new Login("eve.holt@reqres.in", "cityslicka");
        Login successfulLogin = given()
                .body(login)
                .when()
                .post(URL + "api/login")
                .then().log().all()
                .extract().as(Login.class);

        Assert.assertNotNull(successfulLogin.getToken());
        Assert.assertEquals(token, successfulLogin.getToken());
    }

    @Test
    public void verifyLoginUnsuccessfulTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(400));

        String error = "Missing password";

        Login login = new Login("eve.holt@reqres.in", "");
        String unsuccessfulLogin = given()
                .body(login)
                .when()
                .post(URL + "api/login")
                .then().log().all()
                .extract().body().jsonPath().getJsonObject("error");

        Assert.assertEquals(error, unsuccessfulLogin, "Error message is not valid");
    }
}
