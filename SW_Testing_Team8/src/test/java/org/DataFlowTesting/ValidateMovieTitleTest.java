package org.DataFlowTesting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.example.MovieValidator;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateMovieTitleTest {

    // ========================================
    // ALL-DEFS COVERAGE TESTS
    // ========================================

    @Test
    @DisplayName("All-Defs: path <1,2,3,5,6,8,9,18> - result(init), charArray, i")
    void testAllDefs_InitialResult_Def() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("Movie");

        assertTrue(result);
    }

    @Test
    @DisplayName("All-Defs: path <1,2,3,5,6,7,18> - result(loop def)")
    void testAllDefs_Result_Defined_In_Loop() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("Movie 2");

        assertTrue(result);
    }

    @Test
    @DisplayName("All-Defs: path <1,2,3,5,8,9,10,15,16,17> - result(catch def)")
    void testAllDefs_Result_Defined_In_Catch() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("movie");

        assertFalse(result);
    }

    // ========================================
    // ALL-USES COVERAGE TESTS
    // ========================================

    @Test
    @DisplayName("All-Uses: path <1,2,3,18> - charArray (1,3), result (2,18)")
    void testAllUses_LengthCheck() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("Movie");

        assertTrue(result);
    }

    @Test
    @DisplayName("All-Uses: path <1,2,5,6,7,18> - charArray (1,6), i (5,6), result (7,18)")
    void testAllUses_DigitAndSpaceBranch() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("Movie 2");

        assertTrue(result);
    }

    @Test
    @DisplayName("All-Uses: path <1,2,5,8,9,18> - charArray (1,8), i (5,9)")
    void testAllUses_FirstCharacterUppercase() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("A");

        assertTrue(result);
    }

    @Test
    @DisplayName("All-Uses: path <1,2,5,8,11,18> - charArray (1,11), i (5,11)")
    void testAllUses_AfterSpaceUppercase() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("A B");

        assertTrue(result);
    }

    @Test
    @DisplayName("All-Uses: path <1,2,5,8,9,10,15,16,17> - result (16,17)")
    void testAllUses_CatchResultFalse() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("movie");

        assertFalse(result);
    }

    @Test
    @DisplayName("All-Uses: path <1,2,5,14,15,16,17> - invalid character use")
    void testAllUses_InvalidCharacter() {

        MovieValidator validator = new MovieValidator();

        boolean result = validator.validateMovieTitle("Movie@Title");

        assertFalse(result);
    }

    // ========================================
    // ALL-DU-PATHS COVERAGE TESTS
    // ========================================

    @Test
    @DisplayName("DU: <1,3,5,18> charArray, result(init)")
    void testDU_LengthCheck() {

        MovieValidator validator = new MovieValidator();

        assertTrue(validator.validateMovieTitle("Movie"));
    }

    @Test
    @DisplayName("DU: <1,3,5,6,7,18> charArray, i, result(loop)")
    void testDU_DigitAndSpace() {

        MovieValidator validator = new MovieValidator();

        assertTrue(validator.validateMovieTitle("Movie 2"));
    }

    @Test
    @DisplayName("DU: <1,3,5,6,8,9> first character uppercase")
    void testDU_FirstCharacterUppercase() {

        MovieValidator validator = new MovieValidator();

        assertTrue(validator.validateMovieTitle("A"));
    }

    @Test
    @DisplayName("DU: <1,3,5,6,8,9,11> uppercase after space")
    void testDU_AfterSpaceUppercase() {

        MovieValidator validator = new MovieValidator();

        assertTrue(validator.validateMovieTitle("A B"));
    }

    @Test
    @DisplayName("DU: <1,3,5,6,8,9,10,16,17> lowercase first letter")
    void testDU_LowercaseFirstLetter() {

        MovieValidator validator = new MovieValidator();

        assertFalse(validator.validateMovieTitle("movie"));
    }

    @Test
    @DisplayName("DU: <1,3,5,14,16,17> invalid character")
    void testDU_InvalidCharacter() {

        MovieValidator validator = new MovieValidator();

        assertFalse(validator.validateMovieTitle("Movie@Title"));
    }
}
