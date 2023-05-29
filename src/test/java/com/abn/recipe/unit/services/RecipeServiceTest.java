package com.abn.recipe.unit.services;


import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.models.RecipeEntity;
import com.abn.recipe.data.repositories.RecipeRepository;
import com.abn.recipe.dto.request.RecipeRequest;
import com.abn.recipe.dto.search.advance.DataOption;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchCriteria;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchRequest;
import com.abn.recipe.dto.search.advance.recipe.RecipeSpecification;
import com.abn.recipe.exceptions.NotFoundException;
import com.abn.recipe.services.IngredientService;
import com.abn.recipe.services.RecipeService;
import com.abn.recipe.dto.search.advance.recipe.RecipeSearchUtil;
import com.abn.recipe.unit.builder.IngredientMockObjectBuilder;
import com.abn.recipe.unit.builder.RecipeMockObjectBuilder;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MessageHandler messageHandler;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private RecipeSearchUtil recipeSearchUtil;

    @Test
    @DisplayName("Successful - Create a recipe")
    public void create() {
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        RecipeEntity recipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));
        when(ingredientService.getIngredientsByIds(List.of(10L, 11L))).thenReturn(Set.of(ingredientEntity10, ingredientEntity11));
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);

        RecipeRequest recipeRequest = RecipeMockObjectBuilder.giveRecipeRequest("Pizza", "GENERAL", "mix everything", 2, List.of(10L, 11L));
        RecipeEntity response = recipeService.create(recipeRequest);

        assertNotNull(response);
        assertNotNull(response.getIngredients());
        assertEquals(100, response.getId());
        assertEquals(2, response.getIngredients().size());

        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    @DisplayName("Successful - Update a recipe")
    public void update() {

        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        IngredientEntity ingredientEntity12 = IngredientMockObjectBuilder.giveIngredientEntity(12L, "Milk");
        RecipeEntity oldRecipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2,
                new HashSet<>(){{add(ingredientEntity10); add(ingredientEntity11);}});

        when(recipeRepository.findById(100L)).thenReturn(Optional.of(oldRecipeEntity));
        when(ingredientService.getIngredientsByIds(List.of(10L, 11L, 12L))).thenReturn(Set.of(ingredientEntity10, ingredientEntity11, ingredientEntity12));

        RecipeEntity newRecipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11, ingredientEntity12));
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(newRecipeEntity);

        RecipeRequest recipeRequest = RecipeMockObjectBuilder.giveRecipeRequest(100, "Pizza", "GENERAL", "mix everything", 2, List.of(10L, 11L, 12L));
        RecipeEntity response = recipeService.update(recipeRequest);

        assertNotNull(response);
        assertNotNull(response.getIngredients());
        assertEquals(100, response.getId());
        assertEquals(3, response.getIngredients().size());

        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    @DisplayName("Successful - Delete a recipe")
    public void delete(){
        doNothing().when(recipeRepository).deleteById(anyLong());
        when(recipeRepository.existsById(anyLong())).thenReturn(true);
        recipeService.delete(anyLong());

        verify(recipeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("NotFound - Delete a recipe")
    public void deleteNoFound(){
        when(recipeRepository.existsById(anyLong())).thenReturn(false);
        when(messageHandler.getMessage(anyString())).thenReturn("Recipe Not Found");
        assertThrowsExactly(NotFoundException.class, () -> {recipeService.delete(anyLong());});

        verify(recipeRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Successful - find a recipe By Id")
    public void findById(){
        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        RecipeEntity recipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));

        when(recipeRepository.findById(100L)).thenReturn(Optional.ofNullable(recipeEntity));

        RecipeEntity response = recipeService.findById(100L);

        assertNotNull(response);
        assertEquals(100, response.getId());
        assertEquals(recipeEntity.getIngredients().size(), response.getIngredients().size());

        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("NotFound - find a recipe By Id")
    public void findByIdNotFound(){
        RecipeEntity recipeEntity = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of());
        when(recipeRepository.findById(anyLong())).thenThrow(new NotFoundException());

        assertThrowsExactly(NotFoundException.class, () -> {recipeService.findById(anyLong());});

        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successful - get a list of recipe")
    public void getRecipeList(){

        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        IngredientEntity ingredientEntity12 = IngredientMockObjectBuilder.giveIngredientEntity(12L, "Meat");
        RecipeEntity recipeEntity1 = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));
        RecipeEntity recipeEntity2 = RecipeMockObjectBuilder.giveRecipeEntity(101, "Noodle", "GENERAL", "mix everything", 3, Set.of(ingredientEntity10, ingredientEntity12));
        List<RecipeEntity> recipes = List.of(recipeEntity1, recipeEntity2);

        Pageable pageRequest = PageRequest.of(0, 10);
        when(recipeRepository.findAll(pageRequest)).thenReturn(new PageImpl<RecipeEntity>(recipes, pageRequest, 3));

        List<RecipeEntity> responses = recipeService.list(0, 10);

        assertNotNull(responses);
        assertEquals(recipes.size(), responses.size());
        assertEquals(recipes.get(0).getId(), responses.get(0).getId());
        assertEquals(recipes.get(1).getId(), responses.get(1).getId());

        verify(recipeRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Successful - recipe advance search")
    public void advanceSearch(){

        IngredientEntity ingredientEntity10 = IngredientMockObjectBuilder.giveIngredientEntity(10L, "Egg");
        IngredientEntity ingredientEntity11 = IngredientMockObjectBuilder.giveIngredientEntity(11L, "Salt");
        IngredientEntity ingredientEntity12 = IngredientMockObjectBuilder.giveIngredientEntity(12L, "Meat");
        RecipeEntity recipeEntity1 = RecipeMockObjectBuilder.giveRecipeEntity(100, "Pizza", "GENERAL", "mix everything", 2, Set.of(ingredientEntity10, ingredientEntity11));
        RecipeEntity recipeEntity2 = RecipeMockObjectBuilder.giveRecipeEntity(101, "Noodle", "GENERAL", "mix everything", 3, Set.of(ingredientEntity10, ingredientEntity12));
        List<RecipeEntity> recipes = List.of(recipeEntity1, recipeEntity2);

        RecipeSearchRequest criteria= new RecipeSearchRequest();
        criteria.setDataOption(DataOption.all.name());
        RecipeSearchCriteria recipeCriteria1 = new RecipeSearchCriteria();
        recipeCriteria1.setFilterKey("name");
        recipeCriteria1.setValue("Pizza");
        recipeCriteria1.setOperation("eq");
        criteria.setCriteriaList(List.of(recipeCriteria1));

        Pageable pageRequest = PageRequest.of(0, 10);

        RecipeSpecification specification = new RecipeSpecification(new RecipeSearchCriteria());
        when(recipeSearchUtil.buildSpecification(any(), anyString())).thenReturn(specification);
        when(recipeRepository.findAll(specification, pageRequest)).thenReturn(new PageImpl<>(recipes, pageRequest, 2));
        List<RecipeEntity> responses = recipeService.advanceSearch(0, 10, criteria);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(recipes.get(0).getId(), responses.get(0).getId());

        verify(recipeRepository, times(1)).findAll(specification, pageRequest);
    }
}
