package com.ted.resonance.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 *  自定义注解限制 输入的币种
 */
public class CoinValidator implements ConstraintValidator<Coin, String> {
    List<String> coins = Arrays.asList("eth", "etc");
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String type = s.trim().toLowerCase();
        return coins.contains(type);
    }
}
