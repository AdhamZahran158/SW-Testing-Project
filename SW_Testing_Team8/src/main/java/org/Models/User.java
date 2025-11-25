package org.Models;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class User {
    private String name;
    private String id;
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    private ArrayList<String> likedMoviesId = new ArrayList<>();
    private ArrayList<String> RecMovies = new ArrayList<>();


    Random rnd = new Random();

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

    public ArrayList<String> getLikedMoviesID(){ return likedMoviesId; }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public void setRecMovies(ArrayList<String> RecMovies) {
        this.RecMovies = RecMovies;
    }
}
