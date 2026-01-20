package com.example.movie_watchlist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movie_watchlist.entity.Movie;
import com.example.movie_watchlist.repository.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Film bulunamadı"));
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        if (Boolean.FALSE.equals(movie.getWatched())) {
            movie.setRating(null);
        } else if (Boolean.TRUE.equals(movie.getWatched())) {
            if (movie.getRating() == null || movie.getRating() < 1 || movie.getRating() > 5) {
                throw new IllegalArgumentException("İzlenen filme 1-5 arası puan verilmelidir!");
            }
        }
        return movieRepository.save(movie);
    }

    //Arama implemenatasyonu
    @Override
    public List<Movie> searchMovies(String title, List<String> genres) {
        if ((title == null || title.isBlank()) &&
            (genres == null || genres.isEmpty())) {
            return movieRepository.findAll();
        }
        return movieRepository.search(title, genres);
    }
}
