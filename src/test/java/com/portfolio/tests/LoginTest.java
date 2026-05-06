package com.portfolio.tests;

import com.portfolio.pages.AdminDashboardPage;
import com.portfolio.pages.AdminLoginPage;
import com.portfolio.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the admin login / logout flow.
 * logoutRedirectsToLoginPage depends on loginWithValidCredentials
 * to demonstrate TestNG test dependencies.
 */
public class LoginTest extends BaseTest {

    @Test(description = "Login page loads with the correct heading and form elements")
    public void loginPageDisplaysCorrectTitle() {
        AdminLoginPage loginPage = new AdminLoginPage(driver)
                .open(ConfigReader.getAdminUrl());

        Assert.assertEquals(driver.getTitle().toLowerCase().contains("admin")
                || loginPage.getLoginHeading().contains("Admin"), true,
                "Login page should show Admin heading");
    }

    @Test(description = "Submitting wrong credentials shows an error alert")
    public void invalidCredentialsShowErrorMessage() {
        AdminLoginPage loginPage = new AdminLoginPage(driver)
                .open(ConfigReader.getAdminUrl());

        loginPage.enterEmail("wrong@email.com")
                 .enterPassword("badpassword")
                 .clickSignIn();

        Assert.assertTrue(loginPage.hasError(),
                "An error alert should appear after bad credentials");
    }

    @Test(description = "Valid credentials redirect to the admin dashboard")
    public void loginWithValidCredentials() {
        AdminDashboardPage dashboard = new AdminLoginPage(driver)
                .open(ConfigReader.getAdminUrl())
                .loginAs(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());

        dashboard.waitUntilLoaded();

        Assert.assertTrue(dashboard.isDashboardLoaded(),
                "Dashboard main content should be visible after login");
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"),
                "URL should contain 'dashboard' after successful login");
    }

    @Test(
        description = "Logging out redirects back to the login page",
        dependsOnMethods = "loginWithValidCredentials"
    )
    public void logoutRedirectsToLoginPage() {
        // Log in first
        AdminLoginPage loginPage = new AdminDashboardPage(driver) {{
            new AdminLoginPage(driver)
                    .open(ConfigReader.getAdminUrl())
                    .loginAs(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword())
                    .waitUntilLoaded();
        }}.logout();

        // After logout, URL should no longer contain "dashboard"
        Assert.assertFalse(driver.getCurrentUrl().contains("dashboard"),
                "Should be redirected away from dashboard after logout");
    }
}
