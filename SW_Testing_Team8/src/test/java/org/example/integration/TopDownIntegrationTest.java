package org.example.integration;

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
 *
 * Top-Down approach starts testing from the highest level module (main application)
 * and progressively integrates lower-level modules using stubs/mocks initially.
 *
 * Testing Order (from top to bottom):
 * Level 1: MovieRecommendationApp (Main) - with stubbed dependencies
 * Level 2: FileHandler, Validators - with stubbed models
 * Level 3: RecommendationEngine - with stubbed data
 * Level 4: FileWriteHandler - with stubbed user data
 * Level 5: Models (Movie, User) - actual implementations
 * Level 6: Full Integration - all real components
 *
 * In Top-Down testing, we use STUBS to simulate lower-level modules
 * until they are integrated.
 */

/*
Leve1 1,2: Kirllos
Level 3,4: George
Level 5,6: Andro
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TopDownIntegrationTest {

    private final String referencePath = "src/test/java/org/example/integration/TopDown_Text_Test";
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

    }

    // ==================== LEVEL 4: RecommendationEngine Integration ====================

    @Nested
    @DisplayName("Level 4: RecommendationEngine with Models Integration")
    @Order(4)
    class Level4_RecommendationEngineTests {


    }

    // ==================== LEVEL 5: Models Integration ====================

    @Nested
    @DisplayName("Level 5: Models Integration Tests")
    @Order(5)
    class Level5_ModelsIntegrationTests {


    }

    // ==================== LEVEL 6: Full System Integration ====================

    @Nested
    @DisplayName("Level 6: Complete System Integration")
    @Order(6)
    class Level6_CompleteSystemIntegration {


    }
}

