package ru.t2.awardservice.exception;

public class UnsupportedFileExtensionException extends RuntimeException {

    public UnsupportedFileExtensionException(String wrongFileExtension) {
        super("File extension \"%s\" is not supported".formatted(wrongFileExtension));
    }
}
