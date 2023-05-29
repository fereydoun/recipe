package com.abn.recipe.dto.search.advance.recipe;

import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.search.advance.SearchCriteria;
import com.abn.recipe.dto.search.advance.SearchOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@AllArgsConstructor
public class RecipeSpecificationBuilder {

    private final List<SearchCriteria> params;

    public final RecipeSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final RecipeSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Specification<RecipeEntity> build() {
        if (params.isEmpty())
            return null;

        Specification<RecipeEntity> specification = new RecipeSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            SearchCriteria criteria = params.get(i);
            specification = SearchOperation.getDataOption(criteria.getDataOption()) == SearchOperation.ALL
                    ? Specification.where(specification).and(new RecipeSpecification(criteria))
                    : Specification.where(specification).or(new RecipeSpecification(criteria));
        }

        return specification;
    }
}
