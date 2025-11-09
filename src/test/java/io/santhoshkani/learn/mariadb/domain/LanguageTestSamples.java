package io.santhoshkani.learn.mariadb.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LanguageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Language getLanguageSample1() {
        return new Language().id(1L).language("language1");
    }

    public static Language getLanguageSample2() {
        return new Language().id(2L).language("language2");
    }

    public static Language getLanguageRandomSampleGenerator() {
        return new Language().id(longCount.incrementAndGet()).language(UUID.randomUUID().toString());
    }
}
