package com.abn.recipe.dto.request;

import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.enums.RecipeType;
import com.abn.recipe.utilities.annotations.EnumPattern;
import com.abn.recipe.utilities.constants.MessageConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RecipeRequest extends BaseRequest {

    @JsonIgnore
    private long id;

    @NotBlank(message = "{" + MessageConstants.RECIPE_NAME_NOTBLANK + "}")
    @Size(max = 255, message = "{" + MessageConstants.RECIPE_NAME_SIZE + "}")
    @Schema(title = "The name of the recipe", example = "Pizza")
    private String name;

    @NotNull(message = "{" + MessageConstants.RECIPE_TYPE_NOTBLANK + "}")
    @EnumPattern(enumClazz = RecipeType.class, message = "{" + MessageConstants.RECIPE_TYPE_INVALID + "}")
    @Schema(title = "The type of the recipe", allowableValues = {"GENERAL", "VEGETARIAN"})
    private String type;

    @Schema(title = "instructions of the recipe")
    private String instructions;

    @NotNull(message = "{" + MessageConstants.RECIPE_SERVINGS_NOTBLANK + "}")
    @Schema(title = "How many people does the recipe serve?", example = "3")
    private Integer servings;

    @Schema(title = "The ingredient id is used for the recipe", example = "[10,11,15]")
    private List<Long> ingredientIds;


    public static RecipeEntity mapRequestToRecipeEntity(RecipeRequest request) {
        RecipeEntity recipeEntity = new RecipeEntity();
        BeanUtils.copyProperties(request, recipeEntity, "");
        return recipeEntity;
    }
}
