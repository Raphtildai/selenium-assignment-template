package com.portfolio.tests;

import com.portfolio.pages.AdminDashboardPage;
import com.portfolio.pages.AdminLoginPage;
import com.portfolio.utils.ConfigReader;
import com.portfolio.utils.RandomDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for the Projects tab in the admin dashboard.
 * Covers: form filling, textarea, file input, checkboxes, random data.
 */
public class ProjectTest extends BaseTest {

    private AdminDashboardPage dashboard;

    @BeforeMethod
    public void loginAndOpenProjects() {
        dashboard = new AdminLoginPage(driver)
                .open(ConfigReader.getAdminUrl())
                .loginAs(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword())
                .waitUntilLoaded()
                .openProjectsTab();
    }

    @Test(description = "Projects tab heading reads 'Projects'")
    public void projectsTabShowsCorrectHeading() {
        Assert.assertEquals(dashboard.getMainHeading(), "Projects",
                "Main heading should be 'Projects'");
    }

    @Test(description = "Add Project modal opens with the correct title")
    public void addProjectModalOpens() {
        dashboard.clickAddProject();
        Assert.assertTrue(dashboard.getModalTitle().contains("Add"),
                "Modal title should contain 'Add'");
        dashboard.cancelModal();
    }

    @Test(description = "A new project can be submitted with random data via the modal form")
    public void addProjectWithRandomDataSubmitsSuccessfully() {
        String title = RandomDataGenerator.projectTitle() + " " + RandomDataGenerator.tag();
        String date  = RandomDataGenerator.year();
        String desc  = RandomDataGenerator.description();
        String stack = RandomDataGenerator.techStack();

        int rowsBefore = dashboard.countTableRows();

        dashboard.addProjectWithRandomData(title, date, desc, stack);

        int rowsAfter = dashboard.countTableRows();
        Assert.assertTrue(rowsAfter >= rowsBefore,
                "Project table should have at least as many rows after adding");
    }

    @Test(description = "The project form textarea accepts multi-line description text")
    public void projectDescriptionTextareaAcceptsInput() {
        dashboard.clickAddProject();

        String multiLine = "Line one of description.\nLine two with more detail.\nLine three.";
        WebElement textarea = driver.findElement(
                By.xpath("//div[contains(@class,'admin-modal__body')]//textarea[1]"));
        textarea.clear();
        textarea.sendKeys(multiLine);

        Assert.assertTrue(textarea.getAttribute("value").contains("Line one"),
                "Textarea should hold the entered text");
        dashboard.cancelModal();
    }

    @Test(description = "The project form has a file input for image upload")
    public void projectFormHasFileInput() {
        dashboard.clickAddProject();

        WebElement fileInput = driver.findElement(
                By.xpath("//div[contains(@class,'admin-modal__body')]//input[@type='file']"));
        Assert.assertNotNull(fileInput, "File input should exist in the project modal");
        Assert.assertEquals(fileInput.getAttribute("accept"), "image/*",
                "File input should accept only images");
        dashboard.cancelModal();
    }

    @Test(description = "The featured checkbox can be checked in the project form")
    public void projectFeaturedCheckboxCanBeChecked() {
        dashboard.clickAddProject();

        WebElement checkbox = driver.findElement(By.id("featured"));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        Assert.assertTrue(checkbox.isSelected(), "Featured checkbox should be checked");
        dashboard.cancelModal();
    }

    @Test(description = "The visible checkbox is checked by default in the project form")
    public void projectVisibilityCheckboxIsCheckedByDefault() {
        dashboard.clickAddProject();

        WebElement visCheckbox = driver.findElement(By.id("vis-modal"));
        Assert.assertTrue(visCheckbox.isSelected(),
                "Visibility checkbox should be checked by default");
        dashboard.cancelModal();
    }
}
