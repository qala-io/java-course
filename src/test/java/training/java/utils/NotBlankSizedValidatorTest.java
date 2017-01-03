package training.java.utils;

import org.testng.annotations.Test;

import javax.validation.Payload;
import java.lang.annotation.Annotation;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.unicode;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class NotBlankSizedValidatorTest {

    public void validValuesPassValidation() {
        assertTrue(validator(1, 3).isValid(unicode(1, 3), null));
    }
    public void nullFailsValidation() {
        assertFalse(new NotBlankSizedValidator().isValid(null, null));
    }
    public void greaterThanMax_failsValidation() {
        assertFalse(validator(1, 10).isValid(unicode(11), null));
    }
    public void shorterThanMin_failsValidation() {
        assertFalse(validator(2, 3).isValid(unicode(1), null));
    }
    public void minValue_passesValidation() {
        assertTrue(validator(1, 10).isValid(unicode(1), null));
    }
    public void maxValue_passesValidation() {
        assertTrue(validator(1, 3).isValid(unicode(3), null));
    }

    private NotBlankSizedValidator validator(int min, int max) {
        NotBlankSizedValidator validator = new NotBlankSizedValidator();
        validator.initialize(notBlankSized(min, max));
        return validator;
    }

    private static NotBlankSized notBlankSized(int min, int max) {
        return new NotBlankImpl(min, max);
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class NotBlankImpl implements NotBlankSized {
        private final int min, max;

        private NotBlankImpl(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override public int min() { return min; }
        @Override public int max() { return max; }

        @Override public String message() { return null; }
        @Override public Class<?>[] groups() { return new Class<?>[0]; }
        @Override public Class<? extends Payload>[] payload() { return null; }
        @Override public Class<? extends Annotation> annotationType() { return null; }
    }
}
