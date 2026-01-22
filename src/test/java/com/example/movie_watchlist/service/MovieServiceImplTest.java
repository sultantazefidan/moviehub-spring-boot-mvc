package com.example.movie_watchlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.movie_watchlist.entity.Movie;
import com.example.movie_watchlist.repository.MovieRepository;

//UNİT TESTLER 
@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;
    
   //İzlenmeyen filmde puan temizlenmeli, not korunmalıdır. 

    @Test
    void unwatched_clearsRating_keepsNote() {
        Movie movie = new Movie();
        movie.setWatched(false);
        movie.setRating(4);
        movie.setNote("Güzel film");

        when(movieRepository.save(any(Movie.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Movie saved = movieService.saveMovie(movie);

        assertNull(saved.getRating());
        assertEquals("Güzel film", saved.getNote());
    }
    
    //Aynı film tekrar kaydedildiğinde veriler bozulmamalı 
    @Test
    void saveWatched_again_keepsData() {

        Movie movie = new Movie();
        movie.setWatched(true);
        movie.setRating(4);
        movie.setNote("İyiydi");

        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Movie firstSave = movieService.saveMovie(movie);
        Movie secondSave = movieService.saveMovie(firstSave);

        assertEquals(4, secondSave.getRating());
        assertEquals("İyiydi", secondSave.getNote());
    }
    
   //İzlenme durumu null olan film, izlenmemiş kabul edilerek puansız kaydedilmelidir.
    @Test
    void nullWatched_savedAsUnwatched() {
        Movie movie = new Movie();
        movie.setWatched(null);
        movie.setRating(null);
        movie.setNote("İzlenecek");

        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Movie saved = movieService.saveMovie(movie);

        assertNull(saved.getRating());
        assertEquals("İzlenecek", saved.getNote());
    }


    
   //İzlenen film puansız kaydedilmeye çalışılırsa exception fırlatılmalıdır. 
    @Test
    void watchedMovie_mustHaveRating() {

        Movie movie = new Movie();
        movie.setWatched(true);   
        movie.setRating(null);    
        movie.setNote("İyiydi");

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.saveMovie(movie);
        });
    }
    
    //İzlenen filme geçersiz puan (0 / 6 / null) verilirse -> Exeption 
    @Test
    void watched_invalidRating_fails() {

        Movie movie = new Movie();
        movie.setWatched(true);
        movie.setRating(6); // geçersiz (1–5 dışı)
        movie.setNote("Deneme");

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.saveMovie(movie);
        });
    }

    //İzlenmeyen film repository katmanına gönderilirken puan temizlenmeli, not korunmalıdır.
    @Test
    void clearFieldsForUnwatchedMovie() {

        Movie movie = new Movie();
        movie.setWatched(false);
        movie.setRating(5);
        movie.setNote("Silinmeli");

        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        movieService.saveMovie(movie);

        verify(movieRepository).save(argThat(saved ->
                saved.getRating() == null &&
                "Silinmeli".equals(saved.getNote())
        ));
    }

     //Zorunlu alan olan title boş ise film kaydedilmemeli  
    @Test
    void saveMovie_withoutTitle_shouldFail() {
        Movie movie = new Movie();
        movie.setReleaseYear(2020);
        movie.setWatched(false);

        assertThrows(Exception.class, () -> {
            movieService.saveMovie(movie);
        });
    }


}

