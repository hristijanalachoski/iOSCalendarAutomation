package util;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class RestAssuredUtility {

    private final RequestSpecification request = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("Content Type", "application/json");

    @Step("Fetching '{path}' from Bored API")
    public String getActivityValue(String path) {
        JsonPath response = request.get(GlobalVariables.boredApiActivityEndpoint)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .jsonPath();

        GlobalVariables.response = response.prettify();
        System.out.println(GlobalVariables.response);
        return response.get(path).toString();
    }
}