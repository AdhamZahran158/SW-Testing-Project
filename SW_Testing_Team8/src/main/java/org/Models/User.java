package org.Models;

import org.example.RecommendationEngine;
import org.example.UserValidator;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String name;
    private String id;
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    private ArrayList<String> likedMoviesId = new ArrayList<>();
    private ArrayList<String> RecMovies = new ArrayList<>();

    public User(String name, String id, ArrayList<String> likedMoviesId)
    {
        this.name = name;
        this.id = id;
        this.likedMoviesId = likedMoviesId;
    }

    public String getName()
    {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Movie> getLikedMovies() { return likedMovies; }

    public boolean setLikedMovies(ArrayList<Movie> availableMovies) {
        if (likedMoviesId.isEmpty() || likedMoviesId == null) {
            return false;
        }
        int count = 0;
        for (Movie m : availableMovies) {
            for (String id : likedMoviesId) {
                if (Objects.equals(m.getMovieID(), id)) {
                    this.likedMovies.add(m);
                    count++;
                }
            }
        }
        return count >= likedMoviesId.size();
    }

    public void setRecMovies(ArrayList<String> RecMovies) {
        this.RecMovies = RecMovies;
    }

    public ArrayList<String> getRecMovies() {
        return RecMovies;
    }
}
