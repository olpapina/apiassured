package com.solvd.apiassured.githubapi;

import com.solvd.apiassured.Specification;
import com.solvd.apiassured.api.githubapi.Tree;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CommitTest {
    private static final String URL = "https://api.github.com/";
    private static final String owner = "opapina";
    private static final String repo = "hmsbase";

    @Test
    public void verifyCommentCommitCreateTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(201));

        String accessToken = "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";
        String commit = "2872669e269fe8d84617740f064ed3d0a953e739";

        Response response = given()
                .auth().oauth2(accessToken)
                .body("{\"body\":\"Great stuff\",\"path\":\"file1.txt\",\"position\":4,\"line\":1}")
                .when()
                .post(URL + String.format("repos/%s/%s/commits/%s/comments", owner, repo, commit))
                .then()
                .log().all()
                .assertThat()
                .body("position", equalTo(4))
                .body("body", equalTo("Great stuff"))
                .and()
                .extract()
                .response();

        JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchemaInClasspath("post_comment_rs.schema");
        response.then().assertThat().body(validator);
    }

    @Test
    public void verifyCommitListTest() {
        Specification.installSpecification(Specification.requestSpecification(URL),
                Specification.responseSpecification(200));

        Integer commitCountOnPage = 30;
        Integer commentQuantity = 11;

        Response response = given()
                .when()
                .get(URL + String.format("repos/%s/%s/commits", owner, repo))
                .then()
                .log().all()
                .extract()
                .response();

        List<Tree> trees = response.body().jsonPath().getList("commit.tree", Tree.class);
        List<Integer> commentCounts = response.body().jsonPath().getList("commit.comment_count", Integer.class);
        Integer commentCount = commentCounts.stream()
                .mapToInt(Integer::valueOf)
                .sum();

        JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchemaInClasspath("get_comments_rs.schema");
        response.then().assertThat().body(validator);

        SoftAssert sa = new SoftAssert();

        sa.assertEquals(trees.size(), Optional.of(commitCountOnPage), "Commit count doesn't match");
        trees.forEach(tree -> sa.assertTrue(tree.getUrl().contains(owner), "Other owner for this commit"));
        sa.assertEquals(commentCounts.size(), Optional.of(commitCountOnPage), "Commit count doesn't match");
        sa.assertEquals(commentCount, commentQuantity, "Comments quantity doesn't match");
    }
}
