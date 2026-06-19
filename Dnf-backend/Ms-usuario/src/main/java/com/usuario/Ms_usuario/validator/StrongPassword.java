package com.usuario.Ms_usuario.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validador personalizado para verificar que una contraseña es fuerte.
 * Requiere mayúsculas, minúsculas, números y caracteres especiales.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
@Documented
public @interface StrongPassword {
    String message() default "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales (@$!%*?&)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
