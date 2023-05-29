package com.abn.recipe.unit.builder;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.dto.request.IngredientRequest;

public class IngredientMockObjectBuilder {

    public static IngredientRequest giveIngredientRequest(String... ingName){
        return new IngredientRequest(ingName.length == 0 ? "Milk" : ingName[0]);
    }

    public static IngredientRequest giveIngredientRequest(long id){
        IngredientRequest ingredientRequest = giveIngredientRequest();
        ingredientRequest.setId(id);
        return ingredientRequest;
    }

    public static IngredientRequest giveIngredientRequest(long id, String ingName){
        IngredientRequest ingredientRequest = giveIngredientRequest(ingName);
        ingredientRequest.setId(id);
        return ingredientRequest;
    }

    public static IngredientEntity giveIngredientEntity(String... ingName){
        return new IngredientEntity(null, ingName.length == 0 ? "Milk" : ingName[0], null);
    }

    public static IngredientEntity giveIngredientEntity(long id){
        IngredientEntity ingredientEntity = giveIngredientEntity();
        ingredientEntity.setId(id);
        return ingredientEntity;
    }

    public static IngredientEntity giveIngredientEntity(long id, String ingName){
        IngredientEntity ingredientEntity = giveIngredientEntity();
        ingredientEntity.setId(id);
        ingredientEntity.setIngName(ingName);
        return ingredientEntity;
    }
}
