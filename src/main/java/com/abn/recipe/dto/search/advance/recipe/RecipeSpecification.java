package com.abn.recipe.dto.search.advance.recipe;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.search.advance.SearchCriteria;
import com.abn.recipe.dto.search.advance.SearchOperation;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.Optional;

import static com.abn.recipe.dto.search.advance.SpecificationConstants.FIELD_INGREDIENT_NAME;

@AllArgsConstructor
public class RecipeSpecification  implements Specification<RecipeEntity> {

    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        String stringValue = searchCriteria.getValue().toString().toLowerCase();
        switch(Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))){
            case CONTAINS:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.like(criteriaBuilder.lower(ingredientJoin(root).<String>get(searchCriteria.getFilterKey())), "%" + stringValue + "%");
                }
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + stringValue + "%");

            case DOES_NOT_CONTAIN:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.notLike(criteriaBuilder.lower(ingredientJoin(root).<String>get(searchCriteria.getFilterKey())), "%" + stringValue + "%");
                }
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + stringValue + "%");

            case BEGINS_WITH:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.like(criteriaBuilder.lower(ingredientJoin(root).<String>get(searchCriteria.getFilterKey())), stringValue + "%");
                }
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), stringValue + "%");

            case DOES_NOT_BEGIN_WITH:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.notLike(criteriaBuilder.lower(ingredientJoin(root).<String>get(searchCriteria.getFilterKey())), stringValue + "%");
                }
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), stringValue + "%");

            case ENDS_WITH:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.like(criteriaBuilder.lower(ingredientJoin(root).<String>get(searchCriteria.getFilterKey())), "%" + stringValue);
                }
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + stringValue);

            case DOES_NOT_END_WITH:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.notLike(criteriaBuilder.lower(ingredientJoin(root).<String>get(searchCriteria.getFilterKey())), "%" + stringValue);
                }
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + stringValue);

            case EQUAL:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    System.out.println(searchCriteria.getValue());
                    return criteriaBuilder.equal(ingredientJoin(root).<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue());
                }
                return criteriaBuilder.equal(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NOT_EQUAL:
                if(isIngredientEntityField(searchCriteria.getFilterKey())){
                    return criteriaBuilder.notEqual(ingredientJoin(root).<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue() );
                }
                return criteriaBuilder.notEqual(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NULL:
                return criteriaBuilder.isNull(root.get(searchCriteria.getFilterKey()));

            case NOT_NULL:
                return criteriaBuilder.isNotNull(root.get(searchCriteria.getFilterKey()));

            case GREATER_THAN:
                return criteriaBuilder.greaterThan(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case GREATER_THAN_EQUAL:
                return criteriaBuilder.greaterThanOrEqualTo(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case LESS_THAN:
                return criteriaBuilder.lessThan(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case LESS_THAN_EQUAL:
                return criteriaBuilder.lessThanOrEqualTo(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
        }

        return null;
    }

    private Join<RecipeEntity, IngredientEntity> ingredientJoin(Root<RecipeEntity> root){
        return root.join("ingredients");

    }

    private boolean isIngredientEntityField(final String filterKey){

        if (!StringUtils.isBlank(filterKey) && filterKey.equals(FIELD_INGREDIENT_NAME))
            return true;
        return false;
    }
}
