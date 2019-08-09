package com.ted.resonance.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CoinValidator.class)
@Documented
public @interface Coin {
    String message() default "Unknown coin type.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
