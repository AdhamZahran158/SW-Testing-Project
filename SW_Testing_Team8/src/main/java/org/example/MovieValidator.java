package org.example;

import org.Models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieValidator {

    private ExceptionHandler exceptionHandler;
    private final int minIdLength =4;
    private final int minTitleLength =2;
    private final int minGenreLength =2;
    private List<String> id_list;

    public  MovieValidator()
    {
        exceptionHandler = new ExceptionHandler();
        id_list = new ArrayList<>();
    }

    public void setId_list(List<String> id_list) {
        this.id_list = id_list;
    }

    public boolean validateMovieTitle(String title)
    {
        char[] charArray = title.toCharArray();
        boolean result=true;

        try {

            if(charArray.length<minTitleLength)
                exceptionHandler.throwValidationError("ERROR: Movie Title " + title + " is wrong");

            for (int i = 0; i < charArray.length; i++) {
                if (Character.isDigit(charArray[i]) || charArray[i] == ' ')
                    result = true;
                else if (Character.isAlphabetic(charArray[i])) {
                    if (i == 0) {
                        if (!Character.isUpperCase(charArray[i]))
                            exceptionHandler.throwValidationError("ERROR: Movie Title " + title + " is wrong");
                    } else if (charArray[i - 1] == ' ') {
                        if (!Character.isUpperCase(charArray[i]))
                            exceptionHandler.throwValidationError("ERROR: Movie Title " + title + " is wrong");
                    }
                }
                else
                {
                    exceptionHandler.throwValidationError("ERROR: Movie Title " + title + " is wrong");
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            result = false;
            exceptionHandler.logError(e.getMessage());
            return result;
        }
        return result;
    }

    public boolean validateMovieIdLetters(String id)
    {
        char[] charArray = id.toCharArray();
        boolean result=true;

        try {
            for (int i = 0; i < charArray.length-3; i++) {
                if (!Character.isAlphabetic(charArray[i]))
                    exceptionHandler.throwValidationError("ERROR: Movie Id letters " + id + " are wrong");
                else if (!Character.isUpperCase(charArray[i]))
                    exceptionHandler.throwValidationError("ERROR: Movie Id letters " + id + " are wrong");
            }
        }
        catch (IllegalArgumentException e)
        {
            result = false;
            exceptionHandler.logError(e.getMessage());
            return result;
        }
        return result;
    }

    public boolean checkUnique3Digits(String id)
    {
        char[] charArray = id.toCharArray();
        boolean result=true;
        int digitsExist =0;
        String id_digits = id.substring(id.length()-3);

        try {
            for (int i = charArray.length-3; i < charArray.length; i++) {
                if (!Character.isDigit(charArray[i]))
                    exceptionHandler.throwValidationError("ERROR: Movie Id numbers " + id + " are wrong");
            }

            for(String checking_id_digits : id_list)
            {
                checking_id_digits =  checking_id_digits.substring(checking_id_digits.length()-3);
                if (checking_id_digits.equals(id_digits))
                    digitsExist++;
            }

            if(digitsExist>1)
                exceptionHandler.throwValidationError("ERROR: Movie Id numbers " + id + " aren’t unique");


        }
        catch (IllegalArgumentException e)
        {
            result = false;
            exceptionHandler.logError(e.getMessage());
            return result;
        }
        return result;
    }

    public boolean validateMovieIdFull(String id)
    {
        if (id.length()<minIdLength)
            return false;
        else
            return validateMovieIdLetters(id) && checkUnique3Digits(id);
    }

    public boolean validateMovieGenre(String genre)
    {
        char[] charArray = genre.toCharArray();
        boolean result=true;

        try {

            if(genre.length()<minGenreLength)
                exceptionHandler.throwValidationError("ERROR: Movie Genre " + genre + " is wrong");

            for (char c : charArray) {
                if (!Character.isAlphabetic(c))
                    exceptionHandler.throwValidationError("ERROR: Movie Genre " + genre + " is wrong");
            }
        }
        catch (IllegalArgumentException e)
        {
            result = false;
            exceptionHandler.logError(e.getMessage());
            return result;
        }
        return result;
    }
}
