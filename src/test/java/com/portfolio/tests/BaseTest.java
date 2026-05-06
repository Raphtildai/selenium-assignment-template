package com.portfolio.tests;

import com.portfolio.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest sets up and tears down a fresh ChromeDriver for each test method.
 * All test classes extend this so driver configuration stays in one place.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUpDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Headless mode is controlled by config.properties
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }

        // Window size from config
        options.addArguments(String.format(
                "--window-size=%d,%d",
                ConfigReader.getWindowWidth(),
                ConfigReader.getWindowHeight()
        ));

        // Standard CI-friendly Chrome flags
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--remote-allow-origins=*"
        );

        // Custom user agent to identify test traffic
        options.addArguments(
                "--user-agent=SeleniumTestBot/1.0 (PortfolioTest)"
        );

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
