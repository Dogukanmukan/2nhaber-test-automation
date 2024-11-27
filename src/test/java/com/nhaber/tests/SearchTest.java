package com.nhaber.tests;

import com.nhaber.base.BaseTest;
import com.nhaber.pages.HomePage;
import org.testng.annotations.Test;
import org.testng.Assert;

public class SearchTest extends BaseTest {
    
    @Test
    public void testSearch() {
        HomePage homePage = new HomePage(driver);
        
        try {
            // Ana sayfanın yüklenmesi için daha uzun bekleme
            Thread.sleep(3000);
            
            // Arama yap
            homePage.performSearch("İstanbul");
            
            // Sonuçları bekle
            homePage.waitForSearchResults();
            
            // Sonuçların yüklenmesi için ek bekleme
            Thread.sleep(2000);
            
            // 8. sonuca tıkla
            homePage.clickSearchResult(8);
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test başarısız: " + e.getMessage());
        }
    }
} 