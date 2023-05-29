package com.abn.recipe.services;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.data.repositories.RecipeRepository;
import com.abn.recipe.dto.request.RecipeRequest;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchRequest;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchUtil;
import com.abn.recipe.exceptions.NotFoundException;
import com.abn.recipe.utilities.MessageHandler;
import com.abn.recipe.utilities.constants.MessageConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class RecipeService extends BaseService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;

    private final MessageHandler messageHandler;

    private final RecipeSearchUtil recipeSearchUtil;

    public RecipeEntity create(RecipeRequest request) {
        Set<IngredientEntity> ingredients = Optional.ofNullable(request.getIngredientIds())
                .map(ingredientService::getIngredientsByIds)
                .orElse(null);

        RecipeEntity recipe = RecipeRequest.mapRequestToRecipeEntity(request);
        recipe.setIngredients(ingredients);

        return recipeRepository.save(recipe);
    }

    public RecipeEntity update(RecipeRequest request) {
        RecipeEntity recipe = this.findById(request.getId());
        recipe.getIngredients().clear();
        Set<IngredientEntity> ingredients = Optional.ofNullable(request.getIngredientIds())
                .map(ingredientService::getIngredientsByIds)
                .orElse(null);

        recipe = RecipeRequest.mapRequestToRecipeEntity(request);
        recipe.setIngredients(ingredients);

        return recipeRepository.save(recipe);
    }

    public RecipeEntity findById(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHandler.getMessage(MessageConstants.RECIPE_NOTFOUND)));
    }

    public List<RecipeEntity> list(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return recipeRepository.findAll(pageRequest).getContent();
    }

    public void delete(long id) {
        if (!recipeRepository.existsById(id)) {
            log.error(messageHandler.getMessage(MessageConstants.INGREDIENT_NOTFOUND));
            throw new NotFoundException(messageHandler.getMessage(MessageConstants.RECIPE_NOTFOUND));
        }
        recipeRepository.deleteById(id);
    }

    public List<RecipeEntity> advanceSearch(int page, int size, RecipeSearchRequest criteria) {
        Pageable pageRequest = PageRequest.of(page, size);
        Specification<RecipeEntity> specification = recipeSearchUtil.buildSpecification(criteria.getCriteriaList(), criteria.getDataOption());

        return recipeRepository.findAll(specification,pageRequest).getContent();
    }





}
