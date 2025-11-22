package org.Models;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class User {
    private String name;
    private String id;
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    Random rnd = new Random();

    public User(String name, String id)
    {
        this.name = name;
        this.id = id;
    }
    public String getName()
    {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }
}
