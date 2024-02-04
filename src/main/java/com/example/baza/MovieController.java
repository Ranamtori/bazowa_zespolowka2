package com.example.baza;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController

@CrossOrigin(origins = "http://localhost:3000") // adres frontendu
public class MovieController {

    private static final String FILMWEB_POPULAR_MOVIES_URL = "https://www.filmweb.pl/films/search?orderBy=popularity&descending=true";

    @GetMapping("/api/random-movie")
    public ResponseEntity<?> getRandomMovie() {
        try {
            // Fetch the HTML from Filmweb's popular movies page
            Document doc = Jsoup.connect(FILMWEB_POPULAR_MOVIES_URL).get();

            // Select all elements with class "preview__link"
            Elements movieLinks = doc.select("a.preview__link");

            List<String> movieTitles = new ArrayList<>();

            // Extract movie titles from the selected elements
            for (Element link : movieLinks) {
                movieTitles.add(link.text());
            }

            // Select a random movie title from the list
            Random random = new Random();
            String randomMovieTitle = movieTitles.get(random.nextInt(movieTitles.size()));

            // Send the random movie title to the frontend
            return ResponseEntity.ok().body("{\"title\": \"" + randomMovieTitle + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error\"}");
        }
    }
}
