package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static edu.java.scrapper.IntegrationTest.POSTGRES;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;

@Testcontainers
public class LiquibaseIntegrationTest extends IntegrationTest{
    @Test
    public void migrationTest() {
        assertTrue(POSTGRES.isRunning());
    }
}
