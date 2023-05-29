package com.abn.recipe.utilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

@Data
@Configuration
@AllArgsConstructor
public class MessageHandler {
    private final MessageSource messageSource;


    public String getMessage(String key) {
        return getMessage(key, null);
    }

    public String getMessage(String key, List<Object> args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args != null ? args.toArray() : null, locale);
    }
}
