package com.nhaber.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private final String baseUrl = "https://2nhaber.com/";

    private final By searchTrigger = By.cssSelector(".elementor-widget-cmsmasters-search__popup-trigger-inner");
    private final By searchInput = By.cssSelector(".elementor-widget-cmsmasters-search__field");
    private final By submitButton = By.cssSelector(".elementor-search-form__submit");
    private final By searchResults = By.cssSelector(".cmsmasters-blog__posts article");

    private final By mainMenuItems = By.cssSelector(".elementor-widget-cmsmasters-nav-menu__container-inner > li > a");
    private final By subMenuItems = By.cssSelector(".elementor-widget-cmsmasters-nav-menu__item-link-sub");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
        
        if (!driver.getCurrentUrl().equals(baseUrl)) {
            driver.get(baseUrl);
        }
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
    }

    public void performSearch(String searchText) {
        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            WebElement searchButton = wait.until(ExpectedConditions.presenceOfElementLocated(searchTrigger));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
            
            Thread.sleep(1000);
            
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
            input.clear();
            input.sendKeys(searchText);
            
            input.sendKeys(Keys.ENTER);
            
            try {
                WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
                submit.click();
            } catch (TimeoutException e) {
                System.out.println("Submit butonu bulunamadı, Enter tuşu kullanıldı");
            }

        } catch (Exception e) {
            System.err.println("Arama yapılırken hata oluştu: " + e.getMessage());
            takeScreenshot("search_error_" + System.currentTimeMillis());
            throw new RuntimeException("Arama işlemi başarısız", e);
        }
    }

    public void waitForSearchResults() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(searchResults));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickSearchResult(int index) {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            List<WebElement> results = longWait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(searchResults));
            
            if (results.size() >= index) {
                WebElement targetResult = results.get(index - 1);
                
                longWait.until(ExpectedConditions.visibilityOf(targetResult));
                
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", 
                    targetResult);
                
                Thread.sleep(1000);
                
                longWait.until(ExpectedConditions.elementToBeClickable(targetResult));
                
                try {
                    targetResult.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetResult);
                }
                
                longWait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
                
            } else {
                throw new IndexOutOfBoundsException(
                    "İstenen index (" + index + ") bulunan sonuç sayısından (" + 
                    results.size() + ") büyük");
            }
        } catch (Exception e) {
            System.err.println("Arama sonucuna tıklarken hata: " + e.getMessage());
            takeScreenshot("click_error_" + System.currentTimeMillis());
            throw new RuntimeException("Arama sonucuna tıklama başarısız", e);
        }
    }

    public void testAllNavbarElements() {
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        
        try {
            List<WebElement> mainMenus = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(mainMenuItems));
            
            if (mainMenus.isEmpty()) {
                throw new NoSuchElementException("Ana menü elementleri bulunamadı");
            }

            String currentUrl = driver.getCurrentUrl();
            
            for (WebElement mainMenu : mainMenus) {
                try {
                    String mainMenuText = mainMenu.getText();
                    System.out.println("Testing main menu: " + mainMenuText);
                    
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));", 
                        mainMenu);
                    
                    Thread.sleep(500);
                    
                    List<WebElement> subMenus = driver.findElements(subMenuItems);
                    
                    if (!subMenus.isEmpty()) {
                        for (WebElement subMenu : subMenus) {
                            String subMenuText = subMenu.getText();
                            String subMenuUrl = subMenu.getAttribute("href");
                            
                            System.out.println("Testing submenu: " + subMenuText);
                            
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", subMenu);
                            
                            verifyPageLoad(subMenuUrl);
                            takeScreenshot(mainMenuText + "_" + subMenuText);
                            
                            driver.navigate().to(currentUrl);
                            wait.until(ExpectedConditions.jsReturnsValue(
                                "return document.readyState === 'complete'"));
                            
                            mainMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.xpath(String.format("//a[contains(text(),'%s')]", mainMenuText))));
                        }
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error in menu: " + e.getMessage());
                    takeScreenshot("error_" + System.currentTimeMillis());
                }
            }
        } catch (Exception e) {
            System.err.println("Critical error in navbar test: " + e.getMessage());
            takeScreenshot("critical_error_" + System.currentTimeMillis());
        }
    }

    private void verifyPageLoad(String expectedUrl) {
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), 
                      Paths.get("test-output/screenshots/" + 
                              fileName.replaceAll("[^a-zA-Z0-9.-]", "_") + 
                              ".png"));
        } catch (IOException e) {
            System.err.println("Screenshot alınamadı: " + e.getMessage());
        }
    }

    public boolean isMainMenuPresent(String menuText) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(String.format("//a[contains(text(),'%s')]", menuText))
            )).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
} 