package com.abn.recipe.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "ingredient", uniqueConstraints = @UniqueConstraint(name = "UK_INGREDIENT_NAME_1"
        , columnNames = {"ing_name"}))
public class IngredientEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ing_name", nullable = false)
    private String ingName;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private List<RecipeEntity> recipes;
}
