package com.example.movie_watchlist.entity;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.ElementCollection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import jakarta.persistence.ElementCollection;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Film adı boş olamaz!")
    private String title;        //film adı
    
    private String description;  //film konusu
    
    @NotNull(message = "İzlenme durumu seçilmelidir")
    private Boolean watched;     //izlenme durumunu tutan değişken
    
    @Size(max = 150, message = "İzleyici notu fazla 100 karakter olabilir")
    private String note;         //izlenen film üzerine kısa bır not

    
    //Çoklu türler
    @ElementCollection
    @CollectionTable(
        name = "movie_genres",
        joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();
    
    @NotNull(message = "Yıl boş olamaz")
    @Column(name = "release_year")
    @Min(value = 1900, message = "Yıl 1900'den büyük olmalı")
    @Max(value = 2100, message = "Yıl 2100'den küçük olmalı")
    private Integer releaseYear; //film yıl değişkeni
    
    @Min(value = 1, message = "Puan en az 1 olmalı")
    @Max(value = 5, message = "Puan en fazla 5 olmalı")
    private Integer rating; //puan değişkeni
 
    
    @AssertTrue(message = "İzlenen film için puan girilmelidir")
    public boolean isRatingValidForWatchedMovie() {
        return !Boolean.TRUE.equals(watched) || rating != null;
    }
    
    @Min(value = 1, message = "Süre en az 1 dakika olmalı")
    @Max(value = 600, message = "Süre en fazla 600 dakika olabilir")
    @Column(name = "duration")
    private Integer duration;   //film süresi (dk)
    
    private String imageUrl;
    
    @Column(name = "other_genre")
    private String otherGenre;

    private String watchUrl;


    
    public Movie() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getOtherGenre() {
        return otherGenre;
    }
    public void setOtherGenre(String otherGenre) {
        this.otherGenre = otherGenre;
    }

    public String getWatchUrl() {
        return watchUrl;
    }

    public void setWatchUrl(String watchUrl) {
        this.watchUrl = watchUrl;
    }


}
