package org.Models;

import java.util.Vector;

public class Movie {
    private  String title;
    private String movieID;
    private Vector<String> genres;

    public Movie(String title, String movieID, Vector<String> genres) {
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
    public Vector<String> getGenres() {
        return genres;
    }
}
