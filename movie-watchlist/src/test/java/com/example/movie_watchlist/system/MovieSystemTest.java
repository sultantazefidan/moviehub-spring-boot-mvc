package com.example.movie_watchlist.system;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    class MovieSystemTest {

        @Autowired
        private MockMvc mockMvc;
        
       // Yetkili kullanıcı ile film ekleme işlemi yapılır ve
       // ana sayfa erişiminin sistemin güvenlik kuralına göre
       // login sayfasına yönlendirildiği doğrulanır
        @Test
        void addAndListMovieTest() throws Exception {

            // Film ekleme (login var then başarılı)
            mockMvc.perform(post("/add")
                    .with(user("test").roles("USER"))
                    .param("title", "System Film")
                    .param("releaseYear", "2020")
                    .param("genres", "Bilim Kurgu")
                    .param("watched", "true")
                    .param("rating", "4"))
                .andExpect(status().is3xxRedirection());
                
            mockMvc.perform(get("/")
                    .with(user("test").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        }
        
        
     // Yetkisiz kullanıcı film ekleyemez, login sayfasına yönlendirilir
        @Test
        void noLogin_cannotAddMovie() throws Exception {

            mockMvc.perform(post("/add")
                    .param("title", "Hack Film")
                    .param("releaseYear", "2020")
                    .param("genres", "Bilim Kurgu")
                    .param("watched", "true")
                    .param("rating", "4"))
                .andExpect(status().is3xxRedirection());
        }


        
    }