# Movie Recommendation System

A Java-based movie recommendation application that reads movie and user data from files, validates the input, and generates movie recommendations based on user preferences.

## Table of Contents

- [Overview](#overview)
- [Project Architecture](#project-architecture)
- [UML Class Diagram](#uml-class-diagram)
- [Components](#components)
  - [Main Application](#main-application)
  - [Models](#models)
  - [Handlers](#handlers)
  - [Validators](#validators)
  - [Recommendation Engine](#recommendation-engine)
- [Unit Testing](#unit-testing)
- [Getting Started](#getting-started)
- [File Formats](#file-formats)
- [Technologies Used](#technologies-used)

---

## Overview

The Movie Recommendation System is designed to:
1. Read movies and users from input files
2. Validate movie and user data according to specific rules
3. Link users with their liked movies
4. Generate personalized movie recommendations based on genre matching
5. Output recommendations to a file

---

## Project Architecture

```
SW_Testing_Team8/
├── src/
│   ├── main/java/
│   │   ├── org/example/
│   │   │   ├── MovieRecommendationApp.java    # Main entry point
│   │   │   ├── FileHandler.java               # File reading operations
│   │   │   ├── FileWriteHandler.java          # File writing operations
│   │   │   ├── MovieValidator.java            # Movie validation logic
│   │   │   ├── UserValidator.java             # User validation logic
│   │   │   ├── RecommendationEngine.java      # Recommendation generation
│   │   │   └── ExceptionHandler.java          # Error handling
│   │   └── org/Models/
│   │       ├── Movie.java                     # Movie data model
│   │       └── User.java                      # User data model
│   └── test/java/
│       ├── org/example/
│       │   ├── ExceptionHandlerTest.java
│       │   ├── FileHandlerTest.java
│       │   ├── FileWriteHandlerTest.java
│       │   ├── MovieValidatorTest.java
│       │   ├── UserValidatorTest.java
│       │   ├── RecommendationEngineTest.java
│       │   └── integration/                   # Integration tests
│       └── org/Models/
│           ├── MovieTest.java
│           └── UserTest.java
├── pom.xml
├── movies.txt
└── users.txt
```

---

## UML Class Diagram

![UML Class Diagram](../SW_Testing_Team8.png)

The diagram illustrates the relationships between all system components:
- **MovieRecommendationApp** orchestrates the entire flow
- **FileHandler** reads data into **Movie** and **User** models
- **MovieValidator** and **UserValidator** use **ExceptionHandler** for validation
- **RecommendationEngine** processes user preferences
- **FileWriteHandler** outputs results

---

## Components

### Main Application

#### `MovieRecommendationApp`
The central orchestrator that coordinates all system components:
- Prompts user for file paths
- Reads and validates movies and users
- Generates recommendations
- Writes output to file

```java
// Main workflow:
// 1. Read movies from file
// 2. Validate each movie (title, ID, genres)
// 3. Read users from file
// 4. Validate each user (name, ID, liked movies)
// 5. Generate recommendations
// 6. Write output
```

---

### Models

#### `Movie`

Represents a movie with the following attributes:

| Attribute | Type | Description |
|-----------|------|-------------|
| `title` | String | Movie title |
| `movieID` | String | Unique movie identifier |
| `genres` | ArrayList\<String\> | List of genres |

#### `User`

Represents a user with the following attributes:

| Attribute | Type | Description |
|-----------|------|-------------|
| `name` | String | User name |
| `id` | String | Unique user identifier (9 characters) |
| `likedMoviesId` | ArrayList\<String\> | IDs of movies the user likes |
| `likedMovies` | ArrayList\<Movie\> | Linked Movie objects |
| `RecMovies` | ArrayList\<String\> | Recommended movie titles |

---

### Handlers

#### `FileHandler`
Responsible for reading data from files:
- `readMovies(String filePath)` - Reads movies from a file
- `readUser(String filePath)` - Reads users from a file

#### `FileWriteHandler`
Responsible for writing output:
- Writes user recommendations to output file
- Writes error messages when validation fails

#### `ExceptionHandler`
Centralized error handling:
- `throwValidationError(String message)` - Throws validation errors
- `logError(String message)` - Logs errors without throwing
- `getErrorLog()` - Returns list of logged errors
- `clearErrorLog()` - Clears error history

---

### Validators

#### `MovieValidator`

Validates movie data according to rules:

| Method | Validation Rule |
|--------|-----------------|
| `validateMovieTitle(String)` | Title must be ≥2 chars, capitalized words |
| `validateMovieIdLetters(String)` | ID letters must be uppercase |
| `checkUnique3Digits(String)` | Last 3 digits must be unique |
| `validateMovieIdFull(String)` | Minimum ID length of 4 characters |
| `validateMovieGenre(String)` | Genre must be ≥2 characters |

#### `UserValidator`

Validates user data according to rules:

| Method | Validation Rule |
|--------|-----------------|
| `validateUserName(String)` | Name must contain only letters and spaces |
| `validateUserId(String)` | Must be 9 chars, start with digit, specific format |
| `validateLikedMovieList(boolean)` | User must have at least one liked movie |

---

### Recommendation Engine

#### `RecommendationEngine`
Generates movie recommendations using genre-based matching:
- Analyzes user's liked movies
- Finds other movies with matching genres
- Returns list of recommended movie titles

```java
// Algorithm:
// For each liked movie:
//   For each available movie:
//     If genres match and not already recommended:
//       Add to recommendations
```

---

## Unit Testing

The project includes comprehensive unit tests for all components:

| Test Class | Tests For |
|------------|-----------|
| `MovieTest.java` | Movie model functionality |
| `UserTest.java` | User model functionality |
| `FileHandlerTest.java` | File reading operations |
| `FileWriteHandlerTest.java` | File writing operations |
| `MovieValidatorTest.java` | Movie validation logic |
| `UserValidatorTest.java` | User validation logic |
| `ExceptionHandlerTest.java` | Error handling |
| `RecommendationEngineTest.java` | Recommendation generation |

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieValidatorTest

# Run with verbose output
mvn test -X
```

---

## Getting Started

### Prerequisites
- Java 25 or higher
- Maven 3.6+

### Installation

1. Clone the repository
2. Navigate to project directory:
   ```bash
   cd SW_Testing_Team8
   ```
3. Build the project:
   ```bash
   mvn clean compile
   ```
4. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="org.example.MovieRecommendationApp"
   ```

---

## File Formats

### movies.txt
```
Movie Title,MovieID
Genre1,Genre2,Genre3
Movie Title 2,MovieID2
Genre1,Genre4
```

### users.txt
```
User Name,UserID
LikedMovieID1,LikedMovieID2
User Name 2,UserID2
LikedMovieID1,LikedMovieID3
```

---

## Technologies Used

- **Java 25** - Core programming language
- **Maven** - Build and dependency management
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework for tests
- **Apache Commons IO** - I/O utilities

---

## Team

**SW Testing Team 8**

---

## License

This project is part of the Software Testing course (Fall 2025).
