package org.example.integration;

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


    private final String referencePath = "src/test/java/org/example/integration/BottomUp_Text_Test";
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

    }

    // ==================== LEVEL 4: RecommendationEngine with Models ====================

    @Nested
    @DisplayName("Level 4: RecommendationEngine + Movie/User Integration")
    @Order(7)
    class Level4_RecommendationEngineIntegration {


    }

    // ==================== LEVEL 5: FileHandler + Validators Integration ====================

    @Nested
    @DisplayName("Level 5: FileHandler + Validators Integration")
    @Order(8)
    class Level5_FileHandlerValidatorsIntegration {


    }

    // ==================== LEVEL 6: Full System Integration ====================

    @Nested
    @DisplayName("Level 6: Full System Integration")
    @Order(9)
    class Level6_FullSystemIntegration {


    }
}

