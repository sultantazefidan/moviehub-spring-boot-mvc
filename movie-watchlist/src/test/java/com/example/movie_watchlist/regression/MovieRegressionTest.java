package com.example.movie_watchlist.regression;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.movie_watchlist.entity.Movie;
import com.example.movie_watchlist.service.MovieService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MovieRegressionTest {
	
    //REGRESYON TESTLERİ
	
    @Autowired
    private MovieService movieService;
    
    
      //Daha önce doğru çalışan izlenmeyen film davranışının
     //yeni değişikliklerden sonra da bozulmadığını doğrular 
     @Test
      void regression_unwatched_ratingStillCleared() {

     Movie movie = new Movie();
     movie.setTitle("Regression Film");
     movie.setReleaseYear(2020);
     movie.setWatched(false);
     movie.setRating(5);
     movie.setNote("Test");

    
     Movie firstSave = movieService.saveMovie(movie);
     //Aynı film tekrar kaydediliyor (regresyon noktası)
     Movie secondSave = movieService.saveMovie(firstSave);
     //Davranışın bozulmadığı doğrulanır
     assertNull(secondSave.getRating());
     assertEquals("Test", secondSave.getNote());
     }

   
    // Daha önce çalışan bir senaryoda,
    // izlenen bir filmin puan ve not bilgilerinin
     // yeni değişikliklerden sonra da korunmaya devam ettiğini doğrular. 
     @Test
     void watched_keepsRatingAndNote() {

     Movie movie = new Movie();
     movie.setTitle("Regression Watched");
     movie.setReleaseYear(2021);
     movie.setWatched(true);
     movie.setRating(4);
     movie.setNote("Güzel film");

     Movie saved = movieService.saveMovie(movie);

     // Mevcut davranışın bozulmadığı kontrol edilir
     assertEquals(4, saved.getRating());
     assertEquals("Güzel film", saved.getNote());
   }


    //İzlenmeyen bir film sonradan izlenmiş olarak güncellendiğinde puan eklenebilmelidir.
    @Test
    void markedWatched_acceptsRating() {
        Movie movie = new Movie();
        movie.setTitle("Status Change");
        movie.setReleaseYear(2018);
        movie.setWatched(false);
        movie.setNote("Sonra izlenecek");

        Movie saved = movieService.saveMovie(movie);

        // kullanıcı filmi izledi
        saved.setWatched(true);
        saved.setRating(5);

        Movie updated = movieService.saveMovie(saved);

        assertEquals(5, updated.getRating());
    }

    
   //İzlenen bir filmde puan bilgisinin yanlışlıkla silinmemesi gerekir. 
    @Test
    void watched_ratingKept() {
        Movie movie = new Movie();
        movie.setTitle("Regression Guard");
        movie.setReleaseYear(2020);
        movie.setWatched(true);
        movie.setRating(3);

        Movie saved = movieService.saveMovie(movie);

        assertNotNull(saved.getRating());
    }

   //İzlenen bir film izlenmeyen olarak güncellenirse puan temizlenmelidir. 
    @Test
    void watchedToUnwatched_ratingCleared() {
        Movie movie = new Movie();
        movie.setTitle("Toggle Case");
        movie.setReleaseYear(2019);
        movie.setWatched(true);
        movie.setRating(4);

        Movie saved = movieService.saveMovie(movie);

        saved.setWatched(false);
        Movie updated = movieService.saveMovie(saved);

        assertNull(updated.getRating());
    }

}


