package com.abn.recipe.services;

import com.abn.recipe.data.models.IngredientEntity;
import com.abn.recipe.data.repositories.IngredientRepository;
import com.abn.recipe.dto.request.IngredientRequest;
import com.abn.recipe.exceptions.NotFoundException;
import com.abn.recipe.utilities.MessageHandler;
import com.abn.recipe.utilities.constants.MessageConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class IngredientService extends BaseService{

    private final IngredientRepository ingredientRepository;

    private final MessageHandler messageHandler;

    public IngredientEntity create(IngredientRequest request) {
        IngredientEntity ingredient = IngredientRequest.mapRequestToIngredientEntity(request);

        return ingredientRepository.save(ingredient);
    }

    public IngredientEntity update(IngredientRequest request) {
        IngredientEntity ingredientEntity = this.findById(request.getId());
        ingredientEntity.setIngName(request.getIngName());
        return ingredientRepository.save(ingredientEntity);
    }

    public void delete(long id) {
        if (!ingredientRepository.existsById(id)) {
            log.error(messageHandler.getMessage(MessageConstants.INGREDIENT_NOTFOUND));
            throw new NotFoundException(messageHandler.getMessage(MessageConstants.INGREDIENT_NOTFOUND));
        }
        ingredientRepository.deleteById(id);
    }

    public Set<IngredientEntity> getIngredientsByIds(List<Long> ingredientIds) {
        return ingredientIds.stream()
                .map(this::findById)
                .collect(Collectors.toSet());
    }

    public IngredientEntity findById(long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageHandler.getMessage(MessageConstants.INGREDIENT_NOTFOUND)));
    }

    public List<IngredientEntity> list(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return ingredientRepository.findAll(pageRequest).getContent();
    }
}
