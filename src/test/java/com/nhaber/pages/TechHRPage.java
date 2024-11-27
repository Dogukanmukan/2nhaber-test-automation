package com.nhaber.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class TechHRPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "https://2ntech.com.tr/hr";

    // Form alanları için locator'lar
    private final By nameInput = By.name("name");
    private final By birthDateInput = By.cssSelector("input[type='date']");
    private final By tcNoInput = By.id("tcKimlik");
    private final By phoneInput = By.id("phone");
    private final By emailInput = By.id("email");
    private final By cvUploadInput = By.id("cv_field");
    private final By lisansButton = By.xpath("//button[contains(@class, 'py-3') and text()='Lisans']");
    private final By kvkkCheckbox = By.cssSelector("input[type='checkbox'][name='pdp1']");
    private final By nextButton = By.cssSelector("button[type='submit']");
    private final By positionSelect = By.xpath("//span[text()='Test Engineer']/ancestor::div[contains(@class, 'cursor-pointer')]");
    private final By submitButton = By.cssSelector("div.text-white.flex.justify-center.items-center.text-\\[14px\\].py-2.px-4.rounded-full.bg-\\[\\#DF1F29\\].cursor-pointer");
    private final By successMessage = By.cssSelector(".success-message, .alert-success");
    private final By errorMessage = By.cssSelector("div.text-sm.opacity-90");

    public TechHRPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(baseUrl);
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
    }

    public void fillFirstStep(String name, String birthDate, String tcNo, 
                            String phone, String email, String cvPath) {
        try {
            // Ad Soyad
            wait.until(ExpectedConditions.elementToBeClickable(nameInput)).sendKeys(name);
            
            // Doğum Tarihi
            driver.findElement(birthDateInput).sendKeys(birthDate);
            
            // TC Kimlik No
            driver.findElement(tcNoInput).sendKeys(tcNo);
            
            // Telefon
            driver.findElement(phoneInput).sendKeys(phone);
            
            // Email
            driver.findElement(emailInput).sendKeys(email);
            
            // CV Yükleme
            WebElement uploadElement = driver.findElement(cvUploadInput);
            uploadElement.sendKeys(cvPath);

            // Eğitim Seviyesi seçimi
            try {
                
                wait.until(ExpectedConditions.presenceOfElementLocated(lisansButton));
                
               
                WebElement lisansBtn = driver.findElement(lisansButton);
                
                // Görünür olmasını bekle
                wait.until(ExpectedConditions.elementToBeClickable(lisansBtn));
                
                // Butona scroll et
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    lisansBtn);
                
                
                Thread.sleep(500);
                
                // JavaScript ile tıkla
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", lisansBtn);
                
            } catch (Exception e) {
                takeScreenshot("education_selection_error");
                throw new RuntimeException("Eğitim seviyesi seçilirken hata oluştu: " + e.getMessage());
            }

            // KVKK Onayı
            try {
                WebElement kvkkElement = wait.until(ExpectedConditions.presenceOfElementLocated(kvkkCheckbox));
                
                // Checkbox'a scroll et
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    kvkkElement);
                
                Thread.sleep(500); // Scroll animasyonunun tamamlanması için kısa bekleme
                
                // Checkbox seçili değilse tıkla
                if (!kvkkElement.isSelected()) {
                    try {
                        kvkkElement.click();
                    } catch (ElementClickInterceptedException e) {
                        // Normal tıklama çalışmazsa JavaScript ile tıkla
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", kvkkElement);
                    }
                }
                
                // Checkbox'ın seçili olduğunu kontrol et
                if (!kvkkElement.isSelected()) {
                    throw new RuntimeException("KVKK onay kutusu işaretlenemedi!");
                }
                
            } catch (Exception e) {
                takeScreenshot("kvkk_checkbox_error");
                throw new RuntimeException("KVKK onay kutusu işaretlenirken hata oluştu: " + e.getMessage());
            }
            
            
            // İleri Butonu
            try {
                Thread.sleep(2000);
                
                // Submit butonunu bul ve tıklanabilir olmasını bekle
                WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
                
                // Butona scroll et
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", 
                    nextBtn);
                
                Thread.sleep(1000);
                
                // Enter tuşu ile submit et
                nextBtn.sendKeys(Keys.ENTER);
                
                // Test Engineer pozisyonunun görünür olmasını bekle
                wait.until(ExpectedConditions.visibilityOfElementLocated(positionSelect));
                
                Thread.sleep(2000);
                
            } catch (Exception e) {
                takeScreenshot("next_button_error");
                throw new RuntimeException("İleri butonuna tıklarken hata oluştu: " + e.getMessage());
            }
            
            takeScreenshot("first_step_completed");
        } catch (Exception e) {
            takeScreenshot("first_step_error");
            throw new RuntimeException("Form doldurulurken hata oluştu: " + e.getMessage());
        }
    }

    public boolean isSecondStepDisplayed() {
        try {
            // İkinci adımın yüklenmesi için biraz bekle
            Thread.sleep(2000);
            
            // Test Engineer pozisyonunun görünür olmasını bekle
            return wait.until(ExpectedConditions.visibilityOfElementLocated(positionSelect)) != null;
        } catch (Exception e) {
            takeScreenshot("second_step_check_error");
            return false;
        }
    }

    public void selectPosition(String position) {
        try {
            // Pozisyon elementinin yüklenmesini bekle
            Thread.sleep(2000); // Sayfanın tamamen yüklenmesi için bekle
            
            WebElement positionElement = wait.until(ExpectedConditions.elementToBeClickable(positionSelect));
            
            // Görünür olması için scroll
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                positionElement);
            
            Thread.sleep(1000); // Scroll sonrası bekle
            
            // JavaScript ile tıkla
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", positionElement);
            
            Thread.sleep(2000); // Seçim sonrası bekle
            
            takeScreenshot("position_selected");
        } catch (Exception e) {
            takeScreenshot("position_selection_error");
            throw new RuntimeException("Pozisyon seçilirken hata oluştu: " + e.getMessage());
        }
    }

    public void submitForm() {
        try {
            Thread.sleep(2000);
            
            WebElement submitBtn = wait.until(ExpectedConditions.presenceOfElementLocated(submitButton));
            
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", 
                submitBtn);
            
            Thread.sleep(1000);
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
            
            // Başarılı mesajı veya hata mesajını bekle
            try {
                // Önce hata mesajını kontrol et
                try {
                    WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
                    String messageText = errorMsg.getText();
                    
                    if (messageText.contains("daha önce bu programa başvurunuz bulunmaktadır")) {
                        takeScreenshot("duplicate_application_error");
                        System.out.println("Test başarılı: Mükerrer başvuru kontrolü çalışıyor");
                        return;
                    }
                } catch (Exception e) {
                    // Hata mesajı yoksa başarı mesajını kontrol et
                    wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
                }
                
            } catch (Exception e) {
                takeScreenshot("form_submission_error");
                throw new RuntimeException("Form gönderimi sırasında hata: " + e.getMessage());
            }
            
            takeScreenshot("form_submitted");
        } catch (Exception e) {
            takeScreenshot("form_submission_error");
            throw new RuntimeException("Form gönderilirken hata oluştu: " + e.getMessage());
        }
    }

    public boolean isSubmissionSuccessful() {
        try {
            // Önce hata mesajını kontrol et
            try {
                WebElement errorMsg = wait.until(ExpectedConditions.presenceOfElementLocated(errorMessage));
                String messageText = errorMsg.getText().toLowerCase();
                
                // Mükerrer başvuru kontrolü de başarılı bir test sonucu
                if (messageText.contains("daha önce") || messageText.contains("başvurunuz bulunmaktadır")) {
                    takeScreenshot("duplicate_application_detected");
                    System.out.println("Test başarılı: Mükerrer başvuru tespit edildi");
                    return true;
                }
            } catch (Exception e) {
                // Hata mesajı bulunamazsa başarı mesajını kontrol et
                try {
                    WebElement successMsg = wait.until(ExpectedConditions.presenceOfElementLocated(successMessage));
                    return successMsg.isDisplayed();
                } catch (Exception ex) {
                    // Her iki mesaj da bulunamazsa false dön
                    takeScreenshot("no_message_found");
                    return false;
                }
            }
            
            return false; // Hiçbir koşul sağlanmazsa
        } catch (Exception e) {
            takeScreenshot("submission_check_error");
            return false;
        }
    }

    private void takeScreenshot(String fileName) {
        try {
            File screenshotDir = new File("test-output/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = String.valueOf(System.currentTimeMillis());
            Path targetPath = Paths.get("test-output", "screenshots", 
                fileName.replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + timestamp + ".png");
                
            Files.copy(screenshot.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Screenshot alınamadı: " + e.getMessage());
        }
    }
}