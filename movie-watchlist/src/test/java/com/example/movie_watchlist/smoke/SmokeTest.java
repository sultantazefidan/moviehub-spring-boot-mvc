package com.example.movie_watchlist.smoke;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
//Spring Boot uygulama context'inin sorunsuz şekilde ayağa kalktığını doğrular
@SpringBootTest
@ActiveProfiles("test")
class SmokeTest {
    
    @Test
    void contextLoads() {
    	//cokntext yukelnıyorsa test başarılıdır.
    }
}
