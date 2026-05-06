package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * Page object for the public portfolio site.
 * Covers the homepage, navigation, contact form, and public pages.
 */
public class PublicPortfolioPage extends BasePage {

    // ── Navigation ────────────────────────────────────────────────────────────
    private final By navLinks      = By.cssSelector("nav a");
    private final By heroSection   = By.cssSelector(".hero, .home-hero, main section:first-child");

    // Complex XPath: nav link whose href ends with "/contact"
    private final By contactNavLink = By.xpath(
            "//nav//a[contains(@href,'/contact')]");

    // Complex XPath: nav link whose href ends with "/projects"
    private final By projectsNavLink = By.xpath(
            "//nav//a[contains(@href,'/projects')]");

    // ── Contact form ──────────────────────────────────────────────────────────
    private final By nameInput     = By.id("user_name");
    private final By emailInput    = By.id("user_email");
    private final By subjectInput  = By.id("subject");
    private final By messageArea   = By.id("message");
    private final By submitButton  = By.cssSelector(".contact-form__submit");

    // Complex XPath: success alert inside the contact section
    private final By successAlert  = By.xpath(
            "//section[contains(@class,'contact')]//div[contains(@class,'--success')]");

    // ── Static content ────────────────────────────────────────────────────────
    // Complex XPath: any h1 or h2 in the page body
    private final By anyHeading    = By.xpath("//main//h1 | //main//h2 | //body//h1 | //body//h2");

    public PublicPortfolioPage(WebDriver driver) {
        super(driver);
    }

    public PublicPortfolioPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public PublicPortfolioPage navigateTo(String url) {
        driver.get(url);
        return this;
    }

    // ── Navigation actions ────────────────────────────────────────────────────

    public PublicPortfolioPage goToContact() {
        click(contactNavLink);
        return this;
    }

    public PublicPortfolioPage goToProjects() {
        click(projectsNavLink);
        return this;
    }

    public PublicPortfolioPage goBack() {
        driver.navigate().back();
        return this;
    }

    public PublicPortfolioPage goForward() {
        driver.navigate().forward();
        return this;
    }

    // ── Contact form actions ───────────────────────────────────────────────────

    public PublicPortfolioPage fillContactName(String name) {
        fill(nameInput, name);
        return this;
    }

    public PublicPortfolioPage fillContactEmail(String email) {
        fill(emailInput, email);
        return this;
    }

    public PublicPortfolioPage fillContactSubject(String subject) {
        fill(subjectInput, subject);
        return this;
    }

    public PublicPortfolioPage fillContactMessage(String message) {
        fill(messageArea, message);
        return this;
    }

    public PublicPortfolioPage submitContactForm() {
        WebElement btn = waitForClickable(submitButton);
        scrollIntoView(btn);
        jsClick(btn);
        return this;
    }

    public boolean isContactSuccessVisible() {
        try {
            return waitForVisible(successAlert).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Assertion helpers ─────────────────────────────────────────────────────

    public boolean hasHeading() {
        try {
            List<WebElement> headings = driver.findElements(anyHeading);
            return !headings.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasNavLinks() {
        List<WebElement> links = driver.findElements(navLinks);
        return links.size() > 2;
    }

    /** Reads the first visible heading text on the current page. */
    public String getFirstHeading() {
        List<WebElement> headings = driver.findElements(anyHeading);
        return headings.isEmpty() ? "" : headings.get(0).getText().trim();
    }

    /** Hovers over the first nav link and returns the element for assertion. */
    public WebElement hoverOverFirstNavLink() {
        WebElement navLink = waitForVisible(navLinks);
        new Actions(driver).moveToElement(navLink).perform();
        return navLink;
    }

    /** Returns the CSS value of a property on the first nav link (for hover verification). */
    public String getFirstNavLinkCssValue(String property) {
        WebElement navLink = driver.findElement(navLinks);
        return navLink.getCssValue(property);
    }
}
