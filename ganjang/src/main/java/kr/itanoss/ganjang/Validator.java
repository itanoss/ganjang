package kr.itanoss.ganjang;

public interface Validator<T> {
    void validate(T target) throws ValidationException;

    default <T> void shouldNotNull(T value) throws ValidationException {
        if(value == null) {
            throw new ValidationException();
        }
    }

    default void shouldNotEmpty(String value) throws ValidationException {
        shouldNotNull(value);

        if(value.isEmpty()) {
            throw new ValidationException();
        }
    }
}
