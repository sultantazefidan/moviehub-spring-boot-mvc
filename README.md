# MovieHub – Spring Boot MVC Movie Watchlist Application

MovieHub, kullanıcıların filmleri takip edebildiği, ekleyip düzenleyebildiği ve yönetebildiği  
**Spring Boot MVC tabanlı** bir web uygulamasıdır.

Proje; **backend** ve **frontend** geliştirme süreçlerinin yanı sıra, uygulamanın iş kurallarının,
veri akışlarının ve kullanıcı etkileşimlerinin doğruluğunu güvence altına almak amacıyla
**kapsamlı test süreçleriyle desteklenerek** geliştirilmiştir.


Uygulama, yalnızca teknik işlevleri değil; **kullanıcı deneyimini (UX)** de ön planda tutacak biçimde tasarlanmıştır.

---

## Kullanılan Teknolojiler
- Java 17  
- Spring Boot (MVC)  
- Spring Data JPA  
- Thymeleaf  
- MySQL  
- HTML / CSS / Bootstrap  
- Maven  
- JUnit 5 & MockMvc  

---

## Giriş Bilgileri (Demo)
> Test ve demo amaçlıdır.

- **Kullanıcı Adı:** admin  
- **Şifre:** 123  

---

## Uygulanan Test Türleri

Bu projede **8 farklı test türü** uygulanmıştır:

1. Unit Test (Birim Testi)  
2. Integration Test (Entegrasyon Testi)  
3. UI Test (Arayüz Testi)  
4. Regression Test (Regresyon Testi)  
5. End-to-End Test (Uçtan Uca Test)  
6. User Acceptance Test (Kullanıcı Kabul Testi)  
7. Smoke Test  
8. System Test (Sistem Testi)  

> Tüm testler `src/test/java` altında paketlenmiş şekilde bulunmaktadır.

---

## Uygulama Özellikleri
- Kullanıcı girişi (Login)
- Film ekleme / düzenleme / silme (CRUD)
- Film arama
- Türlere göre filtreleme
- İzlenme durumu ve puanlama
- Film görseli yükleme
- Yönetim paneli (Film Yönetimi)
- Film kartlarına tıklandığında **doğrudan izleme bağlantısına yönlendirme**
- Film ekleme sırasında izleme linki tanımlama ve  
  izlenen veya ileride izlenecek filmler için kısa kullanıcı notları ekleme

---

## Kullanıcı Deneyimi (UX) Odaklı Tasarım

Uygulama, **gerçek kullanıcı senaryoları** dikkate alınarak geliştirilmiştir:

- CRUD işlemleri (ekleme, güncelleme, silme) sonrasında  
  kullanıcıya **arayüz üzerinden anlık bildirimler** gösterilir  
  (örn: *“Film başarıyla eklendi”, “Film güncellendi”, “Film silindi”*).

- Butonlar, yönlendirmeler ve formlar kullanıcıya  
  **“gerçek bir web uygulaması” hissi** verecek şekilde tasarlanmıştır.

- Film kartlarına tıklandığında izleme linkine yönlendirme yapılması,  
  uygulamanın **film platformu deneyimini** güçlendirmektedir.

---

## Ekran Görüntüleri

Uygulamanın çalışır haline ait ekran görüntüleri  
`screenshots/` klasörü altında yer almaktadır.

Örnek olarak aşağıdaki ekranlar yer almaktadır:
- Admin / Film Yönetimi ekranı  
- Film ekleme formu  
- Film güncelleme ekranı  
- CRUD işlemleri sonrası kullanıcı bildirimleri  

### Admin Dashboard & Movie Management
![Admin Dashboard](screenshots/app-ui/admin-dashboard-overview.png)

### Movie Update – Success Notification
![Movie Update Success](screenshots/app-ui/movie-update-success.png)

---

## Proje Klasör Yapısı

```text
moviehub-spring-boot-mvc
├── src
│   ├── main
│   │   ├── java               
│   │   └── resources           
│   └── test
│       └── java                
│
├── screenshots
│   ├── app-ui                 
│   │   ├── login-page.png
│   │   ├── home-movie-grid.png
│   │   ├── add-movie-form.png
│   │   ├── edit-movie-form.png
│   │   ├── movie-management-table.png
│   │   ├── movie-search-filter.png
│   │   └── movie-update-success.png
│   │
│   └── tests                   
│       ├── integration-tests-passed.png
│       ├── ui-tests-passed.png
│       └── e2e-movie-flow-test.png
│
├── .gitignore                
├── .gitattributes            
├── mvnw                     
├── mvnw.cmd                  
├── pom.xml                     
├── LICENSE                     
└── README.md                  

```

## Notlar
- `application.properties` dosyası güvenlik nedeniyle repoya eklenmemiştir.
- Örnek yapı için `application-example.properties` kullanılabilir.
- Bu proje, modern web uygulamalarında kullanılan mimari,
  test stratejileri ve kullanıcı deneyimi yaklaşımlarını
  uygulamalı olarak göstermek amacıyla geliştirilmiştir.
- Kullanıcı kayıt (Register) özelliği, sonraki sürümler için planlanmıştır.
- - Test süreçlerinin başarıyla tamamlandığını göstermek amacıyla,
  tüm test çıktıları yerine **örnek olarak seçilen üç test türüne**
  ait ekran görüntüleri paylaşılmıştır.
  Bu kapsamda **Integration Test**, **User Interface (UI) Test**
  ve **End-to-End (E2E) Test** sonuçlarına ait görseller,
  `screenshots/tests` dizini altında yer almaktadır.
- UAT (User Acceptance Test): Uygulama, temel kullanıcı senaryoları
  (film ekleme, silme, güncelleme, listeleme ve arama) üzerinden
  manuel olarak test edilmiştir.


---

## Geliştirici
**Sultan Tazefidan**  
