package io.santhoshkani.learn.mariadb;

import io.santhoshkani.learn.mariadb.config.AsyncSyncConfiguration;
import io.santhoshkani.learn.mariadb.config.EmbeddedSQL;
import io.santhoshkani.learn.mariadb.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { LearnMariadbApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
