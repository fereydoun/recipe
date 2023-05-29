package com.abn.recipe.controllers;

import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.request.RecipeRequest;
import com.abn.recipe.dto.response.RecipeResponse;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchRequest;
import com.abn.recipe.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Recipe Controller", description = "All Recipe APIs")
@RestController
@RequestMapping(value = "api/v1/recipe")
@AllArgsConstructor
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    @Operation(method = "Create", description = "Create a new recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse createRecipe(
            @Parameter(name = "Recipe's Data", required = true) @Valid @RequestBody RecipeRequest request) {

        log.info("Creating the recipe");
        RecipeEntity recipeEntity = recipeService.create(request);
        return RecipeResponse.mapRecipeEntityToResponse(recipeEntity);
    }

    @Operation(method = "Edit by id", description = "Edit an recipe by id and data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe edited"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping(value = "/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeResponse updateRecipe(@Parameter(name = "recipe id", required = true) @PathVariable(name = "id") Long id,
                                               @Parameter(name = "Recipe's Data", required = true) @Valid @RequestBody RecipeRequest request) {
        log.info("Updating the recipe");
        request.setId(id);
        RecipeEntity recipeEntity = recipeService.update(request);
        return RecipeResponse.mapRecipeEntityToResponse(recipeEntity);
    }

    @Operation(method = "Delete by id", description = "Delete an recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Recipe not found by id")
    })
    @DeleteMapping(value = "/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecipe(@Parameter(name = "recipe id", required = true) @PathVariable(name = "id") Long id) {
        log.info("Deleting the recipe by id: {}", id);
        recipeService.delete(id);
    }


    @Operation(method = "Get by id", description = "Get a recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Recipe not found by id")
    })
    @GetMapping(value = "/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeResponse getRecipe(@Parameter(name = "Recipe id", required = true) @PathVariable(name = "id") Long id) {
        log.info("Getting the recipe by Id: {}", id);
        RecipeEntity recipeEntity = recipeService.findById(id);
        return RecipeResponse.mapRecipeEntityToResponse(recipeEntity);

    }

    @Operation(method = "Get all", description = "Get  all recipe as Page by Page(Page start from Zero)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
    })
    @GetMapping(value = "/get/page/{page}/pageSize/{pageSize}")
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeResponse> getRecipeList(@PathVariable(name = "page") int page,
                                                      @PathVariable(name = "pageSize") int pageSize) {
        log.info("Getting the recipes");
        List<RecipeEntity> list = recipeService.list(page, pageSize);

        return list
                .stream()
                .map(RecipeResponse::mapRecipeEntityToResponse)
                .collect(Collectors.toList());
    }

    @Operation(method = "Advance Search", description = "Advance Search on recipes as Page by Page(Page start from Zero)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request")

    })
    @PostMapping(value = "/advance/search/page/{page}/pageSize/{pageSize}")
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeResponse> searchRecipe(@PathVariable(name = "page") int page,
                                             @PathVariable(name = "pageSize") int pageSize,
                                             @Parameter(name = "Criteria Data") @RequestBody @Valid RecipeSearchRequest criteria
                                             ) {
        log.info("Advance Searching on recipe");
        Optional<List<RecipeEntity>> recipes = Optional.ofNullable(recipeService.advanceSearch(page, pageSize, criteria));

        return recipes.isPresent() ? recipes
                .get()
                .stream()
                .map(RecipeResponse::mapRecipeEntityToResponse)
                .collect(Collectors.toList()) : new ArrayList<>();
    }
}
