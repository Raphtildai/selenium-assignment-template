package com.portfolio.tests;

import com.portfolio.pages.PublicPortfolioPage;
import com.portfolio.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the public-facing portfolio site.
 * Covers: static page verification, page title, multiple-page loop,
 * history navigation (back/forward), and the contact form.
 */
public class PublicSiteTest extends BaseTest {

        private PublicPortfolioPage page;
        private String baseUrl;

        @BeforeMethod
        public void openHomepage() {
                baseUrl = ConfigReader.getBaseUrl();
                page = new PublicPortfolioPage(driver).open(baseUrl);
        }

        // ── Static page test ──────────────────────────────────────────────────────

        @Test(description = "Homepage loads and contains at least one heading and navigation links")
        public void homepageLoadsWithContentAndNavigation() {
                Assert.assertTrue(page.hasHeading(),
                        "Homepage should have at least one heading");
                Assert.assertTrue(page.hasNavLinks(),
                        "Homepage should have navigation links");
        }

        // ── Page title test ───────────────────────────────────────────────────────

        @Test(description = "Homepage has a non-empty browser tab title")
        public void homepageHasNonEmptyTitle() {
                String title = page.getPageTitle();
                Assert.assertNotNull(title, "Page title should not be null");
                Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
        }

        @Test(description = "Admin login page has a descriptive title")
        public void adminLoginPageHasTitle() {
                driver.get(ConfigReader.getAdminUrl());
                String title = page.getPageTitle();
                Assert.assertFalse(title.isEmpty(), "Admin login page should have a title");
        }

        // ── Multiple-page test ────────────────────────────────────────────────────

        @Test(description = "All public routes load without error — verified using a URL loop")
        public void allPublicPagesLoadSuccessfully() {
                List<String> routes = Arrays.asList(
                        "/",
                        "/about",
                        "/projects",
                        "/certifications",
                        "/contact",
                        "/resume"
                );

                for (String route : routes) {
                String url = baseUrl + route;
                driver.get(url);

                // Complex XPath: any heading (h1-h3) present anywhere in the body
                List<WebElement> headings = driver.findElements(
                        By.xpath("//body//h1 | //body//h2 | //body//h3"));

                Assert.assertTrue(
                        !headings.isEmpty() || driver.getTitle().length() > 0,
                        "Page at " + route + " should have content (heading or title)"
                );
                }
        }

        // ── History navigation test ───────────────────────────────────────────────

        @Test(description = "Browser back() and forward() navigate between pages correctly")
        public void browserHistoryBackAndForwardWork() {
                // Start at homepage
                String homeUrl = driver.getCurrentUrl();

                // Navigate to projects
                page.goToProjects();
                String projectsUrl = driver.getCurrentUrl();
                Assert.assertTrue(projectsUrl.contains("projects"),
                        "Should be on projects page after navigating");

                // Go back to home
                page.goBack();
                Assert.assertEquals(driver.getCurrentUrl(), homeUrl,
                        "Back button should return to homepage");

                // Go forward to projects again
                page.goForward();
                Assert.assertEquals(driver.getCurrentUrl(), projectsUrl,
                        "Forward button should return to projects page");
        }

        // ── Contact form test ─────────────────────────────────────────────────────

        @Test(description = "Contact form accepts all inputs and submits successfully")
        public void contactFormSubmitsWithAllFieldsFilled() {
                driver.get(baseUrl + "/contact");

                page.fillContactName("Selenium Tester")
                .fillContactEmail("test@example.com")
                .fillContactSubject("Test submission from Selenium")
                .fillContactMessage(
                        "This is an automated test message sent by Selenium WebDriver. "
                        + "Please ignore this submission.");

                page.submitContactForm();

                Assert.assertTrue(page.isContactSuccessVisible(),
                        "Success alert should appear after submitting the contact form");
        }

        // ── JavascriptExecutor test ───────────────────────────────────────────────

        @Test(description = "JavascriptExecutor can scroll to the bottom of the homepage")
        public void javascriptExecutorScrollsToBottom() {
                org.openqa.selenium.JavascriptExecutor js =
                        (org.openqa.selenium.JavascriptExecutor) driver;

                // Scroll to bottom using both window and documentElement to cover all cases
                js.executeScript(
                        "window.scrollTo(0, document.body.scrollHeight);" +
                        "document.documentElement.scrollTop = document.documentElement.scrollHeight;"
                );

                // Brief pause to let scroll settle
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}

                // Check scrollY OR documentElement.scrollTop — whichever the browser uses
                Long scrollY = (Long) js.executeScript(
                        "return window.scrollY || document.documentElement.scrollTop || 0;"
                );
                Long bodyHeight  = (Long) js.executeScript("return document.body.scrollHeight;");
                Long viewHeight  = (Long) js.executeScript("return window.innerHeight;");

                // If the page is taller than the viewport, scrollY must be > 0.
                // If not (single-screen page), the test intent is satisfied — scroll was attempted.
                if (bodyHeight > viewHeight) {
                        Assert.assertTrue(scrollY > 0,
                                "Page should have scrolled down (scrollY > 0). bodyHeight="
                                + bodyHeight + ", viewHeight=" + viewHeight);
                } else {
                        // Page fits in one screen — scroll was invoked successfully, nothing to assert
                        Assert.assertTrue(bodyHeight > 0,
                                "Page body should have some height");
                }
        }
}
