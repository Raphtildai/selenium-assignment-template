package com.portfolio.pages;

import com.portfolio.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage holds shared WebDriver utilities that all page objects inherit.
 * Every page class extends this instead of duplicating wait/scroll logic.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    /** Waits until an element is visible, then returns it. */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits until an element is clickable, then returns it. */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Clears a field and types text into it. */
    protected void fill(By locator, String text) {
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    /** Clicks an element after waiting for it to be clickable. */
    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    /** Reads the trimmed text content of an element. */
    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    /** Scrolls an element into view using JavascriptExecutor. */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", element);
    }

    /** Scrolls to the bottom of the page using JavascriptExecutor. */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight);");
    }

    /** Clicks an element via JavaScript (useful for hidden or overlapped elements). */
    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /** Returns the current page title. */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /** Returns the current URL. */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Waits until the URL contains a given fragment. */
    protected void waitForUrlContaining(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    /** Waits until an element is present in the DOM (not necessarily visible). */
    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}