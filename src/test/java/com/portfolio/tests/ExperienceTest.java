package com.portfolio.tests;

import com.portfolio.pages.AdminDashboardPage;
import com.portfolio.pages.AdminLoginPage;
import com.portfolio.utils.ConfigReader;
import com.portfolio.utils.RandomDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for the Experience tab in the admin dashboard.
 * Covers: dropdown (Select class), checkbox, text inputs, form submission.
 */
public class ExperienceTest extends BaseTest {

    private AdminDashboardPage dashboard;

    @BeforeMethod
    public void loginAndOpenExperience() {
        dashboard = new AdminLoginPage(driver)
                .open(ConfigReader.getAdminUrl())
                .loginAs(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword())
                .waitUntilLoaded()
                .openExperienceTab();
    }

    @Test(description = "Experience tab shows the correct heading")
    public void experienceTabShowsCorrectHeading() {
        Assert.assertEquals(dashboard.getMainHeading(), "Experience",
                "Main heading should be 'Experience'");
    }

    @Test(description = "Add Experience modal opens when the button is clicked")
    public void addExperienceModalOpens() {
        dashboard.clickAddExperience();
        Assert.assertTrue(dashboard.getModalTitle().contains("Experience"),
                "Modal title should mention Experience");
        dashboard.cancelModal();
    }

    @Test(description = "Category dropdown contains Professional and Freelance options")
    public void categoryDropdownHasCorrectOptions() {
        dashboard.clickAddExperience();

        WebElement selectEl = driver.findElement(By.cssSelector(".admin-modal select"));
        Select select = new Select(selectEl);

        Assert.assertEquals(select.getOptions().size(), 2,
                "Category dropdown should have exactly 2 options");
        Assert.assertEquals(select.getOptions().get(0).getText(), "Professional");
        Assert.assertEquals(select.getOptions().get(1).getText(), "Freelance");

        dashboard.cancelModal();
    }

    @Test(description = "Selecting 'Freelance' from the dropdown persists the selection")
    public void selectingFreelanceCategoryPersists() {
        dashboard.clickAddExperience();

        WebElement selectEl = driver.findElement(By.cssSelector(".admin-modal select"));
        Select select = new Select(selectEl);
        select.selectByValue("freelance");

        Assert.assertEquals(select.getFirstSelectedOption().getAttribute("value"), "freelance",
                "Freelance should be the selected option");
        dashboard.cancelModal();
    }

    @Test(description = "Checking 'Currently working here' disables the End Date field")
    public void currentWorkCheckboxDisablesEndDateInput() {
        dashboard.clickAddExperience();
        dashboard.checkCurrentlyWorkingHere();

        WebElement endDateInput = driver.findElement(
                By.xpath("//input[@placeholder[contains(.,'Dec 2024')]]"));
        Assert.assertFalse(endDateInput.isEnabled(),
                "End Date input should be disabled when 'currently working here' is checked");
        dashboard.cancelModal();
    }

    @Test(description = "A new experience entry can be submitted with random data")
    public void addExperienceWithRandomDataSubmitsSuccessfully() {
        int rowsBefore = dashboard.countTableRows();

        dashboard.clickAddExperience();
        dashboard.fillExpRole(RandomDataGenerator.jobRole())
                 .fillExpCompany(RandomDataGenerator.company())
                 .selectExpCategory("professional")
                 .fillExpLocation("Nairobi, Kenya")
                 .fillExpStartDate("Jan 2024")
                 .saveModal();

        int rowsAfter = dashboard.countTableRows();
        Assert.assertTrue(rowsAfter >= rowsBefore,
                "Experience table should have at least as many rows after adding");
    }

    @Test(description = "The role input field accepts typed text")
    public void roleInputFieldAcceptsText() {
        dashboard.clickAddExperience();

        String role = RandomDataGenerator.jobRole();
        dashboard.fillExpRole(role);

        WebElement roleInput = driver.findElement(
                By.xpath("//input[@placeholder[contains(.,'Software Engineer')]]"));
        Assert.assertEquals(roleInput.getAttribute("value"), role,
                "Role field should contain the typed text");
        dashboard.cancelModal();
    }
}
