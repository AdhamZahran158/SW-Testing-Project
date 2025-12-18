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


    }

    @Nested
    @DisplayName("Level 1: User Model Tests")
    @Order(2)
    class Level1_UserModelTests {


    }

    @Nested
    @DisplayName("Level 1: ExceptionHandler Tests")
    @Order(3)
    class Level1_ExceptionHandlerTests {

    }

    // ==================== LEVEL 2: Validators with ExceptionHandler ====================

    @Nested
    @DisplayName("Level 2: MovieValidator + ExceptionHandler Integration")
    @Order(4)
    class Level2_MovieValidatorIntegration {


    }

    @Nested
    @DisplayName("Level 2: UserValidator + ExceptionHandler Integration")
    @Order(5)
    class Level2_UserValidatorIntegration {


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

