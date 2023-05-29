package com.abn.recipe.dto.search.advance;

import com.abn.recipe.dto.enums.RecipeType;
import com.abn.recipe.dto.search.advance.recipe.RecipeFieldName;
import com.abn.recipe.utilities.annotations.EnumPattern;
import com.abn.recipe.utilities.constants.MessageConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

    @NotBlank(message = "{" + MessageConstants.GENERAL_FIELD_NAME_IS_REQUIRED + "}")
    @Schema(title = "field name for search", example = "name")
    private String filterKey;

    @Schema(title = "data to search", example = "Milk")
    private Object value;
    @NotBlank(message = "{" + MessageConstants.GENERAL_OPERATION_IS_REQUIRED + "}")
    @EnumPattern(enumClazz = SearchOperation.class, message = "{" + MessageConstants.GENERAL_SEARCH_OPERATION_INVALID + "}")
    @Schema(title = "operation for doing search", allowableValues = {"cn", "nc", "eq", "ne", "bw", "bn", "ew", "en", "nu", "nn", "gt", "ge", "lt", "le" },
    description = "cn - contains | nc - does not contain | eq - equal | ne - notequal " +
            "| bw - begins with | bn - does not begin with | ew - ends with" +
            " | en - does not end with | nu - null | nn - not null | gt - greater than |" +
            " ge - greater than equal | lt - less than | le - less than equal ")
    private String operation;

    @JsonIgnore
    @Schema(hidden = true)
    private String dataOption;

    public SearchCriteria(String filterKey, String operation,
                          Object value){
        super();
        this.filterKey = filterKey;
        this.operation = operation;
        this.value = value;
    }
}
