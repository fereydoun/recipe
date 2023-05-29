package com.abn.recipe.dto.search.advance.recipe;

import com.abn.recipe.dto.search.advance.DataOption;
import com.abn.recipe.dto.search.advance.SearchCriteria;
import com.abn.recipe.utilities.annotations.EnumPattern;
import com.abn.recipe.utilities.constants.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchRequest{

    @Schema(title = "criteria list: [{field,value,operation}] ", example = "ingName")
    private List<RecipeSearchCriteria> criteriaList;

    @EnumPattern(enumClazz = DataOption.class, message = "{" + MessageConstants.GENERAL_DATA_OPTION_INVALID + "}")
    @Schema(title = "all or one criteria is enough for filtering", allowableValues = {"all", "any"}, example = "all")
    private String dataOption;
}
