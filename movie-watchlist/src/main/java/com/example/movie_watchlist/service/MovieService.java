package com.example.movie_watchlist.service;

import java.util.List;
import com.example.movie_watchlist.entity.Movie;

public interface MovieService {

    List<Movie> getAllMovies();

    Movie getMovieById(Long id);

    Movie saveMovie(Movie movie);

    void deleteMovie(Long id);

    //Arama
    List<Movie> searchMovies(String title, List<String> genres);
}

