package com.abn.recipe.controllers;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.dto.request.IngredientRequest;
import com.abn.recipe.dto.response.IngredientResponse;
import com.abn.recipe.services.IngredientService;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Ingredient Controller", description = "All ingredient APIs")
@RestController
@RequestMapping(value = "api/v1/ingredient")
@AllArgsConstructor
@Validated
public class IngredientController {

    private final IngredientService ingredientService;

    @Operation(method = "Create", description = "Create a new ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingredient created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(
            @Parameter(name = "Ingredient's Data", required = true) @Valid @RequestBody IngredientRequest request) {
        log.info("Creating the ingredient");
        IngredientEntity ingredientEntity = ingredientService.create(request);
        return IngredientResponse.mapIngredientEntityToResponse(ingredientEntity);
    }

    @Operation(method = "Edit by id", description = "Edit an ingredient by id and data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient edited"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping(value = "/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse updateIngredient(@Parameter(name = "ingredient id", required = true) @PathVariable(name = "id") Long id,
            @Parameter(name = "Ingredient's Data", required = true) @Valid @RequestBody IngredientRequest request) {
        log.info("Updating the ingredient");
        request.setId(id);
        IngredientEntity ingredientEntity = ingredientService.update(request);
        return IngredientResponse.mapIngredientEntityToResponse(ingredientEntity);
    }

    @Operation(method = "Delete by id", description = "Delete an ingredient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Ingredient not found by id")
    })
    @DeleteMapping(value = "/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteIngredient(@Parameter(name = "ingredient id", required = true) @PathVariable(name = "id") Long id) {
        log.info("Deleting the ingredient by id: {}", id);
        ingredientService.delete(id);
    }


    @Operation(method = "Get by id", description = "Get an ingredient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Ingredient not found by id")
    })
    @GetMapping(value = "/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse getIngredient(@Parameter(name = "Ingredient id", required = true) @PathVariable(name = "id") Long id) {
        log.info("Getting the ingredient by Id: {}", id);
        IngredientEntity ingredientEntity = ingredientService.findById(id);
        return IngredientResponse.mapIngredientEntityToResponse(ingredientEntity);

    }

    @Operation(method = "Get all", description = "Get  all ingredients as Page by Page(Page start from Zero)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
    })
    @GetMapping(value = "/get/page/{page}/pageSize/{pageSize}")
    @ResponseStatus(HttpStatus.OK)
    public List<IngredientResponse> getIngredientList( @PathVariable(name = "page") int page,
                                                       @PathVariable(name = "pageSize") int pageSize) {
        log.info("Getting the ingredients");
        List<IngredientEntity> list = ingredientService.list(page, pageSize);

        return list
                .stream()
                .map(IngredientResponse::mapIngredientEntityToResponse)
                .collect(Collectors.toList());
    }
}
