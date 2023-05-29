package com.abn.recipe.unit.controllers;


import com.abn.recipe.controllers.IngredientController;
import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.dto.request.IngredientRequest;
import com.abn.recipe.dto.response.IngredientResponse;
import com.abn.recipe.services.IngredientService;
import com.abn.recipe.unit.builder.IngredientMockObjectBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientControllerTest {

    @InjectMocks
    private IngredientController ingredientController;
    @Mock
    private IngredientService ingredientService;

    @Test
    @DisplayName("Successful - create an ingredient")
    public void createIngredient() {
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        when(ingredientService.create(any(IngredientRequest.class))).thenReturn(ingredientEntity);

        IngredientRequest ingredientRequest = IngredientMockObjectBuilder.giveIngredientRequest("Egg");
        IngredientResponse response = ingredientController.createIngredient(ingredientRequest);

        assertNotNull(response);
        assertEquals(ingredientEntity.getIngName(), response.getIngName());

        verify(ingredientService, times(1)).create(ingredientRequest);
    }

    @Test
    @DisplayName("Successful - update an ingredient")
    public void updateIngredient() {
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat");
        when(ingredientService.update(any(IngredientRequest.class))).thenReturn(ingredientEntity);

        IngredientRequest ingredientRequest = IngredientMockObjectBuilder.giveIngredientRequest("Meat");
        IngredientResponse response = ingredientController.updateIngredient(10L, ingredientRequest);

        assertNotNull(response);
        assertEquals(ingredientEntity.getId(), response.getId());
        assertEquals(ingredientRequest.getIngName(), response.getIngName());

        verify(ingredientService, times(1)).update(ingredientRequest);
    }

    @Test
    @DisplayName("Successful - delete an ingredient")
    public void deleteIngredient() {
        doNothing().when(ingredientService).delete(anyLong());
        ingredientController.deleteIngredient(anyLong());

        verify(ingredientService, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("Successful - get an ingredient")
    public void getIngredient() {
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat");
        when(ingredientService.findById(anyLong())).thenReturn(ingredientEntity);

        IngredientResponse response = ingredientController.getIngredient(10L);

        assertNotNull(response);
        assertEquals(10, response.getId());
        assertEquals(ingredientEntity.getIngName(), response.getIngName());

        verify(ingredientService, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successful - get a list of ingredients")
    public void getIngredientList() {
        List<IngredientEntity> ingredients = List.of(IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat"),
                IngredientMockObjectBuilder.giveIngredientEntity(11, "Salt"),
                IngredientMockObjectBuilder.giveIngredientEntity(12, "Egg")
        );
        when(ingredientService.list(anyInt(), anyInt())).thenReturn(ingredients);

        List<IngredientResponse> responses = ingredientController.getIngredientList(anyInt(), anyInt());

        assertNotNull(responses);
        assertEquals(ingredients.size(), responses.size());
        assertEquals(ingredients.get(0).getId(), responses.get(0).getId());
        assertEquals(ingredients.get(1).getId(), responses.get(1).getId());
        assertEquals(ingredients.get(2).getId(), responses.get(2).getId());

        verify(ingredientService, times(1)).list(anyInt(), anyInt());
    }
}
