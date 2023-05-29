package com.abn.recipe.unit.controllers;


import com.abn.recipe.controllers.RecipeController;
import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.request.RecipeRequest;
import com.abn.recipe.dto.response.RecipeResponse;
import com.abn.recipe.dto.search.advance.DataOption;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchCriteria;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchRequest;
import com.abn.recipe.services.RecipeService;
import com.abn.recipe.unit.builder.IngredientMockObjectBuilder;
import com.abn.recipe.unit.builder.RecipeMockObjectBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    @InjectMocks
    private RecipeController recipeController;
    @Mock
    private RecipeService recipeService;

    @Test
    @DisplayName("Successful create a recipe")
    public void createRecipe() {
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        RecipeEntity recipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));

        when(recipeService.create(any(RecipeRequest.class))).thenReturn(recipeEntity);

        RecipeRequest recipeRequest = RecipeMockObjectBuilder.giveRecipeRequest("Pizza", "GENERAL", "mix everything", 2, List.of(10L, 11L));
        RecipeResponse response = recipeController.createRecipe(recipeRequest);

        assertNotNull(response);
        assertEquals(recipeEntity.getIngredients().size(), response.getIngredients().size());

        verify(recipeService, times(1)).create(recipeRequest);
    }

    @Test
    @DisplayName("Successful update a recipe")
    public void updateRecipe() {
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        RecipeEntity recipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));

        when(recipeService.update(any(RecipeRequest.class))).thenReturn(recipeEntity);

        RecipeRequest recipeRequest = RecipeMockObjectBuilder.giveRecipeRequest("Pizza", "GENERAL", "mix everything", 2, List.of(10L, 11L));
        RecipeResponse response = recipeController.updateRecipe(100L, recipeRequest);

        assertNotNull(response);
        assertEquals(recipeEntity.getId(), response.getId());
        assertEquals(recipeEntity.getIngredients().size(), response.getIngredients().size());

        verify(recipeService, times(1)).update(recipeRequest);
    }

    @Test
    @DisplayName("Successful - delete a recipe")
    public void deleteRecipe() {
        doNothing().when(recipeService).delete(anyLong());
        recipeController.deleteRecipe(anyLong());

        verify(recipeService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Successful - get an ingredient")
    public void getRecipe() {
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        RecipeEntity recipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));

        when(recipeService.findById(anyLong())).thenReturn(recipeEntity);

        RecipeResponse response = recipeController.getRecipe(100L);

        assertNotNull(response);
        assertEquals(100, response.getId());

        verify(recipeService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successful - get a list of recipe")
    public void getRecipeList() {
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        IngredientEntity ingredientEntity12 = IngredientMockObjectBuilder.giveIngredientEntity(12L, "Meat");
        RecipeEntity recipeEntity1 = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));
        RecipeEntity recipeEntity2 = RecipeMockObjectBuilder.giveRecipeEntity(101, "Noodle", "GENERAL", "mix everything", 3, Set.of(ingredientEntity10, ingredientEntity12));
        List<RecipeEntity> recipes = List.of(recipeEntity1, recipeEntity2);

        when(recipeService.list(anyInt(), anyInt())).thenReturn(recipes);

        List<RecipeResponse> responses = recipeController.getRecipeList(anyInt(), anyInt());

        assertNotNull(responses);
        assertEquals(recipes.size(), responses.size());
        assertEquals(recipes.get(0).getId(), responses.get(0).getId());
        assertEquals(recipes.get(1).getId(), responses.get(1).getId());
        assertEquals(recipes.get(0).getIngredients().size(), responses.get(0).getIngredients().size());
        assertEquals(recipes.get(1).getIngredients().size(), responses.get(1).getIngredients().size());

        verify(recipeService, times(1)).list(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Successful - recipe advance search")
    public void advanceSearch() {
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        IngredientEntity ingredientEntity12 = IngredientMockObjectBuilder.giveIngredientEntity(12L, "Meat");
        RecipeEntity recipeEntity1 = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));
        RecipeEntity recipeEntity2 = RecipeMockObjectBuilder.giveRecipeEntity(101, "Noodle", "VEGETARIAN", "mix everything", 3, Set.of(ingredientEntity10, ingredientEntity12));
        List<RecipeEntity> recipes = List.of(recipeEntity1, recipeEntity2);

        RecipeSearchRequest criteria = new RecipeSearchRequest();
        criteria.setDataOption(DataOption.any.name());
        RecipeSearchCriteria recipeCriteria1 = new RecipeSearchCriteria();
        recipeCriteria1.setFilterKey("name");
        recipeCriteria1.setValue("Pizza");
        recipeCriteria1.setOperation("cn");
        RecipeSearchCriteria recipeCriteria2 = new RecipeSearchCriteria();
        recipeCriteria2.setFilterKey("type");
        recipeCriteria2.setValue("VEGETARIAN");
        recipeCriteria2.setOperation("eq");
        criteria.setCriteriaList(List.of(recipeCriteria1, recipeCriteria2));

        when(recipeService.advanceSearch(0, 10, criteria)).thenReturn(recipes);

        List<RecipeResponse> responses = recipeController.searchRecipe(0, 10, criteria);

        assertNotNull(responses);
        assertEquals(recipes.size(), responses.size());
        assertEquals(recipes.get(0).getId(), responses.get(0).getId());
        assertEquals(recipes.get(1).getId(), responses.get(1).getId());
        assertEquals(recipes.get(0).getIngredients().size(), responses.get(0).getIngredients().size());
        assertEquals(recipes.get(1).getIngredients().size(), responses.get(1).getIngredients().size());

        verify(recipeService, times(1)).advanceSearch(0, 10, criteria);
    }

}
