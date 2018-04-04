package kr.itanoss.ganjang.integrationtest;

import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.Validator;

public class Dealer {

    private final Validator<Car> carValidator;

    public Dealer(Validator<Car> carValidator) {
        this.carValidator = carValidator;
    }

    public boolean inspect(Car car) {
        try {
            carValidator.validate(car);
            return true;
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
