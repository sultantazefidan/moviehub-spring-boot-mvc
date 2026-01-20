package com.example.movie_watchlist.e2e;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovieEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    //E2E Senaryo: Film ekle , listele, edit, güncelle 
    
    @Test
    void endToEnd_addEditUpdateFlow() throws Exception {

        MockHttpSession session = new MockHttpSession();

        // 1. Login
        mockMvc.perform(post("/login")
                .session(session)
                .param("username", "admin")
                .param("password", "123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        // 2. Ana sayfa açılıyor mu?
        mockMvc.perform(get("/").session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));

        // 2️. Film ekleme başarılı mı ?
        mockMvc.perform(post("/add")
        	    .session(session)
        	    .param("title", "Interstellar")
        	    .param("releaseYear", "2014")     
        	    .param("genres", "Bilim Kurgu")     
        	    .param("watched", "true")
        	    .param("rating", "5")
        	    .param("note", "Efsane"))
        	.andExpect(status().is3xxRedirection());

        //  3️. Listeleme sonrası film sayfada var mı?
        mockMvc.perform(get("/").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Interstellar")));

        // 4️.  Edit sayfası
        mockMvc.perform(get("/edit/{id}", 1L).session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("edit"));

        // 5️. Güncelleme çalışıyor mu ?
        mockMvc.perform(post("/update")
        	    .session(session)
        	    .param("id", "1")
        	    .param("title", "Interstellar Updated")
        	    .param("releaseYear", "2014")   //
        	    .param("watched", "true")
        	    .param("rating", "5"))
        	.andExpect(status().is3xxRedirection());


        // 6️. Güncelleme listede mi ?
        mockMvc.perform(get("/").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Interstellar Updated")));
    }

    
}
