package com.timmydont.wherearetherocas.exceptions;

public class ArgumentException extends Exception {

    public ArgumentException(String message, Object... items) {
        super(String.format(message, items));
    }
}
