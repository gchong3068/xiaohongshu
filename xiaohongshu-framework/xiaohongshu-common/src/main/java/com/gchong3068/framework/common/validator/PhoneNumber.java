package com.gchong3068.framework.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gc
 * @date 2024年05月25日 20:52
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.PARAMETER})
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {

    String message() default "手机号格式不正确,请核实后重试";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
