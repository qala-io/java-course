package training.java.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for {@link NotBlankSized} annotation
 *
 * @author Mikhail Stryzhonok
 */
public class NotBlankSizedValidator implements ConstraintValidator<NotBlankSized, String> {

    private int min;
    private int max;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(NotBlankSized constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    /**
     * Validates input string size
     *
     * @param value string with {@link NotBlankSized} annotation
     * @param context validation context
     *
     * @return true if string not null and has size between the specified boundaries (included).
     *         false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null
                && value.length() >= min
                && value.length() <= max;
    }
}
