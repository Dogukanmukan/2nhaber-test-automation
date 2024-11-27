# 2NHaber Test Automation Project

Bu proje, 2NHaber web sitesinin otomatik test süitini içermektedir. Selenium WebDriver ve TestNG framework'ü kullanılarak geliştirilmiştir.

## 🚀 Özellikler

- UI Testleri (Navigasyon, Arama)
- Form Validasyon Testleri
- Mükerrer Başvuru Kontrolleri
- Screenshot'lar ile Görsel Doğrulama
- Performans Testleri

## 🛠️ Kullanılan Teknolojiler

- Java 11
- Selenium WebDriver 4.18.1
- TestNG 7.9.0
- WebDriverManager 5.7.0
- Maven
- Chrome Driver 131.0.6778.85

## 📋 Test Kapsamı

### 1. UI Testleri
- Ana Menü Navigasyonu
- Arama Fonksiyonu
- Sayfa Yüklenme Kontrolleri

### 2. Form Testleri
- İş Başvuru Formu Validasyonları
- Dosya Yükleme
- KVKK Onayı
- Mükerrer Başvuru Kontrolü

## 🏃‍♂️ Testleri Çalıştırma 

bash
Projeyi klonlayın
git clone https://github.com/yourusername/2nhaber-test-automation.git

Proje dizinine gidin
cd 2nhaber-test-automation

Bağımlılıkları yükleyin ve testleri çalıştırın
mvn clean test


## 📁 Proje Yapısı
2nhaber-test-automation/
├── docs/
│ ├── test-documentation/
│ ├── test-plans/
│ └── test-reports/
├── src/
│ └── test/
│ └── java/
│ └── com/
│ └── nhaber/
│ ├── base/
│ ├── pages/
│ └── tests/
├── test-output/
│ └── screenshots/
├── pom.xml
└── testng.xml

## 📊 Test Sonuçları

### UI Testleri
- Ana Menü Testi: ✅ Başarılı
- Arama Testi: ✅ Başarılı

### Form Testleri
- İş Başvuru Formu: ✅ Başarılı
  - Form Validasyonları: Geçti
  - Dosya Yükleme: Geçti
  - KVKK Onayı: Geçti
  - Mükerrer Başvuru: Geçti

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.