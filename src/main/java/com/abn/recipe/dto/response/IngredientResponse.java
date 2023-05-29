package com.abn.recipe.dto.response;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.utilities.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientResponse extends BaseResponse{
    @Schema(title = "The id of the ingredient", example = "18")
    private long id;
    @Schema(title = "The name of the ingredient", example = "Milk")
    private String ingName;

    public static IngredientResponse mapIngredientEntityToResponse(IngredientEntity entity){
        IngredientResponse ingredientResponse = new IngredientResponse();
        BeanUtils.copyProperties(entity, ingredientResponse, "ingredients");
        return ingredientResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientResponse that = (IngredientResponse) o;
        return id == that.id || Objects.equals(ingName, that.ingName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingName);
    }
}
