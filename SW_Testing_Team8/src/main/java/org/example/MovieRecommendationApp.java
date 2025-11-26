package org.example;


import org.Models.Movie;
import org.Models.User;

import java.util.ArrayList;

public class MovieRecommendationApp {
    static void main() {
            FileHandler fh = new FileHandler();
            ArrayList<Movie> movies = new ArrayList<>();
            movies = fh.readMovies("D:\\JavaTest\\TextFiles\\Movie.txt");
            ArrayList<User> users = new ArrayList<>();
            users = fh.readUser("D:\\JavaTest\\TextFiles\\User.txt");
            ArrayList<String> test = new ArrayList<>();
            test.add("The dark knight");
            test.add("The bright knight");
            RecommendationEngine recommendationEngine = new RecommendationEngine();
            for (User user : users) {
                user.setLikedMovies(movies);
                recommendationEngine.GetRecommendations(user, movies);
                System.out.println(user.getLikedMovies());
            }
            FileWriteHandler fileWriteHandler = new FileWriteHandler(users);
            fileWriteHandler.wrtie();
        }
    }

