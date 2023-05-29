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
public class RecipeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private StringBuilder getBaseUrl(){
        return new StringBuilder("http://localhost").append(":").append(port);
    }
    @Test
    @DisplayName("Create a new recipe")
    public void createRecipe() {
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/recipe/create");
        String body = """
                {
                	"name": "recipe test1",
                     "servings": 10,
                     "type": "GENERAL",
                     "instructions": "all",
                     "ingredientIds": [1,2,3]
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
    @DisplayName("Edit an recipe by id and data")
    public void updateRecipe() {
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/recipe/update/{id}");
        String body = """
                {
                	"name": "recipe test edited",
                     "servings": 3,
                     "type": "GENERAL",
                     "instructions": "for general instructions",
                     "ingredientIds": [1,2]
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
                .body("name", notNullValue());

    }

    @Test
    @DisplayName("Delete an recipe by id")
    public void deleteRecipe() {
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/recipe/delete/{id}");
        var response = given()
                .when()
                .delete(url.toString().replace("{id}", "1"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Get a recipe by id")
    public void getRecipe() {
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/recipe/get/{id}");
        var response = given()
                .when()
                .get(url.toString().replace("{id}", "1"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200)
                .body("id", notNullValue());

    }

    @Test
    @DisplayName("Get  all recipe as Page by Page(Page start from Zero)")
    public void getRecipeList() {
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/recipe/get/page/{page}/pageSize/{pageSize}");
        var response = given()
                .when()
                .get(url.toString().replace("{page}", "0").replace("{pageSize}", "10"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200)
                .body("id", everyItem(notNullValue()));

    }

    @Test
    @DisplayName("Advance Search on recipes as Page by Page(Page start from Zero)")
    public void searchRecipe() {
        StringBuilder url = getBaseUrl();
        url.append("/api/v1/recipe/advance/search/page/{page}/pageSize/{pageSize}");
        String body = """
                {
                	 "criteriaList":[{
                         "filterKey": "name",
                         "value": "rec",
                         "operation": "cn"
                     },
                     {
                         "filterKey": "type",
                         "value": "VEGETARIAN",
                         "operation": "ne"
                     },
                     {
                         "filterKey": "servings",
                         "value": "3",
                         "operation": "ge"
                     }
                     ],
                     "dataOption": "all"
                }
                """;
        var response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(url.toString().replace("{page}", "0").replace("{pageSize}", "10"))
                .then();
        response.log().body();
        response.assertThat().statusCode(200)
                .body("id", everyItem(notNullValue()));

    }
}
