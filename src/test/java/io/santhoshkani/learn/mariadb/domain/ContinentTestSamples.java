package io.santhoshkani.learn.mariadb.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContinentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Continent getContinentSample1() {
        return new Continent().id(1L).name("name1");
    }

    public static Continent getContinentSample2() {
        return new Continent().id(2L).name("name2");
    }

    public static Continent getContinentRandomSampleGenerator() {
        return new Continent().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
