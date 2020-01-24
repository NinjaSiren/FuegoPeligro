package com.mygdx.fuegopeligro;

import java.util.Random;

public final class RandomNumberGenerator {
    private int generatedNumber;

    public RandomNumberGenerator(final int minValue, final int maxValue) {
        generatedNumber = new Random().ints(1, minValue, maxValue)
                .distinct()
                .findAny()
                .getAsInt();
    }

    public int getGeneratedNumber() {
        return generatedNumber;
    }
}
