package com.solvd.apiassured.api;

public class UnsuccessfulLogin extends Registration {
    private String errorMessage;

    public UnsuccessfulLogin() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
