package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for the admin login page (/admin).
 * Encapsulates all interactions with the login form.
 */
public class AdminLoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By emailInput    = By.id("email");
    private final By passwordInput = By.id("password");
    private final By submitButton  = By.cssSelector(".admin-login__btn");
    private final By errorAlert    = By.cssSelector(".admin-alert--error");
    private final By loginCard     = By.cssSelector(".admin-login__card");

    // Complex XPath: finds the h1 inside the login card that contains "Admin"
    private final By loginTitle    = By.xpath(
            "//div[contains(@class,'admin-login__card')]//h1[contains(text(),'Admin')]");

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    /** Navigates directly to the admin login URL. */
    public AdminLoginPage open(String adminUrl) {
        driver.get(adminUrl);
        waitForVisible(loginCard);
        return this;
    }

    /** Fills the email field. */
    public AdminLoginPage enterEmail(String email) {
        fill(emailInput, email);
        return this;
    }

    /** Fills the password field. */
    public AdminLoginPage enterPassword(String password) {
        fill(passwordInput, password);
        return this;
    }

    /** Clicks the Sign In button. */
    public AdminDashboardPage clickSignIn() {
        click(submitButton);
        return new AdminDashboardPage(driver);
    }

    /** Full login flow in one call. Returns the dashboard page object. */
    public AdminDashboardPage loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickSignIn();
    }

    /** Returns the heading text (used to verify the page loaded). */
    public String getLoginHeading() {
        return getText(loginTitle);
    }

    /** Returns the error message shown after a failed login attempt. */
    public String getErrorMessage() {
        return getText(errorAlert);
    }

    /** Returns true if the error alert is currently displayed. */
    public boolean hasError() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return driver.findElement(errorAlert).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
