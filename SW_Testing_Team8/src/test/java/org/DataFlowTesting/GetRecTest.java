package org.DataFlowTesting;

import org.Models.Movie;
import org.Models.User;
import org.example.RecommendationEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetRecTest {


        // ========================================
        // ALL-DEFS COVERAGE TESTS
        // ========================================


        @Test
        @DisplayName("All-Defs: path <1,2,3,4,5,6,7,8,9>")
        void testAllDefs_Path1_2_3_4_5_6_7_8_9() {

            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertEquals(1, user.getRecMovies().size());
            assertTrue(user.getRecMovies().contains("Action Film"));
        }


        // ========================================
        // ALL-USES COVERAGE TESTS
        // ========================================



        @Test
        @DisplayName("All-Uses: path <1,2,3,11> - user (1,11)")
        void testAllUses_User_1_11() {
            User user = new User("Test User", "123456789", new ArrayList<>());
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
            assertTrue(user.getRecMovies().isEmpty());
        }

        @Test
        @DisplayName("All-Uses: path <1,2,3,4> - movies (1,4)")
        void testAllUses_Movies_1_4() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
        }


        @Test
        @DisplayName("All-Uses: path <2,3,4,5,6,7,8,9> - RecMovies (2,9)")
        void testAllUses_RecMovies_2_9() {
            RecommendationEngine engine = new RecommendationEngine();
            User user = createUserWithMultiGenreMovie();

            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010", new ArrayList<>(Arrays.asList("Action", "Thriller"))));
            movies.add(new Movie("Drama Film", "MOV011", new ArrayList<>(Arrays.asList("Drama", "Romance"))));
            movies.add(new Movie("Comedy Film", "MOV012", new ArrayList<>(Arrays.asList("Comedy"))));

            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
            assertTrue(user.getRecMovies().contains("Drama Film"));
            assertTrue(user.getRecMovies().contains("Comedy Film"));
        }

        @Test
        @DisplayName("All-Uses: path <9,10> - RecMovies (9,10)")
        void testAllUses_RecMovies_9_10() {
            RecommendationEngine engine = new RecommendationEngine();
            User user = createUserWithSingleGenreMovie();

            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010", new ArrayList<>(Arrays.asList("Action", "Thriller"))));
            movies.add(new Movie("Action Film", "MOV010", new ArrayList<>(Arrays.asList("Action", "Thriller"))));

            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
            assertEquals(1, user.getRecMovies().size());
        }

        @Test
        @DisplayName("All-Uses: path <9,10,4,3,11> - RecMovies (9,11)")
        void testAllUses_RecMovies_9_11() {
            RecommendationEngine engine = new RecommendationEngine();
            User user = createUserWithMultiGenreMovie();

            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010", new ArrayList<>(Arrays.asList("Action", "Thriller"))));
            movies.add(new Movie("Drama Film", "MOV011", new ArrayList<>(Arrays.asList("Drama", "Romance"))));
            movies.add(new Movie("Comedy Film", "MOV012", new ArrayList<>(List.of("Comedy"))));

            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
            assertTrue(user.getRecMovies().contains("Drama Film"));
            assertTrue(user.getRecMovies().contains("Comedy Film"));
            assertEquals(user.getRecMovies(), user.getRecMovies());
        }

        @Test
        @DisplayName("All-Uses: path <9,10,4,5> - RecMovies (9,5)")
        void testAllUses_RecMovies_9_5() {
            RecommendationEngine engine = new RecommendationEngine();
            User user = createUserWithSingleGenreMovie();

            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010", new ArrayList<>(List.of("Action"))));
            movies.add(new Movie("Action Film", "MOV010", new ArrayList<>(List.of("Action"))));

            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
            assertEquals(1, user.getRecMovies().size());
        }





        // ========================================
        // ALL-DU-PATHS COVERAGE TESTS
        // ========================================

        @Test
        @DisplayName("All-DU-Paths: path <1,2> - user (1,2)")
        void testAllDUPaths_User_1_2() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
        }

        @Test
        @DisplayName("All-DU-Paths: path <1,2,3,11> - user (1,11)")
        void testAllDUPaths_User_1_11() {
            User user = new User("Test User", "123456789", new ArrayList<>());
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
            assertEquals(0, user.getRecMovies().size());
        }

        @Test
        @DisplayName("All-DU-Paths: path <1,2,3,4> - movies (1,4)")
        void testAllDUPaths_Movies_1_4() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
        }

        @Test
        @DisplayName("All-DU-Paths: path <2,3> - likedMovies (2,3)")
        void testAllDUPaths_LikedMovies_2_3() {
            User user = createUserWithMultiGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
        }

        @Test
        @DisplayName("All-DU-Paths: path <2,3,4,5> - RecMovies (2,5)")
        void testAllDUPaths_RecMovies_2_5() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertEquals(1, user.getRecMovies().size());
        }

        @Test
        @DisplayName("All-DU-Paths: path <2,3,4,5,6,7,8,9> - RecMovies (2,9)")
        void testAllDUPaths_RecMovies_2_9() {
            User user = createUserWithMultiGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action", "Thriller"))));
            movies.add(new Movie("Drama Film", "MOV011",
                    new ArrayList<>(Arrays.asList("Drama", "Romance"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().size() >= 2);
        }

        @Test
        @DisplayName("All-DU-Paths: path <2,3,11> - RecMovies (2,11)")
        void testAllDUPaths_RecMovies_2_11() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
        }

        @Test
        @DisplayName("All-DU-Paths: path <9,10> - RecMovies (9,10)")
        void testAllDUPaths_RecMovies_9_10() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action", "Thriller"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
        }

        @Test
        @DisplayName("All-DU-Paths: path <9,10,4,3,11> - RecMovies (9,11)")
        void testAllDUPaths_RecMovies_9_11() {
            User user = createUserWithMultiGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));
            movies.add(new Movie("Drama Film", "MOV011",
                    new ArrayList<>(Arrays.asList("Drama"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
            assertTrue(user.getRecMovies().size() >= 1);
        }

        @Test
        @DisplayName("All-DU-Paths: path <9,10,4,5> - RecMovies (9,5)")
        void testAllDUPaths_RecMovies_9_5() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(List.of("Action"))));
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(List.of("Action"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertEquals(1, user.getRecMovies().size());
        }

        @Test
        @DisplayName("All-DU-Paths: path <3,4,5,6> - lm (3,6)")
        void testAllDUPaths_Lm_3_6() {
            User user = createUserWithMultiGenreMovie();
            ArrayList<Movie> movies = createTestMovies();

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertNotNull(user.getRecMovies());
        }

        @Test
        @DisplayName("All-DU-Paths: path <4,5> - m (4,5)")
        void testAllDUPaths_M_4_5() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertEquals(1, user.getRecMovies().size());
        }

        @Test
        @DisplayName("All-DU-Paths: path <4,5,6,7> - m (4,7)")
        void testAllDUPaths_M_4_7() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action", "Thriller"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
        }

        @Test
        @DisplayName("All-DU-Paths: path <4,5,6,7,8,9> - m (4,9)")
        void testAllDUPaths_M_4_9() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
        }

        @Test
        @DisplayName("All-DU-Paths: path <4,5,6,7,8,9,10> - m (4,10)")
        void testAllDUPaths_M_4_10() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action", "Thriller"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
            assertEquals(1, user.getRecMovies().size());
        }

        @Test
        @DisplayName("All-DU-Paths: path <6,7,8> - lgenre (6,8)")
        void testAllDUPaths_Lgenre_6_8() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
        }

        @Test
        @DisplayName("All-DU-Paths: path <7,8> - genre (7,8)")
        void testAllDUPaths_Genre_7_8() {
            User user = createUserWithSingleGenreMovie();
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Action Film", "MOV010",
                    new ArrayList<>(Arrays.asList("Action"))));

            RecommendationEngine engine = new RecommendationEngine();
            engine.GetRecommendations(user, movies);

            assertTrue(user.getRecMovies().contains("Action Film"));
        }


        // HELPER METHODS

        private User createUserWithSingleGenreMovie() {
            User user = new User("Test User", "123456789", new ArrayList<>());
            Movie likedMovie = new Movie("Action Movie", "MOV001",
                    new ArrayList<>(Arrays.asList("Action")));
            user.getLikedMovies().add(likedMovie);
            return user;
        }

        private User createUserWithMultiGenreMovie() {
            User user = new User("Test User", "123456789", new ArrayList<>());
            Movie likedMovie = new Movie("Multi Genre", "MOV001",
                    new ArrayList<>(Arrays.asList("Action", "Drama", "Comedy")));
            user.getLikedMovies().add(likedMovie);
            return user;
        }

        private ArrayList<Movie> createTestMovies() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(new Movie("The Dark Knight", "MOV010",
                    new ArrayList<>(Arrays.asList("Action", "Thriller"))));
            movies.add(new Movie("Drama Film", "MOV011",
                    new ArrayList<>(Arrays.asList("Drama", "Romance"))));
            movies.add(new Movie("Toy Story", "MOV012",
                    new ArrayList<>(Arrays.asList("Comedy"))));
            movies.add(new Movie("IT", "MOV013",
                    new ArrayList<>(Arrays.asList("Horror"))));
            return movies;
        }

}
