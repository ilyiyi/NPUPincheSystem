package com.devhub.pinchesystemback;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestBigDecimal {
    @Test
    public void testDiv() {
        BigDecimal a = new BigDecimal(19);
        BigDecimal b = new BigDecimal(33);

        double v = a.divide(b, 2, RoundingMode.HALF_UP).doubleValue();
        System.out.println(v);
    }

}
