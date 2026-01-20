package com.example.movie_watchlist.controller;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import com.example.movie_watchlist.entity.Movie;
import com.example.movie_watchlist.service.MovieService;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final MovieService movieService;

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", session.getAttribute("user"));
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("movie", new Movie());

        model.addAttribute("openSection", "filmListe"); 
        model.addAttribute("openList", true);           

        return "home";
    }


    @PostMapping("/add")
    public String addMovie(
            @Valid @ModelAttribute Movie movie,
            BindingResult result,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) String customGenre,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
    	
    	
    	//Tür kontrolü (checkbox + diğer)
    	if ((genres == null || genres.isEmpty())
    	        && (customGenre == null || customGenre.isBlank())) {

    	    result.reject(
    	        "genreError",
    	        "En az bir tür seçilmeli veya Diğer alanı doldurulmalı"
    	    );

    	    model.addAttribute("movies", movieService.getAllMovies());
    	    model.addAttribute("movie", movie);
    	    model.addAttribute("openSection", "filmEkle"); 
    	    return "home";
    	}


        //Validation hatası varsa
        if (result.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("movie", movie);
            model.addAttribute("openSection", "filmEkle"); 
            return "home";
        }

        try {
            //Türleri birleştir
            List<String> finalGenres = new ArrayList<>();

            if (genres != null) {
                finalGenres.addAll(genres);
            }
            
            
            
            if (customGenre != null && !customGenre.isBlank()) {
                String[] otherGenres = customGenre.split(",");
                for (String g : otherGenres) {
                    finalGenres.add(g.trim());
                }
            }
            
            if (finalGenres.isEmpty()) {
                result.reject(
                    "genreError",
                    "Geçerli en az bir tür girilmelidir"
                );
                model.addAttribute("movies", movieService.getAllMovies());
                model.addAttribute("movie", movie);
                model.addAttribute("openSection", "filmEkle"); 
                return "home";
            }

            movie.setGenres(finalGenres);
            movie.setOtherGenre(customGenre);

           //Resim yükleme
            boolean isUpdate = movie.getId() != null;

            if (imageFile != null && !imageFile.isEmpty()) {

                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

                //  SADECE ROOT /uploads
                Path uploadPath = Paths.get("uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(
                        imageFile.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                //Tarayıcı burdan okur
                movie.setImageUrl("/uploads/" + fileName);

            }
            else if (isUpdate) {
                //Güncellemede eski görseli KORU
                Movie existingMovie = movieService.getMovieById(movie.getId());
                movie.setImageUrl(existingMovie.getImageUrl());
            }
            
            //Kaydet
            movieService.saveMovie(movie);
            
            redirectAttributes.addFlashAttribute("openSection", "filmListe");

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    isUpdate
                            ? "🎉 Film başarıyla güncellendi"
                            : "✅ Film başarıyla eklendi"
            );

            return "redirect:/";

        } catch (Exception e) {
            model.addAttribute("error", "Bir hata oluştu: " + e.getMessage());
            model.addAttribute("movies", movieService.getAllMovies());
            return "home";
        }
    }


    
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {

        movieService.deleteMovie(id);

        redirectAttributes.addFlashAttribute(
            "successMessage",
            "🗑️ Film başarıyla silindi !"
        );

        //sadece silmede
        redirectAttributes.addFlashAttribute("autoDismiss", true);
        redirectAttributes.addFlashAttribute("alertSize", "slim"); 

        return "redirect:/manage";
    }


    
    @GetMapping("/edit/{id}")
    public String editMovie(@PathVariable Long id,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        try {
            Movie movie = movieService.getMovieById(id);

            //Güvenlik: genres null ise patlamasın
            if (movie.getGenres() == null) {
                movie.setGenres(new ArrayList<>());
            }

            model.addAttribute("movie", movie);
            return "edit"; //  edit.html

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "❌ Güncellenecek film bulunamadı"
            );
            return "redirect:/";
        }
    }
    @GetMapping("/search")
    public String searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<String> genres,
            Model model
    ) {
        model.addAttribute(
            "movies",
            movieService.searchMovies(title, genres)
        );

        model.addAttribute("movie", new Movie());
        
        model.addAttribute("openSection", "filmAra"); //Arama açık kalsın
        model.addAttribute("openList", true);         //Sonuç listesi görünsün
        model.addAttribute("searchTitle", title);     //input dolu kalsın

        return "home";
    }


    @PostMapping("/update")
    public String updateMovie(
            @Valid @ModelAttribute Movie movie,
            BindingResult result,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) String customGenre,
            @RequestParam(required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes
    ) throws IOException {

        if (result.hasErrors()) {
            return "edit";
        }

        //Mevcut filmi databaseden çek
        Movie existingMovie = movieService.getMovieById(movie.getId());

       //Türler
        List<String> finalGenres = new ArrayList<>();
        if (genres != null) finalGenres.addAll(genres);
        if (customGenre != null && !customGenre.isBlank()) {
            finalGenres.add(customGenre.trim());
        }
        movie.setGenres(finalGenres);
        movie.setOtherGenre(customGenre);

        //Görsel
        if (imageFile != null && !imageFile.isEmpty()) {

           
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    imageFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            movie.setImageUrl("/uploads/" + fileName);

        } else {
            //Kullanıcı resme dokunmadı -> Eski resim korunur
            movie.setImageUrl(existingMovie.getImageUrl());
        }

        //Kaydet
        movieService.saveMovie(movie);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "🎉 Film başarıyla güncellendi!"
        );

        return "redirect:/edit/" + movie.getId();
    }


    @GetMapping("/manage")
    public String manageMovies(Model model, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", session.getAttribute("user"));
        model.addAttribute("movies", movieService.getAllMovies());

        return "manage"; 
    }

    

    
    
    
}
