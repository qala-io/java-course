package training.java.controller;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ValidationRestError {
    private String objectName;
    private String field;
    private String errorCode;
    private String errorMessage;

    public ValidationRestError() {}
    public ValidationRestError(FieldError error) {
        this.objectName = error.getObjectName();
        this.field = error.getField();
        this.errorCode = error.getCode();
        this.errorMessage = error.getDefaultMessage();
    }

    public ValidationRestError(ObjectError error) {
        this.objectName = error.getObjectName();
        this.errorMessage = error.getDefaultMessage();
        this.errorCode = error.getCode();
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static List<ValidationRestError> fromSpringErrors(List<ObjectError> allErrors) {
        List<ValidationRestError> result = new ArrayList<>(allErrors.size());
        for(ObjectError next: allErrors) {
            if(next instanceof FieldError) result.add(new ValidationRestError((FieldError) next));
            else result.add(new ValidationRestError(next));
        }
        return result;
    }
}
