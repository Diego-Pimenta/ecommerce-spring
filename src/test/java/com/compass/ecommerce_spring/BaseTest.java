package com.compass.ecommerce_spring;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    // https://danielme.com/2023/04/13/testing-spring-boot-docker-with-testcontainers-and-junit-5-mysql-and-other-images/
    @Container
    @ServiceConnection
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("ecommerce_spring")
            .withUsername("user")
            .withPassword("password")
            .withExposedPorts(3306)
            .withInitScript("schema.sql")
            .withTmpFs(Map.of("/var/lib/mysql", "rw"))
            .waitingFor(Wait.forLogMessage(".*mysqld: ready for connections.*", 2))
            .withReuse(true);

    @Container
    @ServiceConnection
    protected static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.4-alpine")
            .withExposedPorts(6379);

    static {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
        redisContainer.stop();
    }
}
