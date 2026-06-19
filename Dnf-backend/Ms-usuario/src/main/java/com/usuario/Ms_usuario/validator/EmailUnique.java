package com.usuario.Ms_usuario.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validador personalizado para verificar que un email es único.
 * Se usa en DTOs de registro.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailUniqueValidator.class)
@Documented
public @interface EmailUnique {
    String message() default "El email ya está registrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
