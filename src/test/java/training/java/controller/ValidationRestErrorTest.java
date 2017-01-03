package training.java.controller;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.testng.annotations.Test;
import training.java.domain.Dog;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static io.qala.datagen.RandomShortApi.nullOrBlank;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@Test
public class ValidationRestErrorTest {

    public void convertsFieldError_intoRestError() {
        BeanPropertyBindingResult errors = validate(Dog.random().setName(nullOrBlank()));
        ValidationRestError restError = new ValidationRestError(errors.getFieldError());
        assertEquals(restError.getObjectName(), "dog");
        assertEquals(restError.getField(), "name");
        assertEquals(restError.getErrorCode(), "NotBlankSized");
        assertEquals(restError.getErrorMessage(), "size must be between 1 and 100");
    }
    public void convertsAllErrors_intoRestError() {
        BeanPropertyBindingResult errors = validate(Dog.random().setName(nullOrBlank()));
        List<ValidationRestError> restErrors = ValidationRestError.fromSpringErrors(errors.getAllErrors());
        assertEquals(restErrors.size(), 1);
    }
    public void resultsInEmptyErrorList_ifValidationIsNotFailed() {
        BeanPropertyBindingResult errors = validate(Dog.random());
        List<ValidationRestError> restErrors = ValidationRestError.fromSpringErrors(errors.getAllErrors());
        assertEquals(restErrors.size(), 0);
    }

    @Test(enabled = false)//there are no global (class-level) validation so far, so nothing to test yet
    public void convertsObjectErrors_intoRestError() { fail(); }

    private BeanPropertyBindingResult validate(Dog dog) {
        CustomValidatorBean validator = new CustomValidatorBean();
        validator.afterPropertiesSet();
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dog, "dog");
        validator.validate(dog, errors);
        return errors;
    }
}