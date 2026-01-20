package com.example.movie_watchlist.repository;

import com.example.movie_watchlist.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
        SELECT DISTINCT m FROM Movie m
        LEFT JOIN m.genres g
        WHERE
        (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND
        (:genres IS NULL OR g IN :genres)
    """)
    List<Movie> search(
            @Param("title") String title,
            @Param("genres") List<String> genres
    );
}
