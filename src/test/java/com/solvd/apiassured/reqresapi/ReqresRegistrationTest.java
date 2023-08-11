package com.solvd.apiassured.reqresapi;

import com.solvd.apiassured.Specification;
import com.solvd.apiassured.api.Registration;
import com.solvd.apiassured.api.SuccessfulRegistration;
import com.solvd.apiassured.api.UserData;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresRegistrationTest {
    private static final String URL = "https://reqres.in/";

    @Test
    public void verifySuccessfulRegistrationTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        SuccessfulRegistration successfulRegistration = given()
                .body(user)
                .when()
                .post(URL + "api/register")
                .then().log().all()
                .extract().as(SuccessfulRegistration.class);

        Assert.assertNotNull(successfulRegistration.getId());
        Assert.assertNotNull(successfulRegistration.getToken());

        Assert.assertEquals(id, successfulRegistration.getId());
        Assert.assertEquals(token, successfulRegistration.getToken());
    }

    @Test
    public void verifyRegisteredUserExistTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        UserData userData = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "api/users/4")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", UserData.class);
        Assert.assertNotNull(userData.getId());
        Assert.assertEquals(userData.getId(), 4, "Id doesn't match");
    }

    @Test
    public void verifyUserNotFoundTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(404));

        given()
                .when()
                .get(URL + "api/users/23")
                .then().log().all();
    }

    @Test
    public void verifyUsersOnPageTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));
        List<UserData> userData = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "api/users?page=1")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        Integer pageCount = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "api/users?page=1")
                .then().log().all()
                .extract().body().jsonPath().get("per_page");

        Assert.assertEquals(pageCount, (userData.size()), "The quantity users on the page isn't equal");
    }
}
