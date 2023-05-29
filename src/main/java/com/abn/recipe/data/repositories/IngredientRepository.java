package com.abn.recipe.data.repositories;

import com.abn.recipe.data.models.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long>, JpaSpecificationExecutor<IngredientEntity> {
}
