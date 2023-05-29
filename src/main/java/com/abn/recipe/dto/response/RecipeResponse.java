package com.abn.recipe.dto.response;

import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.enums.RecipeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeResponse extends BaseResponse {
    @Schema(title = "The id of the recipe", example = "18")
    private long id;
    @Schema(title = "The name of the recipe", example = "Pizza")
    private String name;

    @Schema(title = "The instructions of the recipe")
    private String instructions;

    @Schema(title = "The type of the recipe", allowableValues = {"GENERAL", "VEGETARIAN"})
    private String type;

    @Schema(title = "How many people does the recipe serve?", example = "3")
    private Integer servings;

    @Schema(title = "The ingredients of the recipe")
    private Set<IngredientResponse> ingredients;

    public static RecipeResponse mapRecipeEntityToResponse(RecipeEntity entity) {
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(entity, recipeResponse, "ingredients");
        if (entity.getIngredients() != null) {
            recipeResponse.setIngredients(new HashSet<>());
            recipeResponse.getIngredients().addAll(entity.getIngredients()
                    .stream()
                    .map(IngredientResponse::mapIngredientEntityToResponse)
                    .collect(Collectors.toSet()));
        }
        return recipeResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse that = (RecipeResponse) o;
        return id == that.id || Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
