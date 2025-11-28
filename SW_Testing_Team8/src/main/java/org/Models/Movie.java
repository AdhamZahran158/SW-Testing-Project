package org.Models;

import java.util.ArrayList;

public class Movie {
    private  String title;
    private String movieID;
    private ArrayList<String> genres = new ArrayList<>();

    public Movie(String title, String movieID, ArrayList<String> genres) {
        this.title = title;
        this.movieID = movieID;
        this.genres = genres;
    }
    public String getTitle() {
        return title;
    }
    public String getMovieID() {
        return movieID;
    }
    public ArrayList<String> getGenres() {
        return genres;
    }


}
