package kr.itanoss.ganjang.integrationtest;

import kr.itanoss.ganjang.integrationtest.model.Car;
import kr.itanoss.ganjang.integrationtest.model.Dealer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DealerTest {

    private Dealer dealer;

    @Before
    public void setUp() {
        dealer = new Dealer();
    }

    @Test
    public void testInspect() {
        Car value = new Car();
        value.modelName = "520d";

        assertTrue(dealer.inspect(value));
    }

    @Test
    public void testInspect_nullModelName() {
        Car value = new Car();
        // no setting model name

        assertFalse(dealer.inspect(value));
    }
}