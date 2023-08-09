package com.solvd.apiassured.api;

public class Login extends Registration{
    private String token;

    public Login() {
    }

    public Login(String email, String password) {
        super(email, password);
    }

    public String getToken() {
        return token;
    }
}
