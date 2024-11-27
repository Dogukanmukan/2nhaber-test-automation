package com.nhaber.tests;

import com.nhaber.base.BaseTest;
import com.nhaber.pages.TechHRPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TechHRFormTest extends BaseTest {
    
    @Test
    public void testHRFormSubmission() {
        TechHRPage formPage = new TechHRPage(driver);
        
        // 1. adım için test verileri
        String name = "Test Kullanıcı";
        String birthDate = "01.11.2024";
        String tcNo = "11111111111";
        String phone = "05448445488";
        String email = "test@gmail.com";
        String cvPath = System.getProperty("user.dir") + "/src/test/java/com/nhaber/resources/test_cv.pdf";
        
        formPage.fillFirstStep(name, birthDate, tcNo, phone, email, cvPath);
        
        // 1. adımdan 2. adıma geçildiğini kontrol et
        Assert.assertTrue(formPage.isSecondStepDisplayed(), 
            "Form'un ikinci adımına geçilemedi!");

        // 2. adım - Test Engineer pozisyonunu seç ve gönder
        formPage.selectPosition("Test Engineer");
        formPage.submitForm();
        
        // Başarılı gönderimi kontrol et
        Assert.assertTrue(formPage.isSubmissionSuccessful(), 
            "Form başarıyla gönderilemedi!");
    }
} 