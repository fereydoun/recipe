package com.abn.recipe.unit.builder;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.dto.request.RecipeRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeMockObjectBuilder {

    public static RecipeRequest giveRecipeRequest(String name, String type, String instructions, int servings, List<Long> ingredientIds){
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName(name);
        recipeRequest.setType(type);
        recipeRequest.setInstructions(instructions);
        recipeRequest.setServings(servings);
        recipeRequest.setIngredientIds(ingredientIds != null ? ingredientIds : new ArrayList<>());
        return recipeRequest;
    }

    public static RecipeRequest giveRecipeRequest(long id, String name, String type, String instructions, int servings, List<Long> ingredientIds){
        RecipeRequest recipeRequest = giveRecipeRequest(name, type, instructions, servings, ingredientIds);
        recipeRequest.setId(id);
        return recipeRequest;
    }

    public static RecipeRequest giveRecipeRequest(long id){
        RecipeRequest recipeRequest = giveRecipeRequest("Pizza", "GENERAL", "mix every thing ", 3, new ArrayList<>(){{add(10L); add(11L);}});
        recipeRequest.setId(id);
        return recipeRequest;
    }

    public static RecipeEntity giveRecipeEntity(String name, String type, String instructions, int servings, Set<IngredientEntity> ingredients){
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        recipeEntity.setType(type);
        recipeEntity.setInstructions(instructions);
        recipeEntity.setServings(servings);
        recipeEntity.setIngredients(ingredients != null ? ingredients : new HashSet<>());
        return recipeEntity;
    }

    public static RecipeEntity giveRecipeEntity(long id, String name, String type, String instructions, int servings, Set<IngredientEntity> ingredients){
        RecipeEntity recipeEntity = giveRecipeEntity(name, type, instructions, servings, ingredients);
        recipeEntity.setId(id);
        return recipeEntity;
    }

    public static RecipeEntity giveRecipeEntity(long id){
        RecipeEntity recipeEntity = giveRecipeEntity("Pizza", "GENERAL", "mix every thing ", 3, new HashSet<>(){{
            add(IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat"));
            add(IngredientMockObjectBuilder.giveIngredientEntity(11, "Salt"));
        }});
        recipeEntity.setId(id);
        return recipeEntity;
    }

}
