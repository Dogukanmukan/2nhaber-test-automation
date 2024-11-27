package com.nhaber.tests;

import com.nhaber.base.BaseTest;
import com.nhaber.pages.HomePage;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class NavbarTest extends BaseTest {
    
    

    @Test
    public void testNavigation() {
        HomePage homePage = new HomePage(driver);
        homePage.testAllNavbarElements();
    }
} 