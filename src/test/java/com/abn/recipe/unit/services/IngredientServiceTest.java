package com.abn.recipe.unit.services;


import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.repositories.IngredientRepository;
import com.abn.recipe.dto.request.IngredientRequest;
import com.abn.recipe.exceptions.NotFoundException;
import com.abn.recipe.services.IngredientService;
import com.abn.recipe.unit.builder.IngredientMockObjectBuilder;
import com.abn.recipe.utilities.MessageHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private MessageHandler messageHandler;

    @Test
    @DisplayName("Successful - Create of ingredient")
    public void create() {
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        when(ingredientRepository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity);

        IngredientRequest ingredientRequest = IngredientMockObjectBuilder.giveIngredientRequest("Egg");
        IngredientEntity response = ingredientService.create(ingredientRequest);

        assertNotNull(response);
        assertEquals(10, response.getId());
        assertEquals(ingredientEntity.getIngName(), response.getIngName());

        verify(ingredientRepository, times(1)).save(any(IngredientEntity.class));
    }

    @Test
    @DisplayName("Successful - Update an ingredient")
    public void update(){
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat");
        when(ingredientRepository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity);
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(ingredientEntity));

        IngredientRequest ingredientRequest = IngredientMockObjectBuilder.giveIngredientRequest(10,"Meat");
        IngredientEntity response = ingredientService.update(ingredientRequest);

        assertNotNull(response);
        assertEquals(10, response.getId());
        assertEquals(ingredientRequest.getIngName(), response.getIngName());

        verify(ingredientRepository, times(1)).save(any(IngredientEntity.class));
    }

    @Test
    @DisplayName("Successful - Delete an ingredient")
    public void delete(){
        doNothing().when(ingredientRepository).deleteById(anyLong());
        when(ingredientRepository.existsById(anyLong())).thenReturn(true);
        ingredientService.delete(anyLong());

        verify(ingredientRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("NotFound - Delete an ingredient")
    public void deleteNoFound(){
        when(ingredientRepository.existsById(anyLong())).thenReturn(false);
        when(messageHandler.getMessage(anyString())).thenReturn("Ingredient Not Found");
        assertThrowsExactly(NotFoundException.class, () -> {ingredientService.delete(anyLong());});

        verify(ingredientRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Successful - find an ingredient By Id")
    public void findById(){
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat");
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ingredientEntity));

        IngredientEntity response = ingredientService.findById(10L);

        assertNotNull(response);
        assertEquals(10, response.getId());
        assertEquals(ingredientEntity.getIngName(), response.getIngName());

        verify(ingredientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("NotFound - find an ingredient By Id")
    public void findByIdNotFound(){
        IngredientEntity ingredientEntity = IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat");
        when(ingredientRepository.findById(anyLong())).thenThrow(new NotFoundException());

        assertThrowsExactly(NotFoundException.class, () -> {ingredientService.findById(anyLong());});

        verify(ingredientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successful - get a list of ingredients")
    public void getIngredientList(){
        List<IngredientEntity> ingredients = List.of(IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat"),
                IngredientMockObjectBuilder.giveIngredientEntity(11, "Salt"),
                IngredientMockObjectBuilder.giveIngredientEntity(12, "Egg")
        );
        Pageable pageRequest = PageRequest.of(0, 10);
        when(ingredientRepository.findAll(pageRequest)).thenReturn(new PageImpl<IngredientEntity>(ingredients, pageRequest, 3));

        List<IngredientEntity> responses = ingredientService.list(0, 10);

        assertNotNull(responses);
        assertEquals(ingredients.size(), responses.size());
        assertEquals(ingredients.get(0).getId(), responses.get(0).getId());
        assertEquals(ingredients.get(1).getId(), responses.get(1).getId());
        assertEquals(ingredients.get(2).getId(), responses.get(2).getId());

        verify(ingredientRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Successful - get a list of ingredients by id")
    public void getIngredientsByIds(){
        IngredientEntity ingredient = IngredientMockObjectBuilder.giveIngredientEntity(10, "Meat");
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(ingredient));

        Set<IngredientEntity> responses = ingredientService.getIngredientsByIds(new ArrayList<>(){{add(10L);}});

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(ingredient.getId(), responses.stream().toList().get(0).getId());

        verify(ingredientRepository, times(1)).findById(anyLong());
    }

}
