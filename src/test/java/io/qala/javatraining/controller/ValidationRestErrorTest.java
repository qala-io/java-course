package io.qala.javatraining.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import io.qala.javatraining.domain.Dog;

import java.util.List;

import static io.qala.datagen.RandomShortApi.nullOrBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ValidationRestErrorTest {

    @Test void convertsFieldError_intoRestError() {
        BeanPropertyBindingResult errors = validate(Dog.random().setName(nullOrBlank()));
        ValidationRestError restError = new ValidationRestError(errors.getFieldError());
        assertEquals(restError.getObjectName(), "dog");
        assertEquals(restError.getField(), "name");
        assertEquals(restError.getErrorCode(), "NotBlankSized");
        assertEquals(restError.getErrorMessage(), "size must be between 1 and 100");
    }
    @Test void convertsAllErrors_intoRestError() {
        BeanPropertyBindingResult errors = validate(Dog.random().setName(nullOrBlank()));
        List<ValidationRestError> restErrors = ValidationRestError.fromSpringErrors(errors.getAllErrors());
        assertEquals(restErrors.size(), 1);
    }
    @Test void resultsInEmptyErrorList_ifValidationIsNotFailed() {
        BeanPropertyBindingResult errors = validate(Dog.random());
        List<ValidationRestError> restErrors = ValidationRestError.fromSpringErrors(errors.getAllErrors());
        assertEquals(restErrors.size(), 0);
    }

    //there are no global (class-level) validation so far, so nothing to test yet
    @Test @Disabled void convertsObjectErrors_intoRestError() { fail(""); }

    private BeanPropertyBindingResult validate(Dog dog) {
        CustomValidatorBean validator = new CustomValidatorBean();
        validator.afterPropertiesSet();
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dog, "dog");
        validator.validate(dog, errors);
        return errors;
    }
}