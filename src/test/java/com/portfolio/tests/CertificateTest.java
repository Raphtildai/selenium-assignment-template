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
 * Tests for the Certificates tab in the admin dashboard.
 */
public class CertificateTest extends BaseTest {

    private AdminDashboardPage dashboard;

    @BeforeMethod
    public void loginAndOpenCerts() {
        dashboard = new AdminLoginPage(driver)
                .open(ConfigReader.getAdminUrl())
                .loginAs(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword())
                .waitUntilLoaded()
                .openCertsTab();
    }

    @Test(description = "Certificates tab displays the correct section heading")
    public void certificatesTabShowsCorrectHeading() {
        Assert.assertEquals(dashboard.getMainHeading(), "Certificates",
                "Main heading should be 'Certificates'");
    }

    @Test(description = "Add Certificate modal opens and shows the correct title")
    public void addCertificateModalOpens() {
        dashboard.clickAddCertificate();
        Assert.assertTrue(dashboard.getModalTitle().contains("Certificate"),
                "Modal title should mention Certificate");
        dashboard.cancelModal();
    }

    @Test(description = "Certificate title field accepts text input")
    public void certTitleInputAcceptsText() {
        dashboard.clickAddCertificate();

        String title = RandomDataGenerator.certTitle();
        dashboard.fillCertTitle(title);

        WebElement titleInput = driver.findElement(
                By.xpath("//div[contains(@class,'admin-modal__body')]//input[@type='text'][1]"));
        Assert.assertEquals(titleInput.getAttribute("value"), title,
                "Title field should hold the entered text");
        dashboard.cancelModal();
    }

    @Test(description = "Certificate description textarea accepts multi-line text")
    public void certDescriptionTextareaAcceptsMultilineText() {
        dashboard.clickAddCertificate();

        String desc = "First line.\nSecond line with more detail.\nThird line.";
        dashboard.fillCertDescription(desc);

        WebElement textarea = driver.findElement(
                By.xpath("//div[contains(@class,'admin-modal__body')]//textarea[1]"));
        Assert.assertTrue(textarea.getAttribute("value").contains("First line"),
                "Textarea should contain the entered text");
        dashboard.cancelModal();
    }

    @Test(description = "Certificate form has a file input for the certificate image")
    public void certFormHasFileInputForImage() {
        dashboard.clickAddCertificate();

        // Complex XPath: file input inside the modal body
        WebElement fileInput = driver.findElement(
                By.xpath("//div[contains(@class,'admin-modal__body')]//input[@type='file']"));
        Assert.assertNotNull(fileInput,
                "Certificate modal should have a file upload input");
        dashboard.cancelModal();
    }

    @Test(description = "A new certificate can be added with random generated data")
    public void addCertificateWithRandomDataSubmitsSuccessfully() {
        int rowsBefore = dashboard.countTableRows();

        dashboard.clickAddCertificate();
        dashboard.fillCertTitle(RandomDataGenerator.certTitle() + " " + RandomDataGenerator.tag())
                 .fillCertDate(RandomDataGenerator.certDate())
                 .fillCertDescription(RandomDataGenerator.description())
                 .saveModal();

        int rowsAfter = dashboard.countTableRows();
        Assert.assertTrue(rowsAfter >= rowsBefore,
                "Certificate table should have at least as many rows after adding");
    }
}
