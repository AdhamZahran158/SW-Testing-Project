package org.integration;

import org.Models.Movie;
import org.Models.User;
import org.example.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TOP-DOWN INTEGRATION TESTING
 * Top-Down approach starts testing from the highest level module (main application)
 * and progressively integrates lower-level modules.
 * Testing Order (from top to bottom):
 * Level 1: MovieRecommendationApp (Main) - with inline test data (no file I/O)
 * Level 2: FileHandler + Validators - with real file I/O
 * Level 3: Validators + ExceptionHandler - real components
 * Level 4: RecommendationEngine + Models - real components with test data
 * Level 5: Models (Movie, User) - actual implementations
 * Level 6: Full Integration - all real components
 * Note: This test uses TEST DATA (not stubs/mocks) to isolate higher-level
 * logic before integrating real file I/O in lower levels.
 */

/*
Leve1 1,2: Kirllos
Level 3,4: George
Level 5,6: Andro
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TopDownIntegrationTest {

    private final String referencePath = "src/test/java/org/integration/TopDown_Text_Test";
    private final Path testDir = Path.of(System.getProperty("user.dir")).resolve(referencePath);

    private AutoCloseable mocks;

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    // ==================== LEVEL 1: Main Application Flow (with Stubs) ====================

    @Nested
    @DisplayName("Level 1: Main Application Flow Tests")
    @Order(1)
    class Level1_MainApplicationFlowTests {
        @Test
        @DisplayName("Application workflow with stubbed FileHandler")
        void testApplicationWorkflowWithStubbedFileHandler() throws IOException {
            // Create stub data that would come from FileHandler
            ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Sci-Fi"));
            Movie stubMovie = new Movie("The Matrix", "MATR001", genres);
            ArrayList<Movie> stubMovies = new ArrayList<>(Arrays.asList(stubMovie));

            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("MATR001"));
            User stubUser = new User("John Doe", "1a2b3c4d5", likedIds);
            ArrayList<User> stubUsers = new ArrayList<>(Arrays.asList(stubUser));

            // Simulate the main application flow
            MovieValidator movieValidator = new MovieValidator();
            UserValidator userValidator = new UserValidator();
            RecommendationEngine recommendationEngine = new RecommendationEngine();

            // Validate movies (as main app would do)
            boolean moviesValid = true;
            for (Movie movie : stubMovies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    moviesValid = false;
                    break;
                }
                movieValidator.getId_list().add(movie.getMovieID());
            }
            assertTrue(moviesValid);

            // Validate users
            for (User user : stubUsers) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
                user.setLikedMovies(stubMovies);
            }

            // Generate recommendations
            for (User user : stubUsers) {
                recommendationEngine.GetRecommendations(user, stubMovies);
            }

            assertFalse(stubUsers.get(0).getRecMovies().isEmpty());
        }

        @Test
        @DisplayName("Application error flow - invalid movie")
        void testApplicationErrorFlowInvalidMovie() {
            ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action"));
            Movie invalidMovie = new Movie("invalid", "M001", genres); // lowercase title
            ArrayList<Movie> movies = new ArrayList<>(Arrays.asList(invalidMovie));

            MovieValidator movieValidator = new MovieValidator();

            boolean hasError = false;
            String firstError = null;

            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    hasError = true;
                    firstError = movieValidator.getExceptionHandler().getErrorLog().get(0);
                    break;
                }
            }

            assertTrue(hasError);
            assertNotNull(firstError);
            assertTrue(firstError.contains("ERROR"));
        }

        @Test
        @DisplayName("Application error flow - invalid user")
        void testApplicationErrorFlowInvalidUser() {
            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001"));
            User invalidUser = new User("123Invalid", "12345678", likedIds); // Invalid name & ID

            UserValidator userValidator = new UserValidator();

            boolean hasError = !userValidator.validateUserName(invalidUser.getName());

            assertTrue(hasError);
            assertFalse(userValidator.getExceptionHandler().getErrorLog().isEmpty());
        }

    }

    // ==================== LEVEL 2: FileHandler Integration ====================

    @Nested
    @DisplayName("Level 2: FileHandler Integration Tests")
    @Order(2)
    class Level2_FileHandlerIntegrationTests {
        @Test
        @DisplayName("FileHandler reads movies and integrates with validators")
        void testFileHandlerWithValidators() throws IOException {
            // Create real test file
            Path moviesFile = testDir.resolve("movies.txt");
            Files.writeString(moviesFile, "The Matrix,MATR001\nAction,Sci-Fi");

            FileHandler fileHandler = new FileHandler();
            MovieValidator movieValidator = new MovieValidator();

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            // Validate each movie
            for (Movie movie : movies) {
                assertTrue(movieValidator.validateMovieTitle(movie.getTitle()));
            }
        }

        @Test
        @DisplayName("FileHandler reads users and integrates with validators")
        void testFileHandlerUsersWithValidators() throws IOException {
            Path usersFile = testDir.resolve("users.txt");
            Files.writeString(usersFile, "John Doe,1a2b3c4d5\nMATR001");

            FileHandler fileHandler = new FileHandler();
            UserValidator userValidator = new UserValidator();

            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
            }
        }

        @Test
        @DisplayName("FileHandler with main app validation flow")
        void testFileHandlerMainAppFlow() throws IOException {
            // Setup files
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            Files.writeString(moviesFile, "The Matrix,MATR001\nAction,Sci-Fi");
            Files.writeString(usersFile, "John Doe,1a2b3c4d5\nMATR001");

            // Simulate main app components
            FileHandler fileHandler = new FileHandler();
            MovieValidator movieValidator = new MovieValidator();
            UserValidator userValidator = new UserValidator();

            // Read and validate movies
            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            boolean moviesValid = true;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    moviesValid = false;
                    break;
                }
                movieValidator.getId_list().add(movie.getMovieID());
            }
            assertTrue(moviesValid);

            // Read and validate users
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());
            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
            }
        }

    }

    // ==================== LEVEL 3: Validators Integration ====================

    @Nested
    @DisplayName("Level 3: Validators with ExceptionHandler Integration")
    @Order(3)
    class Level3_ValidatorsIntegrationTests {
        @Test
        @DisplayName("MovieValidator integrates with ExceptionHandler for error logging")
        void testMovieValidatorExceptionHandlerIntegration() {
            MovieValidator validator = new MovieValidator();

            // Valid case - no errors
            validator.validateMovieTitle("Valid Title");
            assertTrue(validator.getExceptionHandler().getErrorLog().isEmpty());

            // Invalid case - error logged
            validator.validateMovieTitle("x"); // Too short
            assertFalse(validator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("UserValidator integrates with ExceptionHandler for error logging")
        void testUserValidatorExceptionHandlerIntegration() {
            UserValidator validator = new UserValidator();

            // Valid case
            validator.validateUserName("Valid Name");
            assertTrue(validator.getExceptionHandler().getErrorLog().isEmpty());

            // Invalid case
            validator.validateUserName(""); // Empty name
            assertFalse(validator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("MovieValidator ID uniqueness check")
        void testMovieValidatorIdUniqueness() {
            MovieValidator validator = new MovieValidator();

            // Add first ID
            validator.getId_list().add("MOV001");

            // Check uniqueness
            boolean isUnique = validator.checkUnique3Digits("ABC002"); // Different digits
            assertTrue(isUnique);

            boolean isDuplicate = validator.checkUnique3Digits("XYZ001"); // Same digits
            assertFalse(isDuplicate);
        }

        @Test
        @DisplayName("UserValidator ID uniqueness check")
        void testUserValidatorIdUniqueness() {
            UserValidator validator = new UserValidator();

            // First user - should pass
            assertTrue(validator.validateUserId("1a2b3c4d5"));

            // Second user with same ID - should fail
            assertFalse(validator.validateUserId("1a2b3c4d5"));
        }
    }

    // ==================== LEVEL 4: RecommendationEngine Integration ====================

    @Nested
    @DisplayName("Level 4: RecommendationEngine with Models Integration")
    @Order(4)
    class Level4_RecommendationEngineTests {
        @Test
        @DisplayName("RecommendationEngine generates correct recommendations")
        void testRecommendationEngineWithModels() {
            // Create movies
            ArrayList<String> actionGenre = new ArrayList<>(Arrays.asList("Action"));
            ArrayList<String> comedyGenre = new ArrayList<>(Arrays.asList("Comedy"));
            ArrayList<String> actionComedyGenre = new ArrayList<>(Arrays.asList("Action", "Comedy"));

            Movie actionMovie = new Movie("Action Hero", "A001", actionGenre);
            Movie comedyMovie = new Movie("Funny Times", "C001", comedyGenre);
            Movie mixedMovie = new Movie("Action Comedy", "AC01", actionComedyGenre);

            ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(actionMovie, comedyMovie, mixedMovie));

            // Create user who likes action
            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("A001"));
            User user = new User("Test User", "123456789", likedIds);
            user.setLikedMovies(allMovies);

            // Generate recommendations
            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, allMovies);

            ArrayList<String> recs = user.getRecMovies();

            // Should recommend Action Hero and Action Comedy (both have Action genre)
            assertTrue(recs.contains("Action Hero"));
            assertTrue(recs.contains("Action Comedy"));
            assertFalse(recs.contains("Funny Times")); // No Action genre
        }

        @Test
        @DisplayName("RecommendationEngine with user having multiple liked movies")
        void testRecommendationEngineMultipleLikedMovies() {
            ArrayList<String> genre1 = new ArrayList<>(Arrays.asList("Drama"));
            ArrayList<String> genre2 = new ArrayList<>(Arrays.asList("Comedy"));
            ArrayList<String> genre3 = new ArrayList<>(Arrays.asList("Drama", "Comedy"));
            ArrayList<String> genre4 = new ArrayList<>(Arrays.asList("Horror"));

            Movie movie1 = new Movie("Drama One", "D001", genre1);
            Movie movie2 = new Movie("Comedy One", "C001", genre2);
            Movie movie3 = new Movie("Dramedy", "DC01", genre3);
            Movie movie4 = new Movie("Horror Film", "H001", genre4);

            ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(movie1, movie2, movie3, movie4));

            // User likes Drama and Comedy
            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("D001", "C001"));
            User user = new User("Multi Taste", "123456789", likedIds);
            user.setLikedMovies(allMovies);

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, allMovies);

            ArrayList<String> recs = user.getRecMovies();

            assertTrue(recs.contains("Drama One"));
            assertTrue(recs.contains("Comedy One"));
            assertTrue(recs.contains("Dramedy")); // Has both Drama and Comedy
            assertFalse(recs.contains("Horror Film")); // No matching genre
        }

        @Test
        @DisplayName("RecommendationEngine with no matching genres")
        void testRecommendationEngineNoMatchingGenres() {
            ArrayList<String> actionGenre = new ArrayList<>(Arrays.asList("Action"));
            ArrayList<String> horrorGenre = new ArrayList<>(Arrays.asList("Horror"));

            Movie actionMovie = new Movie("Action Movie", "A001", actionGenre);
            Movie horrorMovie = new Movie("Horror Movie", "H001", horrorGenre);

            ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(actionMovie, horrorMovie));

            // User likes only Action
            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("A001"));
            User user = new User("Action Fan", "123456789", likedIds);
            user.setLikedMovies(allMovies);

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, allMovies);

            ArrayList<String> recs = user.getRecMovies();

            assertEquals(1, recs.size()); // Only the liked movie itself
            assertTrue(recs.contains("Action Movie"));
        }

    }

    // ==================== LEVEL 5: Models Integration ====================

    @Nested
    @DisplayName("Level 5: Models Integration Tests")
    @Order(5)
    class Level5_ModelsIntegrationTests {

        @Test
        @DisplayName("Movie model works with all components")
        void testMovieModelIntegration() {
            ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Adventure"));
            Movie movie = new Movie("Adventure Time", "ADV001", genres);

            // Test with MovieValidator
            MovieValidator validator = new MovieValidator();
            assertTrue(validator.validateMovieTitle(movie.getTitle()));
            assertTrue(validator.validateMovieIdLetters(movie.getMovieID()));

            // Test getters work correctly
            assertEquals("Adventure Time", movie.getTitle());
            assertEquals("ADV001", movie.getMovieID());
            assertEquals(2, movie.getGenres().size());
        }

        @Test
        @DisplayName("User model integrates with Movie model")
        void testUserMovieModelIntegration() {
            // Create movies
            ArrayList<String> genres = new ArrayList<>(Arrays.asList("Sci-Fi"));
            Movie movie = new Movie("Sci Fi Movie", "SF001", genres);
            ArrayList<Movie> movies = new ArrayList<>(Arrays.asList(movie));

            // Create user
            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("SF001"));
            User user = new User("Sci Fi Fan", "1a2b3c4d5", likedIds);

            // Link movies
            assertTrue(user.setLikedMovies(movies));
            assertEquals(1, user.getLikedMovies().size());
            assertEquals("Sci Fi Movie", user.getLikedMovies().get(0).getTitle());
        }

        @Test
        @DisplayName("User model setRecMovies and getRecMovies")
        void testUserRecMovies() {
            ArrayList<String> likedIds = new ArrayList<>();
            User user = new User("Test User", "123456789", likedIds);

            ArrayList<String> recommendations = new ArrayList<>(Arrays.asList("Movie A", "Movie B"));
            user.setRecMovies(recommendations);

            assertEquals(2, user.getRecMovies().size());
            assertTrue(user.getRecMovies().contains("Movie A"));
            assertTrue(user.getRecMovies().contains("Movie B"));
        }
    }

    // ==================== LEVEL 6: Full System Integration ====================

    @Nested
    @DisplayName("Level 6: Complete System Integration")
    @Order(6)
    class Level6_CompleteSystemIntegration {

        @Test
        @DisplayName("Full system workflow with real components")
        void testFullSystemWorkflow() throws IOException {
            // Create test files
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            String moviesContent =
                    "The Matrix,MATR001\n" +
                            "Action,Sci-Fi\n" +
                            "Inception,INCE002\n" +
                            "Thriller,Sci-Fi\n" +
                            "The Hangover,HANG003\n" +
                            "Comedy";

            String usersContent =
                    "John Doe,1a2b3c4d5\n" +
                            "MATR001\n" +
                            "Jane Smith,9z8y7x6w5\n" +
                            "HANG003";

            Files.writeString(moviesFile, moviesContent);
            Files.writeString(usersFile, usersContent);

            // Initialize all components
            FileHandler fileHandler = new FileHandler();
            MovieValidator movieValidator = new MovieValidator();
            UserValidator userValidator = new UserValidator();
            RecommendationEngine recommendationEngine = new RecommendationEngine();

            // Read movies
            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            assertEquals(3, movies.size());

            // Validate movies
            boolean hasError = false;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    hasError = true;
                    break;
                }
                movieValidator.getId_list().add(movie.getMovieID());
            }
            assertFalse(hasError);

            // Read users
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());
            assertEquals(2, users.size());

            // Validate and link users
            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
                assertTrue(user.setLikedMovies(movies));
            }

            // Generate recommendations
            for (User user : users) {
                recommendationEngine.GetRecommendations(user, movies);
            }

            // Verify John's recommendations (likes Matrix: Action, Sci-Fi)
            ArrayList<String> johnRecs = users.get(0).getRecMovies();
            assertTrue(johnRecs.contains("The Matrix"));
            assertTrue(johnRecs.contains("Inception")); // Shares Sci-Fi
            assertFalse(johnRecs.contains("The Hangover")); // No shared genre

            // Verify Jane's recommendations (likes Hangover: Comedy)
            ArrayList<String> janeRecs = users.get(1).getRecMovies();
            assertTrue(janeRecs.contains("The Hangover"));
            assertEquals(1, janeRecs.size()); // Only Comedy movie
        }

        @Test
        @DisplayName("Full system with validation errors")
        void testFullSystemWithValidationErrors() throws IOException {
            // Create file with invalid movie title
            Path moviesFile = testDir.resolve("invalid_movies.txt");
            Files.writeString(moviesFile, "lowercase title,M001\nAction");

            FileHandler fileHandler = new FileHandler();
            MovieValidator movieValidator = new MovieValidator();

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            boolean hasError = false;
            String errorMessage = null;

            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    hasError = true;
                    errorMessage = movieValidator.getExceptionHandler().getErrorLog().get(0);
                    break;
                }
            }

            assertTrue(hasError);
            assertNotNull(errorMessage);
            assertTrue(errorMessage.contains("ERROR"));
        }

        @Test
        @DisplayName("Full system with multiple users and complex recommendations")
        void testFullSystemComplexScenario() throws IOException {
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            String moviesContent =
                    "Action One,ACT001\n" +
                            "Action\n" +
                            "Comedy One,COM001\n" +
                            "Comedy\n" +
                            "Action Comedy,ACO001\n" +
                            "Action,Comedy\n" +
                            "Drama Film,DRA001\n" +
                            "Drama";

            String usersContent =
                    "Action Fan,1a2b3c4d5\n" +
                            "ACT001\n" +
                            "Comedy Fan,2b3c4d5e6\n" +
                            "COM001\n" +
                            "Mix Fan,3c4d5e6f7\n" +
                            "ACT001,COM001";

            Files.writeString(moviesFile, moviesContent);
            Files.writeString(usersFile, usersContent);

            // Full workflow
            FileHandler fileHandler = new FileHandler();
            MovieValidator movieValidator = new MovieValidator();
            UserValidator userValidator = new UserValidator();
            RecommendationEngine engine = new RecommendationEngine();

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            // Validate
            for (Movie movie : movies) {
                movieValidator.validateMovieTitle(movie.getTitle());
                movieValidator.getId_list().add(movie.getMovieID());
            }

            for (User user : users) {
                userValidator.validateUserName(user.getName());
                userValidator.validateUserId(user.getId());
                user.setLikedMovies(movies);
                engine.GetRecommendations(user, movies);
            }

            // Action Fan: likes Action One -> gets Action One + Action Comedy
            ArrayList<String> actionFanRecs = users.get(0).getRecMovies();
            assertTrue(actionFanRecs.contains("Action One"));
            assertTrue(actionFanRecs.contains("Action Comedy"));
            assertEquals(2, actionFanRecs.size());

            // Comedy Fan: likes Comedy One -> gets Comedy One + Action Comedy
            ArrayList<String> comedyFanRecs = users.get(1).getRecMovies();
            assertTrue(comedyFanRecs.contains("Comedy One"));
            assertTrue(comedyFanRecs.contains("Action Comedy"));
            assertEquals(2, comedyFanRecs.size());

            // Mix Fan: likes both -> gets Action One, Comedy One, Action Comedy
            ArrayList<String> mixFanRecs = users.get(2).getRecMovies();
            assertTrue(mixFanRecs.contains("Action One"));
            assertTrue(mixFanRecs.contains("Comedy One"));
            assertTrue(mixFanRecs.contains("Action Comedy"));
            assertEquals(3, mixFanRecs.size());
        }

        @Test
        @DisplayName("System handles empty liked movies")
        void testSystemHandlesEmptyLikedMovies() throws IOException {
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            Files.writeString(moviesFile, "Test Movie,TEST001\nAction");
            Files.writeString(usersFile, "Empty User,1a2b3c4d5\nNONEXISTENT");

            FileHandler fileHandler = new FileHandler();
            UserValidator userValidator = new UserValidator();

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            User user = users.get(0);
            boolean hasLikedMovies = user.setLikedMovies(movies);

            // Validate that user has liked movies
            userValidator.validateLikedMovieList(hasLikedMovies);

            assertFalse(hasLikedMovies);
            assertFalse(userValidator.getExceptionHandler().getErrorLog().isEmpty());
        }
    }
}

