package com.gchong3068.framework.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author gc
 * @date 2024年05月25日 20:53
 */

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber , String> {


    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber != null && phoneNumber.matches("\\d{11}");
    }
}
