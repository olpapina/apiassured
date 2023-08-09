package com.solvd.apiassured.api;

public class CreatedUserJob extends UserJob {
    private Integer id;
    private String createdAt;

    public CreatedUserJob() {
    }

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
