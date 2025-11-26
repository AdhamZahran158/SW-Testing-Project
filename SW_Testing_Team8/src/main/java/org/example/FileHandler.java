package org.example;

import org.Models.Movie;
import org.Models.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    public ArrayList<Movie> readMovies(String filePathMovie){
        ArrayList<Movie> movies = new ArrayList<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(filePathMovie));
            String line;
            String[] splittedLine;
            String title;
            String MovieID;
            while((line  = reader.readLine()) != null){
                splittedLine = line.split(",");
                title = splittedLine[0];
                MovieID = splittedLine[1];
                line  = reader.readLine();
                splittedLine = line.split(",");
                ArrayList<String> genres =  new ArrayList<>();
                for (String genre : splittedLine) {
                    genres.add(genre);
                }
                movies.add(new Movie(title, MovieID, genres));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return movies;
    }
    public ArrayList<User> readUser(String filePathUser){
        ArrayList<User> users = new ArrayList<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(filePathUser));
            String line;
            String[] splittedLine;
            String UserName;
            String UserID;
            while((line  = reader.readLine()) != null){
                splittedLine = line.split(",");
                UserName = splittedLine[0];
                UserID = splittedLine[1];
                line  = reader.readLine();
                splittedLine = line.split(",");
                ArrayList<String> likedMoviesID =  new ArrayList<>();
                for (String likedMovieID : splittedLine) {
                    likedMoviesID.add(likedMovieID);
                }
                users.add(new User(UserName, UserID, likedMoviesID));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return users;
    }
}
