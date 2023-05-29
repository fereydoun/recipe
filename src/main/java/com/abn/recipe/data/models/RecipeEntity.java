package com.abn.recipe.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "recipe", uniqueConstraints = @UniqueConstraint(name = "UK_RECIPE_NAME_1"
        , columnNames = {"name"}))
public class RecipeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "instructions")
    private String instructions;

    @Column(name = "type", length = 20)
    @ColumnDefault("'GENERAL'")
    private String type;

    @Column(name = "servings")
    private Integer servings;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "recipe_ingredient",
            joinColumns = {@JoinColumn(name = "recipe_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "ingredient_id", referencedColumnName = "id")})
    @JsonIgnore
    private Set<IngredientEntity> ingredients;
}
