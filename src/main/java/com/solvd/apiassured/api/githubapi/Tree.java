package com.solvd.apiassured.api.githubapi;

public class Tree {
    private String url;
    private String sha;

    public Tree(String url, String sha) {
        this.url = url;
        this.sha = sha;
    }

    public Tree() {
    }

    public String getUrl() {
        return url;
    }

    public String getSha() {
        return sha;
    }
}
