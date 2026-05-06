package com.portfolio.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestNG listener that captures a screenshot whenever a test fails.
 * Screenshots are saved to target/screenshots/ with a timestamp.
 */
public class ScreenshotListener implements ITestListener {

    private static final String SCREENSHOT_DIR = "target/screenshots/";

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();

        // Retrieve the WebDriver from the test class via reflection
        WebDriver driver = null;
        try {
            java.lang.reflect.Field driverField =
                    testInstance.getClass().getSuperclass().getDeclaredField("driver");
            driverField.setAccessible(true);
            driver = (WebDriver) driverField.get(testInstance);
        } catch (Exception e) {
            System.err.println("[ScreenshotListener] Could not get driver: " + e.getMessage());
        }

        if (driver == null) return;

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String testName  = result.getName();
        String fileName  = SCREENSHOT_DIR + testName + "_" + timestamp + ".png";

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(fileName);
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("[ScreenshotListener] Screenshot saved: " + fileName);
        } catch (IOException e) {
            System.err.println("[ScreenshotListener] Failed to save screenshot: " + e.getMessage());
        }
    }
}
