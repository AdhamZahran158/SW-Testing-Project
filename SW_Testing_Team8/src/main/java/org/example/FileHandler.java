package org.example;

import org.Models.Movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    private String filePathMovie = "";
    private String filePathUser = "";
    public void setFilePath(String filePathMovie, String filePathUser) {
        this.filePathMovie = filePathMovie;
        this.filePathUser = filePathUser;
    }
    public ArrayList<Movie> readMovies(){
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
}
