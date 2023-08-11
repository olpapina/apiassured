package com.solvd.apiassured.reqresapi;

import com.solvd.apiassured.Specification;
import com.solvd.apiassured.api.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.restassured.RestAssured.*;

public class ReqResUserJobTest {
    private static final String URL = "https://reqres.in/";
    private String createdID;

    @Test
    public void verifyCreateUserTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(201));

        Type typeId = Integer.class;
        String regex = "(.{10})$";
        String regex1 = "(.{8})$";
        String currentDateTime = LocalDateTime.now(ZoneOffset.UTC).toString().replaceAll(regex,"");


        UserJob userJob = new UserJob("olga", "tester");
        CreatedUserJob createdUser = given()
                .body(userJob)
                .when()
                .post(URL + "api/users")
                .then().log().all()
                .extract().as(CreatedUserJob.class);

        Assert.assertNotNull(createdUser.getId());
        Assert.assertNotNull(createdUser.getCreatedAt());

        Assert.assertEquals(createdUser.getId().getClass(), typeId, "Id is not Integer");
        Assert.assertEquals(createdUser.getCreatedAt().replaceAll(regex1,""), currentDateTime, "Date doesn't match today");
        createdID = Integer.toString(createdUser.getId());
    }

    @Test
    public void verifyUpdateUserTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        String newName = "Marya";
        String newJob = "Doctor";

        String regex = "(.{11})$";
        String regex1 = "(.{8})$";
        String currentDateTime = Clock.systemUTC().instant().toString().replaceAll(regex,"");

        UserJob userJob = new UserJob(newName, newJob);
        UpdatedUserJob updatedUser = given()
                .body(userJob)
                .when()
                .put(URL + "api/users/" + createdID)
                .then().log().all()
                .extract().as(UpdatedUserJob.class);

        Assert.assertNotNull(updatedUser.getUpdatedAt());

        Assert.assertEquals(updatedUser.getName(), newName, "Name isn't changed");
        Assert.assertEquals(updatedUser.getJob(), newJob, "Job isn't changed");
        Assert.assertEquals(updatedUser.getUpdatedAt().replaceAll(regex1, ""), currentDateTime, "Date doesn't match today");
    }

    @Test
    public void verifyDeletedUserTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(204));

        given()
                .when()
                .delete(URL + "api/users/" + createdID)
                .then().log().all()
                .assertThat()
                .contentType("");
    }
}
