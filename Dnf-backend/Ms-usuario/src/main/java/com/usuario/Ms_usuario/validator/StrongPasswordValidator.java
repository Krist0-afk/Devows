package com.usuario.Ms_usuario.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementación del validador de contraseña fuerte.
 * Verifica que la contraseña contiene caracteres requeridos.
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null es válido (otros validadores lo manejarán)
        }

        // Verificar que tiene todos los requisitos
        boolean hasUpperCase = value.matches(".*[A-Z].*");
        boolean hasLowerCase = value.matches(".*[a-z].*");
        boolean hasDigit = value.matches(".*\\d.*");
        boolean hasSpecial = value.matches(".*[@$!%*?&].*");
        boolean hasMinLength = value.length() >= 8;

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecial && hasMinLength;
    }
}
