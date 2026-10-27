package com.eloyroldan.tp_final_alojamiento.backend.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Valida que el dominio del correo (lo que va despues de la arroba) sea uno de
/// los proveedores conocidos (gmail, hotmail, etc), para filtrar dominios
/// inventados tipo "@correofalso.com". La lista de dominios permitidos vive en
/// DominioEmailValidoValidator.
@Documented
@Constraint(validatedBy = DominioEmailValidoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DominioEmailValido {

    String message() default "El dominio de correo no esta permitido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
