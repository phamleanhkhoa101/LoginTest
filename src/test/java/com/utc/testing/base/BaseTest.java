package com.utc.testing.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;
    protected String validUsername;

    @BeforeEach
    void setUp() throws IOException {
        Properties config = new Properties();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Không tìm thấy config.properties");
            }
            config.load(stream);
        }

        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(config.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        driver = new ChromeDriver(options); // Selenium Manager tự quản lý ChromeDriver.
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = config.getProperty("base.url");
        validUsername = System.getenv("UTC_USERNAME");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
