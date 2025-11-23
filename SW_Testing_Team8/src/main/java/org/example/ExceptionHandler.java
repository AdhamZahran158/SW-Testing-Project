package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExceptionHandler {
    private List<String> errorLog;

    public ExceptionHandler() {
        errorLog = new ArrayList<String>();
    }
    public void throwValidationError(String message) {
        errorLog.add(message);
        throw new IllegalArgumentException(message);
    }


    public void logError(String message) {
        errorLog.add(message);
    }

    public List<String> getErrorLog() {
        return Collections.unmodifiableList(errorLog);
    }

    public void clearErrorLog() {
            errorLog.clear();
    }

    public void printErrorLog() {
        for(String error : errorLog) {
            System.out.println(error);
        }
    }
}
