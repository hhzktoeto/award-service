package ru.t2.awardservice.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.t2.awardservice.dto.ErrorResponse;
import ru.t2.awardservice.exception.FileProcessingException;
import ru.t2.awardservice.exception.IncorrectFileDataException;
import ru.t2.awardservice.exception.UnexpectedBehaviourException;
import ru.t2.awardservice.exception.UnsupportedFileExtensionException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(UnsupportedFileExtensionException.class)
    public ErrorResponse handleUnsupportedFileExtensionException(UnsupportedFileExtensionException e) {
        this.logException(e);
        return ErrorResponse.builder()
                .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorResponse handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        this.logException(e);
        return ErrorResponse.builder()
                .code(HttpStatus.PAYLOAD_TOO_LARGE.value())
                .message("Uploaded file size is more than allowed 2MB")
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileProcessingException.class)
    public ErrorResponse handleFileProcessingException(FileProcessingException e) {
        this.logException(e);
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IncorrectFileDataException.class)
    public ErrorResponse handleIncorrectFileDataException(IncorrectFileDataException e) {
        this.logException(e);
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Uploaded file is invalid and can not be processed. Check the data inside the file")
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UnexpectedBehaviourException.class)
    public ErrorResponse handleUnexpectedBehaviourException(UnexpectedBehaviourException e) {
        this.logException(e);
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected internal server error occurred")
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnknownException(Exception e) {
        this.logException(e);
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error has occurred. Please try again later.")
                .build();
    }

    private void logException(Exception e) {
        log.error("Exception {}: {}", e.getClass().getCanonicalName(), e.getMessage());
        log.trace("Stack trace:", e);
    }
}
