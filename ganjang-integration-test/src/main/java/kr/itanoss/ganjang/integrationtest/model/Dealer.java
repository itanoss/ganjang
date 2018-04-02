package kr.itanoss.ganjang.integrationtest.model;

import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.Validator;

public class Dealer {

    private Validator<Car> carValidator = new kr.itanoss.ganjang.integrationtest.model.Car_Validator();

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
