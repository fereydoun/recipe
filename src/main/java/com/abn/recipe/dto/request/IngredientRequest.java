package com.abn.recipe.dto.request;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.utilities.constants.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IngredientRequest extends BaseRequest{

    @NotBlank(message = "{" + MessageConstants.INGREDIENT_NAME_NOTBLANK + "}")
    @Size(max = 255, message = "{" + MessageConstants.INGREDIENT_NAME_SIZE + "}")
    @Schema(title = "The name of the ingredient", example = "Low Fat Milk")
    private String ingName;

    public static IngredientEntity mapRequestToIngredientEntity(IngredientRequest request){
        IngredientEntity ingredientEntity = new IngredientEntity();
        BeanUtils.copyProperties(request, ingredientEntity, "");
        return ingredientEntity;
    }
}
