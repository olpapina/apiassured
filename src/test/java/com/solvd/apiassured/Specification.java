package com.solvd.apiassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {

    public static RequestSpecification requestSpecification(String url) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpecification(Integer statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .build();
    }
//
//    public static ResponseSpecification responseSpecificationCreated201() {
//        return new ResponseSpecBuilder()
//                .expectStatusCode(201)
//                .build();
//    }
//
//    public static ResponseSpecification responseSpecificationUpdated200() {
//        return new ResponseSpecBuilder()
//                .expectStatusCode(200)
//                .build();
//    }
//
//    public static ResponseSpecification responseSpecificationDelete204() {
//        return new ResponseSpecBuilder()
//                .expectStatusCode(204)
//                .build();
//    }
//
//    public static ResponseSpecification responseSpecificationNotFound404() {
//        return new ResponseSpecBuilder()
//                .expectStatusCode(404)
//                .build();
//    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}
