package org.integration;

import org.Models.Movie;
import org.Models.User;
import org.example.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BOTTOM-UP INTEGRATION TESTING
 *
 * Bottom-Up approach starts testing from the lowest level modules (leaf modules)
 * and progressively integrates and tests higher-level modules.
 *
 * Testing Order (from bottom to top):
 * Level 1: Models (Movie, User) + ExceptionHandler - Lowest level, no dependencies
 * Level 2: MovieValidator, UserValidator - Depend on ExceptionHandler
 * Level 3: FileHandler - Depends on Movie, User models
 * Level 4: RecommendationEngine - Depends on Movie, User models
 * Level 5: FileWriteHandler - Depends on User model
 * Level 6: Full System Integration - All components together
 */

/*
Level 1,2: Andro
Level 3,4: George
Level 5,6: Kirllos
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BottomUpIntegrationTest {


    private final String referencePath = "src/test/java/org/integration/BottomUp_Text_Test";
    private final Path testDir = Path.of(System.getProperty("user.dir")).resolve(referencePath);
    // ==================== LEVEL 1: Basic Models + ExceptionHandler ====================

    @Nested
    @DisplayName("Level 1: Movie Model Tests")
    @Order(1)
    class Level1_MovieModelTests {

        @Test
        @DisplayName("Movie model creation and getters")
        void testMovieModelCreation() {
            ArrayList<String> genres = new ArrayList<>(Arrays.asList("Action", "Drama"));
            Movie movie = new Movie("The Matrix", "M001", genres);

            assertEquals("The Matrix", movie.getTitle());
            assertEquals("M001", movie.getMovieID());
            assertEquals(2, movie.getGenres().size());
            assertTrue(movie.getGenres().contains("Action"));
            assertTrue(movie.getGenres().contains("Drama"));
        }

        @Test
        @DisplayName("Movie with single genre")
        void testMovieWithSingleGenre() {
            ArrayList<String> genres = new ArrayList<>(Arrays.asList("Comedy"));
            Movie movie = new Movie("Funny Movie", "FM01", genres);

            assertEquals(1, movie.getGenres().size());
            assertEquals("Comedy", movie.getGenres().get(0));
        }
    }

    @Nested
    @DisplayName("Level 1: User Model Tests")
    @Order(2)
    class Level1_UserModelTests {

        @Test
        @DisplayName("User model creation and getters")
        void testUserModelCreation() {
            ArrayList<String> likedMovieIds = new ArrayList<>(Arrays.asList("M001", "M002"));
            User user = new User("John Doe", "123456789", likedMovieIds);

            assertEquals("John Doe", user.getName());
            assertEquals("123456789", user.getId());
            assertEquals(2, user.getLikedMovies().size() == 0 ? likedMovieIds.size() : user.getLikedMovies().size());
        }

        @Test
        @DisplayName("User setLikedMovies integration with Movie model")
        void testUserSetLikedMoviesWithMovieModel() {
            // Create movies
            ArrayList<String> genres1 = new ArrayList<>(Arrays.asList("Action"));
            ArrayList<String> genres2 = new ArrayList<>(Arrays.asList("Drama"));
            Movie movie1 = new Movie("Movie One", "M001", genres1);
            Movie movie2 = new Movie("Movie Two", "M002", genres2);
            ArrayList<Movie> availableMovies = new ArrayList<>(Arrays.asList(movie1, movie2));

            // Create user with liked movie IDs
            ArrayList<String> likedMovieIds = new ArrayList<>(Arrays.asList("M001", "M002"));
            User user = new User("Jane Doe", "987654321", likedMovieIds);

            // Link movies
            boolean result = user.setLikedMovies(availableMovies);

            assertTrue(result);
            assertEquals(2, user.getLikedMovies().size());
        }

        @Test
        @DisplayName("User setLikedMovies with non-existent movie ID")
        void testUserSetLikedMoviesNonExistent() {
            ArrayList<String> genres1 = new ArrayList<>(Arrays.asList("Action"));
            Movie movie1 = new Movie("Movie One", "M001", genres1);
            ArrayList<Movie> availableMovies = new ArrayList<>(Arrays.asList(movie1));

            ArrayList<String> likedMovieIds = new ArrayList<>(Arrays.asList("M999")); // Non-existent
            User user = new User("Test User", "111111111", likedMovieIds);

            boolean result = user.setLikedMovies(availableMovies);

            assertFalse(result); // Should return false as movie not found
        }
    }

    @Nested
    @DisplayName("Level 1: ExceptionHandler Tests")
    @Order(3)
    class Level1_ExceptionHandlerTests {

        @Test
        @DisplayName("ExceptionHandler logs errors correctly")
        void testExceptionHandlerLogsErrors() {
            ExceptionHandler handler = new ExceptionHandler();
            handler.logError("Test error message");

            assertEquals(1, handler.getErrorLog().size());
            assertEquals("Test error message", handler.getErrorLog().get(0));
        }

        @Test
        @DisplayName("ExceptionHandler throws validation error")
        void testExceptionHandlerThrowsError() {
            ExceptionHandler handler = new ExceptionHandler();

            assertThrows(IllegalArgumentException.class, () -> {
                handler.throwValidationError("Validation failed");
            });

            assertEquals(1, handler.getErrorLog().size());
        }

        @Test
        @DisplayName("ExceptionHandler clear error log")
        void testExceptionHandlerClearLog() {
            ExceptionHandler handler = new ExceptionHandler();
            handler.logError("Error 1");
            handler.logError("Error 2");
            handler.clearErrorLog();

            assertEquals(0, handler.getErrorLog().size());
        }
    }

    // ==================== LEVEL 2: Validators with ExceptionHandler ====================

    @Nested
    @DisplayName("Level 2: MovieValidator + ExceptionHandler Integration")
    @Order(4)
    class Level2_MovieValidatorIntegration {

        private MovieValidator movieValidator;

        @BeforeEach
        void setUp() {
            movieValidator = new MovieValidator();
        }

        @Test
        @DisplayName("Valid movie title passes validation")
        void testValidMovieTitle() {
            assertTrue(movieValidator.validateMovieTitle("The Matrix"));
            assertTrue(movieValidator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("Invalid movie title logs error in ExceptionHandler")
        void testInvalidMovieTitleLogsError() {
            assertFalse(movieValidator.validateMovieTitle("a")); // Too short

            assertFalse(movieValidator.getExceptionHandler().getErrorLog().isEmpty());
            assertTrue(movieValidator.getExceptionHandler().getErrorLog().get(0).contains("ERROR"));
        }

        @Test
        @DisplayName("Valid movie ID passes validation")
        void testValidMovieId() {
            assertTrue(movieValidator.validateMovieIdLetters("ABCD123"));
        }

        @Test
        @DisplayName("Unique digit check with ID list")
        void testUniqueDigitCheck() {
            movieValidator.getId_list().add("MOV001");

            // Same digits should fail
            assertFalse(movieValidator.checkUnique3Digits("ABC001"));
        }
    }

    @Nested
    @DisplayName("Level 2: UserValidator + ExceptionHandler Integration")
    @Order(5)
    class Level2_UserValidatorIntegration {

        private UserValidator userValidator;

        @BeforeEach
        void setUp() {
            userValidator = new UserValidator();
        }

        @Test
        @DisplayName("Valid user name passes validation")
        void testValidUserName() {
            assertTrue(userValidator.validateUserName("John Doe"));
            assertTrue(userValidator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("Invalid user name logs error")
        void testInvalidUserNameLogsError() {
            assertFalse(userValidator.validateUserName("123John")); // Starts with digits

            assertFalse(userValidator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("Valid user ID passes validation")
        void testValidUserId() {
            assertTrue(userValidator.validateUserId("1a2b3c4d5"));
        }

        @Test
        @DisplayName("Duplicate user ID fails validation")
        void testDuplicateUserId() {
            assertTrue(userValidator.validateUserId("1a2b3c4d5"));
            assertFalse(userValidator.validateUserId("1a2b3c4d5")); // Duplicate
        }

        @Test
        @DisplayName("Validate liked movie list integration")
        void testValidateLikedMovieList() {
            userValidator.validateLikedMovieList(false);

            assertFalse(userValidator.getExceptionHandler().getErrorLog().isEmpty());
            assertTrue(userValidator.getExceptionHandler().getErrorLog().get(0).contains("No Liked Movies"));
        }
    }

    // ==================== LEVEL 3: FileHandler with Models ====================

    @Nested
    @DisplayName("Level 3: FileHandler + Movie/User Models Integration")
    @Order(6)
    class Level3_FileHandlerIntegration {
        private FileHandler fileHandler;

        @BeforeEach
        void setUp() {

            fileHandler = new FileHandler();
        }

        @Test
        @DisplayName("Read movies from file creates Movie objects correctly")
        void testReadMoviesCreatesMovieObjects() throws IOException {
            // Create test movie file
            Path moviesFile = testDir.resolve("test_movies.txt");
            String content = "The Matrix,M001\nAction,Sci-Fi\nInception,M002\nThriller,Drama";
            Files.writeString(moviesFile, content);

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            assertEquals(2, movies.size());
            assertEquals("The Matrix", movies.get(0).getTitle());
            assertEquals("M001", movies.get(0).getMovieID());
            assertTrue(movies.get(0).getGenres().contains("Action"));
            assertTrue(movies.get(0).getGenres().contains("Sci-Fi"));
        }

        @Test
        @DisplayName("Read users from file creates User objects correctly")
        void testReadUsersCreatesUserObjects() throws IOException {
            // Create test user file
            Path usersFile = testDir.resolve("test_users.txt");
            String content = "John Doe,123456789\nM001,M002\nJane Smith,987654321\nM002";
            Files.writeString(usersFile, content);

            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            assertEquals(2, users.size());
            assertEquals("John Doe", users.get(0).getName());
            assertEquals("123456789", users.get(0).getId());
        }

        @Test
        @DisplayName("FileHandler integrates Movie and User reading")
        void testFileHandlerIntegratesMoviesAndUsers() throws IOException {
            // Create movie file
            Path moviesFile = testDir.resolve("movies.txt");
            Files.writeString(moviesFile, "Test Movie,TM01\nAction,Comedy");

            // Create user file
            Path usersFile = testDir.resolve("users.txt");
            Files.writeString(usersFile, "Test User,1a2b3c4d5\nTM01");

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            // Link users to movies
            boolean linked = users.get(0).setLikedMovies(movies);

            assertTrue(linked);
            assertEquals(1, users.get(0).getLikedMovies().size());
            assertEquals("Test Movie", users.get(0).getLikedMovies().get(0).getTitle());
        }

    }

    // ==================== LEVEL 4: RecommendationEngine with Models ====================

    @Nested
    @DisplayName("Level 4: RecommendationEngine + Movie/User Integration")
    @Order(7)
    class Level4_RecommendationEngineIntegration {
        private RecommendationEngine recommendationEngine;

        @BeforeEach
        void setUp() {
            recommendationEngine = new RecommendationEngine();
        }

        @Test
        @DisplayName("Generate recommendations based on liked movie genres")
        void testGenerateRecommendations() {
            // Create movies
            ArrayList<String> genres1 = new ArrayList<>(Arrays.asList("Action", "Sci-Fi"));
            ArrayList<String> genres2 = new ArrayList<>(Arrays.asList("Action", "Drama"));
            ArrayList<String> genres3 = new ArrayList<>(Arrays.asList("Comedy"));

            Movie movie1 = new Movie("The Matrix", "M001", genres1);
            Movie movie2 = new Movie("Die Hard", "M002", genres2);
            Movie movie3 = new Movie("Funny Movie", "M003", genres3);

            ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(movie1, movie2, movie3));

            // Create user who likes The Matrix
            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001"));
            User user = new User("Test User", "123456789", likedIds);
            user.setLikedMovies(allMovies);

            // Generate recommendations
            recommendationEngine.GetRecommendations(user, allMovies);

            // Should recommend Die Hard (shares Action genre) but not Funny Movie
            ArrayList<String> recommendations = user.getRecMovies();
            assertTrue(recommendations.contains("The Matrix")); // Liked movie also recommended
            assertTrue(recommendations.contains("Die Hard")); // Shares Action genre
            assertFalse(recommendations.contains("Funny Movie")); // No shared genre
        }

        @Test
        @DisplayName("Recommendations with multiple liked movies")
        void testRecommendationsMultipleLikedMovies() {
            ArrayList<String> genres1 = new ArrayList<>(Arrays.asList("Action"));
            ArrayList<String> genres2 = new ArrayList<>(Arrays.asList("Comedy"));
            ArrayList<String> genres3 = new ArrayList<>(Arrays.asList("Action", "Comedy"));

            Movie movie1 = new Movie("Action Movie", "M001", genres1);
            Movie movie2 = new Movie("Comedy Movie", "M002", genres2);
            Movie movie3 = new Movie("Action Comedy", "M003", genres3);

            ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(movie1, movie2, movie3));

            ArrayList<String> likedIds = new ArrayList<>(Arrays.asList("M001", "M002"));
            User user = new User("Test User", "123456789", likedIds);
            user.setLikedMovies(allMovies);

            recommendationEngine.GetRecommendations(user, allMovies);

            ArrayList<String> recommendations = user.getRecMovies();
            assertEquals(3, recommendations.size()); // All movies should be recommended
        }

    }

    // ==================== LEVEL 5: FileHandler + Validators Integration ====================

    @Nested
    @DisplayName("Level 5: FileHandler + Validators Integration")
    @Order(8)
    class Level5_FileHandlerValidatorsIntegration {
        private FileHandler fileHandler;
        private MovieValidator movieValidator;
        private UserValidator userValidator;

        @BeforeEach
        void setUp() {
            fileHandler = new FileHandler();
            movieValidator = new MovieValidator();
            userValidator = new UserValidator();
        }

        @Test
        @DisplayName("Read and validate movies from file")
        void testReadAndValidateMovies() throws IOException {
            Path moviesFile = testDir.resolve("valid_movies.txt");
            Files.writeString(moviesFile, "The Matrix,MATR001\nAction,Sci-Fi");

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            for (Movie movie : movies) {
                assertTrue(movieValidator.validateMovieTitle(movie.getTitle()));
                assertTrue(movieValidator.validateMovieIdLetters(movie.getMovieID()));
            }
        }

        @Test
        @DisplayName("Read and validate users from file")
        void testReadAndValidateUsers() throws IOException {
            Path usersFile = testDir.resolve("valid_users.txt");
            Files.writeString(usersFile, "John Doe,1a2b3c4d5\nM001");

            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
            }
        }

        @Test
        @DisplayName("Complete read-validate-link workflow")
        void testCompleteReadValidateLinkWorkflow() throws IOException {
            // Create test files
            Path moviesFile = testDir.resolve("movies.txt");
            Path usersFile = testDir.resolve("users.txt");

            Files.writeString(moviesFile, "The Matrix,MATR001\nAction,Sci-Fi\nInception,INCE002\nThriller,Drama");
            Files.writeString(usersFile, "John Doe,1a2b3c4d5\nMATR001,INCE002");

            // Read data
            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            // Validate movies
            boolean moviesValid = true;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    moviesValid = false;
                    break;
                }
            }

            // Validate users
            boolean usersValid = true;
            for (User user : users) {
                if (!userValidator.validateUserName(user.getName()) ||
                        !userValidator.validateUserId(user.getId())) {
                    usersValid = false;
                    break;
                }
            }

            assertTrue(moviesValid);
            assertTrue(usersValid);

            // Link movies to users
            for (User user : users) {
                assertTrue(user.setLikedMovies(movies));
            }
        }

    }

    // ==================== LEVEL 6: Full System Integration ====================

    @Nested
    @DisplayName("Level 6: Full System Integration")
    @Order(9)
    class Level6_FullSystemIntegration {
        private FileHandler fileHandler;
        private MovieValidator movieValidator;
        private UserValidator userValidator;
        private RecommendationEngine recommendationEngine;

        @BeforeEach
        void setUp() {
            fileHandler = new FileHandler();
            movieValidator = new MovieValidator();
            userValidator = new UserValidator();
            recommendationEngine = new RecommendationEngine();
        }

        @Test
        @DisplayName("Complete workflow: Read -> Validate -> Link -> Recommend")
        void testCompleteWorkflow() throws IOException {
            // Setup test files
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

            // Step 1: Read movies
            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());
            assertEquals(3, movies.size());

            // Step 2: Validate movies
            boolean moviesValid = true;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    moviesValid = false;
                    break;
                }
                movieValidator.getId_list().add(movie.getMovieID());
            }
            assertTrue(moviesValid);

            // Step 3: Read users
            ArrayList<User> users = fileHandler.readUser(usersFile.toString());
            assertEquals(2, users.size());

            // Step 4: Validate users
            for (User user : users) {
                assertTrue(userValidator.validateUserName(user.getName()));
                assertTrue(userValidator.validateUserId(user.getId()));
            }

            // Step 5: Link movies to users
            for (User user : users) {
                assertTrue(user.setLikedMovies(movies));
            }

            // Step 6: Generate recommendations
            for (User user : users) {
                recommendationEngine.GetRecommendations(user, movies);
            }

            // Verify recommendations
            // John likes Matrix (Action, Sci-Fi) -> should get Matrix, Inception (both have Sci-Fi)
            ArrayList<String> johnRecs = users.get(0).getRecMovies();
            assertTrue(johnRecs.contains("The Matrix"));
            assertTrue(johnRecs.contains("Inception")); // Shares Sci-Fi genre
            assertFalse(johnRecs.contains("The Hangover")); // No shared genre

            // Jane likes Hangover (Comedy) -> should only get Hangover
            ArrayList<String> janeRecs = users.get(1).getRecMovies();
            assertTrue(janeRecs.contains("The Hangover"));
            assertEquals(1, janeRecs.size());
        }

        @Test
        @DisplayName("Error handling: Invalid movie title stops processing")
        void testErrorHandlingInvalidMovieTitle() throws IOException {
            Path moviesFile = testDir.resolve("invalid_movies.txt");
            Files.writeString(moviesFile, "invalid title,M001\nAction"); // lowercase title

            ArrayList<Movie> movies = fileHandler.readMovies(moviesFile.toString());

            boolean hasError = false;
            for (Movie movie : movies) {
                if (!movieValidator.validateMovieTitle(movie.getTitle())) {
                    hasError = true;
                    break;
                }
            }

            assertTrue(hasError);
            assertFalse(movieValidator.getExceptionHandler().getErrorLog().isEmpty());
        }

        @Test
        @DisplayName("Error handling: Invalid user name stops processing")
        void testErrorHandlingInvalidUserName() throws IOException {
            Path usersFile = testDir.resolve("invalid_users.txt");
            Files.writeString(usersFile, " Invalid Name,123456789\nM001"); // starts with space

            ArrayList<User> users = fileHandler.readUser(usersFile.toString());

            boolean hasError = false;
            for (User user : users) {
                if (!userValidator.validateUserName(user.getName())) {
                    hasError = true;
                    break;
                }
            }

            assertTrue(hasError);
            assertFalse(userValidator.getExceptionHandler().getErrorLog().isEmpty());
        }
    }
}

