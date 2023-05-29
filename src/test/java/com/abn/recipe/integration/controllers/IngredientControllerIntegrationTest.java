package com.abn.recipe.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IngredientControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private StringBuilder getBaseUrl(){
        return new StringBuilder("http://localhost").append(":").append(port);
    }

    @Test
    @DisplayName("Create a new ingredient")
    public void createIngredient(){
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/ingredient/create");
        String body = """
                {
                	"ingName": "Milk"
                }
                """;
        var response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(url.toString())
                .then();
        response.log().body();
        response.assertThat().statusCode(201)
                .body("id", notNullValue())
                ;

    }


    @Test
    @DisplayName("Update an ingredient")
    public void updateIngredient(){
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/ingredient/update/{id}");
        String body = """
                {
                	"ingName": "Chicken"
                }
                """;

        var response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put(url.toString().replace("{id}", "1"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200)
                .body("ingName", notNullValue());
    }

    @Test
    @DisplayName("Delete an ingredient by id")
    public void deleteIngredient(){
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/ingredient/delete/{id}");
        var response = given()
                .when()
                .delete(url.toString().replace("{id}", "1"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Get an ingredient by id")
    public void getIngredient(){
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/ingredient/get/{id}");
        var response = given()
                .when()
                .get(url.toString().replace("{id}", "2"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Get  all ingredients as Page by Page(Page start from Zero)")
    public void getIngredientList(){
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/ingredient/get/page/{page}/pageSize/{pageSize}");
        var response = given()
                .when()
                .get(url.toString().replace("{page}", "0").replace("{pageSize}", "10"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200)
                .body("id", everyItem(notNullValue()));
    }
}
