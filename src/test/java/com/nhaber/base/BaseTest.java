package com.nhaber.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        try {
            // Screenshot klasörünü oluştur
            File screenshotDir = new File("test-output/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // WebDriver ayarları
            WebDriverManager.chromedriver().setup();
            
            // Chrome options ayarları
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");
            
            // Normal sayfa yükleme stratejisi
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            
            // WebDriver'ı başlat
            driver = new ChromeDriver(options);
            
            // Timeout ayarları
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20)); 
            
            // Ana sayfaya git
            driver.get("https://2nhaber.com/");
            
            // Sayfa yüklenmesini bekle
            new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
            
        } catch (Exception e) {
            if (driver != null) {
                driver.quit();
            }
            throw new RuntimeException("Driver başlatılamadı: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}