package org.DataFlowTesting;

import org.Models.Movie;
import org.Models.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class setLikedMoviesTest {
    private User user;
    private ArrayList<Movie> availableMovies;

    @BeforeEach
    public void setup() {
        availableMovies = new ArrayList<>();

        // Setup available movies with genres
        ArrayList<String> action = new ArrayList<>();
        action.add("Action");

        ArrayList<String> comedy = new ArrayList<>();
        comedy.add("Comedy");

        ArrayList<String> drama = new ArrayList<>();
        drama.add("Drama");

        availableMovies.add(new Movie("Movie 1", "M001", action));
        availableMovies.add(new Movie("Movie 2", "M002", comedy));
        availableMovies.add(new Movie("Movie 3", "M003", drama));
    }

    /**
     * Test Case 1: likedMoviesId is null
     * Covers path: Entry,1,2
     * Tests DU pair for likedMoviesId: (Entry, 1)
     */
    @Test
    public void testLikedMoviesIdIsNull() {
        ArrayList<String> nullList = null;
        user = new User("John", "U001", nullList);
        user.setLikedMovies(null);

        boolean result = user.setLikedMovies(availableMovies);

        assertFalse(result);
        assertTrue(user.getLikedMovies().isEmpty());
    }

    /**
     * Test Case 2: likedMoviesId is empty
     * Covers path: Entry,1,2
     * Tests DU pair for likedMoviesId: (Entry, 1)
     */
    @Test
    public void testLikedMoviesIdIsEmpty() {
        ArrayList<String> emptyList = new ArrayList<>();
        user = new User("Jane", "U002", emptyList);

        boolean result = user.setLikedMovies(availableMovies);

        assertFalse(result);
        assertTrue(user.getLikedMovies().isEmpty());
    }

    /**
     * Test Case 3: Single matching movie
     * Covers paths:
     * - Entry,1,3,4,5,6,7,8,5,4,9
     * Tests DU pairs:
     * - likedMoviesId: (Entry, 5), (Entry, 9)
     * - availableMovies: (Entry, 4)
     * - count: (3, 8), (8, 9)
     * - m: (4, 6), (4, 7)
     * - id: (5, 6)
     * - likedMovies: (7, 7)
     */
    @Test
    public void testSingleMatchingMovie() {
        ArrayList<String> liked = new ArrayList<>();
        liked.add("M001");
        user = new User("Alice", "U003", liked);

        boolean result = user.setLikedMovies(availableMovies);

        assertTrue(result);
        assertEquals(1, user.getLikedMovies().size());
        assertEquals("M001", user.getLikedMovies().get(0).getMovieID());
    }

    /**
     * Test Case 4: Multiple matching movies
     * Covers paths:
     * - Entry ,1, 3 , 4 , 5 , 6, 7, 8, 5, 6, 7 , 8 , 5 , 4 , 9
     * Tests DU pairs:
     * - count: (3, 8), (8, 8), (8, 9)
     * - likedMovies: (7, 7) - multiple times
     */
    @Test
    public void testMultipleMatchingMovies() {
        ArrayList<String> liked = new ArrayList<>();
        liked.add("M001");
        liked.add("M002");
        liked.add("M003");
        user = new User("Bob", "U004", liked);

        boolean result = user.setLikedMovies(availableMovies);

        assertTrue(result);
        assertEquals(3, user.getLikedMovies().size());
    }

    /**
     * Test Case 5: Some matching movies (partial match)
     * Covers path: Entry ,1,3 ,4,5, 6,7 ,8, 5, 6, 8, 5,4, 9
     * Tests when not all liked movie IDs are found
     */
    @Test
    public void testPartialMatchingMovies() {
        ArrayList<String> liked = new ArrayList<>();
        liked.add("M001");
        liked.add("M999"); // This ID doesn't exist
        user = new User("Charlie", "U005", liked);

        boolean result = user.setLikedMovies(availableMovies);

        assertFalse(result); // Should return false because count < likedMoviesId.size()
        assertEquals(1, user.getLikedMovies().size());
        assertEquals("M001", user.getLikedMovies().get(0).getMovieID());
    }

    /**
     * Test Case 6: No matching movies
     * Covers path: Entry , 1 , 3 , 4 , 5 , 6 , 5 , 4 , 9
     * Tests when loop executes but no matches are found
     */
    @Test
    public void testNoMatchingMovies() {
        ArrayList<String> liked = new ArrayList<>();
        liked.add("M999");
        liked.add("M888");
        user = new User("David", "U006", liked);

        boolean result = user.setLikedMovies(availableMovies);

        assertFalse(result);
        assertTrue(user.getLikedMovies().isEmpty());
    }

    /**
     * Test Case 7: Empty availableMovies list
     * Covers path: Entry , 1 , 3 , 4 , 9
     * Tests when outer loop doesn't execute
     */
    @Test
    public void testEmptyAvailableMovies() {
        ArrayList<String> liked = new ArrayList<>();
        liked.add("M001");
        user = new User("Eve", "U007", liked);
        availableMovies.clear();

        boolean result = user.setLikedMovies(availableMovies);

        assertFalse(result);
        assertTrue(user.getLikedMovies().isEmpty());
    }
}