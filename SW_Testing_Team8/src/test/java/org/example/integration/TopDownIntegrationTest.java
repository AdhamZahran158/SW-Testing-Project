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


    }

    // ==================== LEVEL 2: FileHandler Integration ====================

    @Nested
    @DisplayName("Level 2: FileHandler Integration Tests")
    @Order(2)
    class Level2_FileHandlerIntegrationTests {


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


    }

    // ==================== LEVEL 6: Full System Integration ====================

    @Nested
    @DisplayName("Level 6: Complete System Integration")
    @Order(6)
    class Level6_CompleteSystemIntegration {


    }
}

