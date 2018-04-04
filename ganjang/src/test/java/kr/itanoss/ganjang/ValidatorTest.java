package kr.itanoss.ganjang;

import org.junit.Before;
import org.junit.Test;

public class ValidatorTest {

    private Validator<Object> subject;

    @Before
    public void setUp() {
        subject = target -> {
        };
    }

    @Test
    public void validate_notNull_pass() throws ValidationException {
        subject.shouldNotNull("test");
    }

    @Test(expected = ValidationException.class)
    public void validate_notNull_fail() throws ValidationException {
        subject.shouldNotNull(null);
    }
}