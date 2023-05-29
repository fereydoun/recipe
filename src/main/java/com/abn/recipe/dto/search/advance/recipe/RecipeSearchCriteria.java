package com.abn.recipe.dto.search.advance.recipe;

import com.abn.recipe.dto.search.advance.SearchCriteria;
import com.abn.recipe.utilities.annotations.EnumPattern;
import com.abn.recipe.utilities.constants.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeSearchCriteria extends SearchCriteria {

    @NotBlank(message = "{" + MessageConstants.GENERAL_FIELD_NAME_IS_REQUIRED + "}")
    @EnumPattern(enumClazz = RecipeFieldName.class, message = "{" + MessageConstants.GENERAL_SEARCH_FIELD_NAME_INVALID + "}")
    @Schema(title = "field name for search", description = "field names are: " +
            " (name," +
            " type," +
            " instructions," +
            " servings," +
            " ingName)",
            example = "name")
    private String filterKey;
}
