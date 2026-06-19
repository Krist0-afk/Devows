package com.usuario.Ms_usuario.validator;

import com.usuario.Ms_usuario.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementación del validador de email único.
 * Verifica en la base de datos si el email ya existe.
 */
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void initialize(EmailUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null es válido (otros validadores lo manejarán)
        }
        
        if (usuarioRepository == null) {
            return true; // Si el repositorio no está disponible, permitir (fallback)
        }

        return !usuarioRepository.findByEmail(value).isPresent();
    }
}
