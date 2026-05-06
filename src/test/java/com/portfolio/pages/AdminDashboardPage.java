package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page object for the admin dashboard (/admin/dashboard).
 * Covers tab navigation, project/cert/experience forms, and logout.
 */
public class AdminDashboardPage extends BasePage {

    // ── Top-level structure ───────────────────────────────────────────────────
    private final By sidebar        = By.cssSelector(".admin-sidebar");
    private final By logoutButton   = By.cssSelector(".admin-sidebar__logout");
    private final By mainContent    = By.cssSelector(".admin-main");

    // Complex XPath: sidebar nav button whose text contains a given label
    // Used via sidebarTabLocator(label) helper below

    // ── Add buttons ───────────────────────────────────────────────────────────
    private final By addProjectBtn  = By.xpath(
            "//button[contains(@class,'btn-primary') and .//text()[contains(.,'Add Project')]]");
    private final By addCertBtn     = By.xpath(
            "//button[contains(@class,'btn-primary') and .//text()[contains(.,'Add Certificate')]]");
    private final By addExpBtn      = By.xpath(
            "//button[contains(@class,'btn-primary') and .//text()[contains(.,'Add Experience')]]");

    // ── Modal fields (shared by project & cert modals) ────────────────────────
    private final By modalOverlay   = By.cssSelector(".admin-modal-overlay");
    private final By modalTitle     = By.cssSelector(".admin-modal__title");
    private final By modalSaveBtn   = By.xpath(
            "//div[contains(@class,'admin-modal__footer')]//button[contains(@class,'btn-primary')]");
    private final By modalCancelBtn = By.xpath(
            "//div[contains(@class,'admin-modal__footer')]//button[contains(@class,'btn-ghost')]");
    private final By modalError     = By.cssSelector(".admin-modal .admin-alert--error");

    // Project form fields
    private final By projectTitleInput  = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//input[@type='text'][1]");
    private final By projectDateInput   = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//input[@placeholder[contains(.,'2024')]]");
    private final By projectDescArea    = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//textarea[1]");
    private final By projectStackInput  = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//input[@placeholder[contains(.,'Python')]]");

    // Certificate form fields
    private final By certTitleInput     = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//input[@type='text'][1]");
    private final By certDateInput      = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//input[@placeholder[contains(.,'March')]]");
    private final By certDescArea       = By.xpath(
            "//div[contains(@class,'admin-modal__body')]//textarea[1]");

    // Experience form fields
    private final By expRoleInput       = By.xpath(
            "//input[@placeholder[contains(.,'Software Engineer')]]");
    private final By expCompanyInput    = By.xpath(
            "//input[@placeholder[contains(.,'Acme')]]");
    private final By expCategorySelect  = By.cssSelector(".admin-modal select");
    private final By expLocationInput   = By.xpath(
            "//input[@placeholder[contains(.,'Nairobi')]]");
    private final By expStartDateInput  = By.xpath(
            "//input[@placeholder[contains(.,'Jan 2023')]]");
    private final By expCurrentCheckbox = By.id("exp-current");

    // ── Locator helpers ───────────────────────────────────────────────────────

    /** XPath for a sidebar tab button by its visible label text. */
    private By sidebarTabLocator(String label) {
        return By.xpath("//button[contains(@class,'admin-sidebar__link') and .//text()[contains(.,'" + label + "')]]");
    }

    public AdminDashboardPage(WebDriver driver) {
        super(driver);
    }

