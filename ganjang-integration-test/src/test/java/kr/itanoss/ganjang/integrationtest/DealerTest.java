package kr.itanoss.ganjang.integrationtest;

import kr.itanoss.ganjang.Validator;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DealerTest {

    private Validator<Car> carValidator;
    private Dealer dealer;

    @Before
    public void setUp() {
        carValidator = new Car_Validator();
        dealer = new Dealer(carValidator);
    }

    @Test
    public void testInspect() {
        Car value = new Car();
        value.modelName = "520d";
        value.color = Color.Red;

        boolean result = dealer.inspect(value);

        assertThat(result).isTrue();
    }

    @Test
    public void testInspect_nullModelName() {
        Car value = new Car();
        value.color = Color.Red;

        boolean result = dealer.inspect(value);

        assertThat(result).isFalse();
    }

    @Test
    public void testInspect_nullColor() {
        Car value = new Car();
        value.modelName = "520d";

        boolean result = dealer.inspect(value);

        assertThat(result).isFalse();
    }
}