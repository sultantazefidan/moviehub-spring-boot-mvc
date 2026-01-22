package com.example.movie_watchlist.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.movie_watchlist.entity.Movie;
import com.example.movie_watchlist.repository.MovieRepository;
import com.example.movie_watchlist.service.MovieService;

import jakarta.transaction.Transactional;


@ActiveProfiles("test") // Test ortamında hızlı ve bağımsız çalışması için H2 kullanılmış, canlı ortamda MySQL tercih edilmiştir.

@SpringBootTest
@Transactional
class MovieRepositoryIntegrationTest {

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;
    
  //ENTEGRASYON TESTLERİ 
    
   //İzlenen ve geçerli puana sahip film başarıyla kaydedilmeli 
    @Test
    void saveWatchedMovie_rating_ok() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setGenres(List.of("Sci-fi"));
        movie.setReleaseYear(2010);  
        movie.setWatched(true);
        movie.setRating(5);          
        movie.setNote("Harika");

        Movie saved = movieService.saveMovie(movie);

        assertNotNull(saved.getId());
        assertEquals(5, saved.getRating());
        assertEquals("Harika", saved.getNote());
    }
      

  //Veritabanından kayıtlı filmler başarıyla getirilebilmeli  
    @Test
    void canGetMoviesFromDatabase() {
        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setReleaseYear(2014);   
        movie.setWatched(false);

        movieService.saveMovie(movie);

        List<Movie> movies = movieRepository.findAll();
        assertFalse(movies.isEmpty());
    }
    
    
  //İzlenmeyen filmde puan ve not servis tarafından temizlenmeli 
    @Test
    void unwatchedMovie_ratingRemoved_noteKept() {
        Movie movie = new Movie();
        movie.setTitle("Matrix");
        movie.setReleaseYear(1999);
        movie.setWatched(false);
        movie.setRating(4);      
        movie.setNote("Deneme");  

        Movie saved = movieService.saveMovie(movie);

        assertNotNull(saved.getId());
        assertNull(saved.getRating());        
        assertEquals("Deneme", saved.getNote()); 
    }
    
    //id ile bulunabilmeli     
    @Test
    void shouldFindMovieById() {
        Movie movie = new Movie();
        movie.setTitle("Bulunacak Film");
        movie.setReleaseYear(2001);   
        movie.setWatched(true);
        movie.setRating(4);           

        Movie saved = movieService.saveMovie(movie);

        Movie found = movieRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("Bulunacak Film", found.getTitle());
    }
    
    //Film silinebilmeli.    
    @Test
    void shouldDeleteMovie() {
        Movie movie = new Movie();
        movie.setTitle("Silinecek Film");
        movie.setReleaseYear(2005);   
        movie.setWatched(false);

        Movie saved = movieService.saveMovie(movie);
        Long id = saved.getId();

        movieRepository.deleteById(id);

        assertFalse(movieRepository.findById(id).isPresent());
    }

}