    /** Waits until the dashboard main area is visible (confirms successful login). */
    public AdminDashboardPage waitUntilLoaded() {
        waitForVisible(mainContent); // Wait for main content to confirm dashboard loaded
        waitForClickable(sidebarTabLocator("Projects")); // Wait until at least one sidebar tab button is clickable
        return this;
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public AdminDashboardPage openProjectsTab() {
        click(sidebarTabLocator("Projects"));
        return this;
    }

    public AdminDashboardPage openCertsTab() {
        click(sidebarTabLocator("Certificates"));
        return this;
    }

    public AdminDashboardPage openExperienceTab() {
        click(sidebarTabLocator("Experience"));
        return this;
    }

    public AdminDashboardPage openSettingsTab() {
        click(sidebarTabLocator("Site Content"));
        return this;
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    /** Clicks the Sign Out button and returns the login page object. */
    public AdminLoginPage logout() {
        click(logoutButton);
        return new AdminLoginPage(driver);
    }

    // ── Project form ──────────────────────────────────────────────────────────

    public AdminDashboardPage clickAddProject() {
        waitForVisible(addProjectBtn);
        click(addProjectBtn);
        waitForVisible(modalOverlay);
        return this;
    }

    public AdminDashboardPage fillProjectTitle(String title) {
        fill(projectTitleInput, title);
        return this;
    }

    public AdminDashboardPage fillProjectDate(String date) {
        fill(projectDateInput, date);
        return this;
    }

    public AdminDashboardPage fillProjectDescription(String desc) {
        fill(projectDescArea, desc);
        return this;
    }

    public AdminDashboardPage fillProjectStack(String stack) {
        fill(projectStackInput, stack);
        return this;
    }

    public AdminDashboardPage saveModal() {
        click(modalSaveBtn);
        // Wait for modal to close
        wait.until(driver -> {
            try { driver.findElement(modalOverlay); return false; }
            catch (Exception e) { return true; }
        });
        return this;
    }

    public AdminDashboardPage cancelModal() {
        click(modalCancelBtn);
        return this;
    }

    /** Adds a complete project entry using random data. Returns the title used. */
    public String addProjectWithRandomData(String title, String date, String desc, String stack) {
        clickAddProject();
        fillProjectTitle(title);
        fillProjectDate(date);
        fillProjectDescription(desc);
        fillProjectStack(stack);
        saveModal();
        return title;
    }

    // ── Certificate form ──────────────────────────────────────────────────────

    public AdminDashboardPage clickAddCertificate() {
        waitForVisible(addCertBtn);
        click(addCertBtn);
        waitForVisible(modalOverlay);
        return this;
    }

    public AdminDashboardPage fillCertTitle(String title) {
        fill(certTitleInput, title);
        return this;
    }

    public AdminDashboardPage fillCertDate(String date) {
        fill(certDateInput, date);
        return this;
    }

    public AdminDashboardPage fillCertDescription(String desc) {
        fill(certDescArea, desc);
        return this;
    }

    // ── Experience form ───────────────────────────────────────────────────────

    public AdminDashboardPage clickAddExperience() {
        waitForVisible(addExpBtn);
        click(addExpBtn);
        waitForVisible(modalOverlay);
        return this;
    }

    public AdminDashboardPage fillExpRole(String role) {
        fill(expRoleInput, role);
        return this;
    }

    public AdminDashboardPage fillExpCompany(String company) {
        fill(expCompanyInput, company);
        return this;
    }

    /** Selects experience category using the Select class (dropdown). */
    public AdminDashboardPage selectExpCategory(String category) {
        WebElement selectEl = waitForVisible(expCategorySelect);
        new Select(selectEl).selectByValue(category);
        return this;
    }

    public AdminDashboardPage fillExpLocation(String location) {
        fill(expLocationInput, location);
        return this;
    }

    public AdminDashboardPage fillExpStartDate(String startDate) {
        fill(expStartDateInput, startDate);
        return this;
    }

    public AdminDashboardPage checkCurrentlyWorkingHere() {
        WebElement checkbox = waitForVisible(expCurrentCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    // ── Assertions helpers ────────────────────────────────────────────────────

    public boolean isDashboardLoaded() {
        try {
            return driver.findElement(mainContent).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getModalTitle() {
        return getText(modalTitle);
    }

    /** Returns the current tab's h1 heading text. */
    public String getMainHeading() {
        return getText(By.cssSelector(".admin-main__title"));
    }

    /** Complex XPath: counts how many table rows exist in the admin table. */
    public int countTableRows() {
        List<WebElement> rows = driver.findElements(
                By.xpath("//table[contains(@class,'admin-table')]/tbody/tr[not(contains(@style,'display:none'))]"));
        return rows.size();
    }
}
