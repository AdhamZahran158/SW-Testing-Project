package org.example;

import org.Models.Movie;
import org.Models.User;

import java.util.ArrayList;
import java.util.Scanner;

public class MovieRecommendationApp {
    static void main() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Movie Recommendation System ===");
        System.out.print("Enter the path to movies.txt file: ");
        String moviesFilePath = scanner.nextLine().trim();

        System.out.print("Enter the path to users.txt file: ");
        String usersFilePath = scanner.nextLine().trim();

        scanner.close();

        // Initialize handlers and validators
        FileHandler fileHandler = new FileHandler();
        MovieValidator movieValidator = new MovieValidator();
        UserValidator userValidator = new UserValidator();

        // Read movies from file
        ArrayList<Movie> movies = fileHandler.readMovies(moviesFilePath);

        // Validate all movies
        boolean hasError = false;
        String firstError = null;

        for (Movie movie : movies) {
            // Validate movie title
            if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                hasError = true;
                firstError = movieValidator.getExceptionHandler().getErrorLog().get(0);
                break;
            }

            // Validate movie ID
            if (!movieValidator.validateMovieIdFull(movie.getMovieID())) {
                hasError = true;
                firstError = movieValidator.getExceptionHandler().getErrorLog().get(0);
                break;
            }

            // Add current movie ID to the list for uniqueness checking **after successful validation**
            movieValidator.getId_list().add(movie.getMovieID());

            // Validate all genres
            for (String genre : movie.getGenres()) {
                if (!movieValidator.validateMovieGenre(genre)) {
                    hasError = true;
                    firstError = movieValidator.getExceptionHandler().getErrorLog().get(0);
                    break;
                }
            }

            if (hasError) break;
        }

        // If there's an error in movies, write it and exit
        if (hasError) {
            FileWriteHandler errorWriter = new FileWriteHandler(firstError);
            errorWriter.write();
            return;
        }

        // Read users from file
        ArrayList<User> users = fileHandler.readUser(usersFilePath);

        // Validate all users and link liked movies
        for (User user : users) {
            // Validate user name
            if (!userValidator.validateUserName(user.getName())) {
                hasError = true;
                firstError = userValidator.getExceptionHandler().getErrorLog().get(0);
                break;
            }

            // Validate user ID
            if (!userValidator.validateUserId(user.getId())) {
                hasError = true;
                firstError = userValidator.getExceptionHandler().getErrorLog().get(0);
                break;
            }

            // Link liked movies using the setLikedMovies method from User class
            boolean hasLikedMovies = user.setLikedMovies(movies);

            // Validate that user has liked movies
            userValidator.validateLikedMovieList(hasLikedMovies);
            if (!userValidator.getExceptionHandler().getErrorLog().isEmpty()) {
                hasError = true;
                firstError = userValidator.getExceptionHandler().getErrorLog().get(0);
                break;
            }

            if (hasError) break;
        }

        // If there's an error in users, write it and exit
        if (hasError) {
            FileWriteHandler errorWriter = new FileWriteHandler(firstError);
            errorWriter.write();
            return;
        }

        // Generate recommendations for all users
        RecommendationEngine recommendationEngine = new RecommendationEngine();
        for (User user : users) {
            recommendationEngine.GetRecommendations(user, movies);
        }

        // Write recommendations to output file
        FileWriteHandler outputWriter = new FileWriteHandler(users);
        outputWriter.write();

        System.out.println("\nProcess completed! Check Output.txt for results.");
    }
}
