package com.abn.recipe.utilities.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumPatternValidator implements ConstraintValidator<EnumPattern, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(EnumPattern annotation) {
        acceptedValues = Stream.of(annotation.enumClazz().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}
