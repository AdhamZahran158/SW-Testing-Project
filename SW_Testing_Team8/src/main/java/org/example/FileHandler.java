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
            String line =  reader.readLine();
            String[] splittedLine;
            String title;
            String MovieID;
            ArrayList<String> genres =  new ArrayList<>();
            while((line  = reader.readLine()) != null){
                splittedLine = line.split(",");
                title = splittedLine[0];
                MovieID = splittedLine[1];
                line  = reader.readLine();
                splittedLine = line.split(",");
                for (String genre : splittedLine) {
                    genres.add(genre);
                }
                movies.add(new Movie(title, MovieID, genres));
                genres = null;
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
            String line =  reader.readLine();
            String[] splittedLine;
            String UserName;
            String UserID;
            ArrayList<String> likedMoviesID =  new ArrayList<>();
            while((line  = reader.readLine()) != null){
                splittedLine = line.split(",");
                UserName = splittedLine[0];
                UserID = splittedLine[1];
                line  = reader.readLine();
                splittedLine = line.split(",");
                for (String likedMovieID : splittedLine) {
                    likedMoviesID.add(likedMovieID);
                }
                users.add(new User(UserName, UserID, likedMoviesID));
                likedMoviesID = null;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return users;
    }
}
