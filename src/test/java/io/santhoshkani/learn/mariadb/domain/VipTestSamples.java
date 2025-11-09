package io.santhoshkani.learn.mariadb.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vip getVipSample1() {
        return new Vip().id(1L).name("name1");
    }

    public static Vip getVipSample2() {
        return new Vip().id(2L).name("name2");
    }

    public static Vip getVipRandomSampleGenerator() {
        return new Vip().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
