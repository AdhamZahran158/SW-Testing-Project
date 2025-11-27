package org.example;


import org.Models.Movie;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    private ExceptionHandler exceptionHandler;
    private List<String> userIdList;
    private final int UserIdLen = 9;

    public UserValidator() {
        exceptionHandler = new ExceptionHandler();
        userIdList = new ArrayList<>();
    }

    public void validateLikedMovieList(boolean result) {
        try{
        if(!result)
            exceptionHandler.throwValidationError("ERROR: No Liked Movies Entered");
        }
        catch (IllegalArgumentException ex)
        {
            exceptionHandler.logError(ex.getMessage());
        }
    }

    public boolean validateUserName(String userName) {
        boolean result = true;
        try {
            if (userName == null || userName.isEmpty() || userName.charAt(0) == ' ') {
                exceptionHandler.throwValidationError("ERROR: User Name " + userName + " is wrong");
            }


            for (char c : userName.toCharArray()) {
                if (!Character.isAlphabetic(c) && c != ' ') {
                    exceptionHandler.throwValidationError("ERROR: User Name " + userName + " is wrong");
                }
            }

        } catch (IllegalArgumentException e) {
            result = false;
            exceptionHandler.logError(e.getMessage());
            return result;
        }
        return result;
    }

    public boolean validateUserId(String userId) {
        boolean result = true;
        try {
            if (userId == null || userId.length() != UserIdLen) {
                exceptionHandler.throwValidationError("ERROR: User Id " + userId + " is wrong");
            }


            if (!Character.isDigit(userId.charAt(0))) {
                exceptionHandler.throwValidationError("ERROR: User Id " + userId + " is wrong");
            }


            for (char c : userId.toCharArray()) {
                if (!Character.isLetterOrDigit(c)) {
                    exceptionHandler.throwValidationError("ERROR: User Id " + userId + " is wrong");
                }
            }

            if (Character.isLetter(userId.charAt(userId.length() - 1)) && Character.isLetter(userId.charAt(userId.length() - 2))) {
                exceptionHandler.throwValidationError("ERROR: User Id " + userId + " is wrong");
            }
            // does it allow mixed characters?
            if (userIdList.contains(userId)) {
                exceptionHandler.throwValidationError("ERROR: User Id " + userId + " is wrong");
            }

        } catch (IllegalArgumentException e) {
            result = false;
            exceptionHandler.logError(e.getMessage());
            return result;
        }


        userIdList.add(userId);

        return result;
    }


}
