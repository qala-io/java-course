package training.java.controller;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings(/*Public get/set methods are needed for Jackson*/"WeakerAccess")
class ValidationRestError {
    private String objectName;
    private String field;
    private String errorCode;
    private String errorMessage;

    @SuppressWarnings(/*used by Jackson*/"unused") public ValidationRestError() {}
    ValidationRestError(FieldError error) {
        this.objectName = error.getObjectName();
        this.field = error.getField();
        this.errorCode = error.getCode();
        this.errorMessage = error.getDefaultMessage();
    }

    private ValidationRestError(ObjectError error) {
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

    @Override public String toString() {
        return "ValidationRestError{" +
                "objectName='" + objectName + '\'' +
                ", field='" + field + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
