package com.abn.recipe.dto.search.advance.recipe;

import com.abn.recipe.data.models.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeSearchUtil {

    public Specification<RecipeEntity> buildSpecification(List<RecipeSearchCriteria> criteriaList, String dataOption) {
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder(new ArrayList<>());
        if(criteriaList != null){
            criteriaList.forEach(x-> {x.setDataOption( dataOption);
                builder.with(x);
            });
        }

        return builder.build();
    }
}
