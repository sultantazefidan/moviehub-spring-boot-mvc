package com.example.movie_watchlist.controller;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movie_watchlist.entity.Movie;
import com.example.movie_watchlist.service.MovieService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
class HomeControllerTest {
	
// USER İNTERFACE TESTLERİ
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MovieService movieService;


    //Ana sayfa açılıyor mu?
    @Test
    void home_load_loggedIn() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "admin");

        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("movies"));
    }

    
     //Kullanıcı Çıkış yapa tıkladığında login sayfasına dönmeli 
      @Test
      void logout_redirect_login() throws Exception {
        mockMvc.perform(get("/logout"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"));
     }

     
     //Film düzenle butonu çalışmalı.
    @Test
    void edit_open_click() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "admin");
        
        Movie movie = new Movie();
        movie.setTitle("Test Film");
        movie.setReleaseYear(2020);
        movie.setWatched(false);

        Movie saved = movieService.saveMovie(movie);
        mockMvc.perform(get("/edit/" + saved.getId()).session(session))
               .andExpect(status().isOk())
               .andExpect(view().name("edit")) 
               .andExpect(model().attributeExists("movie"));
    }

     
     //Film silme butonu çalışmalı
     @Test
     void delete_click() throws Exception {
         MockHttpSession session = new MockHttpSession();
         session.setAttribute("user", "admin");
         Movie movie = new Movie();
         movie.setTitle("Silinecek Film");
         movie.setReleaseYear(2021);
         movie.setWatched(false);

         Movie saved = movieService.saveMovie(movie);
         mockMvc.perform(get("/delete/" + saved.getId()).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manage")); 
     }



      
     //Arama alanı çalışıyor mu ?
     @Test
     void filter_search() throws Exception {
         MockHttpSession session = new MockHttpSession();
         session.setAttribute("user", "admin");

         mockMvc.perform(get("/search")
                 .session(session)
                 .param("keyword", "Matrix"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("movies"));
     }

     
     //Login olmayan kullanıcı ana sayfaya erişemez
      @Test
       void redirectToLogin_whenUnauth() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
     }

    
    
    //  Film ekleme POST çalışıyor mu 
    @Test
    void shouldAddMovieSuccessfully() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "admin");

        mockMvc.perform(post("/add")
                .session(session)
                .param("title", "Inception")
                .param("genres", "Bilim Kurgu")
                .param("releaseYear", "2010")
                .param("watched", "true")
                .param("rating", "5")
                .param("note", "Harika"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    // 3 İzlenmiş ama puan yoksa hata vermeli
    @Test
    void watched_withoutRating_fails() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "admin");

        mockMvc.perform(post("/add")
                .session(session)
                .param("title", "Matrix")
                .param("genres", "Bilim Kurgu")
                .param("releaseYear", "1999")
                .param("watched", "true")) 
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors(
                        "movie",
                        "ratingValidForWatchedMovie"
                ));
    }


   
}

