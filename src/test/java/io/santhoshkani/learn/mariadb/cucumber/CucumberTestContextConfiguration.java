package io.santhoshkani.learn.mariadb.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import io.santhoshkani.learn.mariadb.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
