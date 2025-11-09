package io.santhoshkani.learn.mariadb.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CountryStatsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CountryStats getCountryStatsSample1() {
        return new CountryStats().id(1L).year(1).population(1L);
    }

    public static CountryStats getCountryStatsSample2() {
        return new CountryStats().id(2L).year(2).population(2L);
    }

    public static CountryStats getCountryStatsRandomSampleGenerator() {
        return new CountryStats().id(longCount.incrementAndGet()).year(intCount.incrementAndGet()).population(longCount.incrementAndGet());
    }
}
