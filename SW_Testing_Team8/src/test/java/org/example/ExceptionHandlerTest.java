package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {
    private ExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ExceptionHandler();
    }

    @Test
    @DisplayName("Throw validation error adds to log")
    void testThrowValidationError() {
        try {
            handler.throwValidationError("Test error");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Test error", e.getMessage());
            assertEquals(1, handler.getErrorLog().size());
        }
    }

    @Test
    @DisplayName("Log error adds to log")
    void testLogError() {
        handler.logError("Test error");
        assertEquals(1, handler.getErrorLog().size());
        assertTrue(handler.getErrorLog().contains("Test error"));
    }

    @Test
    @DisplayName("Multiple errors logged correctly")
    void testMultipleErrors() {
        handler.logError("Error 1");
        handler.logError("Error 2");
        handler.logError("Error 3");

        assertEquals(3, handler.getErrorLog().size());
        assertEquals("Error 1", handler.getErrorLog().get(0));
        assertEquals("Error 2", handler.getErrorLog().get(1));
        assertEquals("Error 3", handler.getErrorLog().get(2));
    }

    @Test
    @DisplayName("Clear error log works")
    void testClearErrorLog() {
        handler.logError("Error 1");
        handler.logError("Error 2");
        assertEquals(2, handler.getErrorLog().size());

        handler.clearErrorLog();
        assertEquals(0, handler.getErrorLog().size());
    }

    @Test
    @DisplayName("Get error log returns unmodifiable list")
    void testGetErrorLogUnmodifiable() {
        handler.logError("Test error");
        List<String> log = handler.getErrorLog();

        assertThrows(UnsupportedOperationException.class, () -> {
            log.add("Another error");
        });
    }
}