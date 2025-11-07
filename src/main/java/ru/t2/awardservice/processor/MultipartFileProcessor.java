package ru.t2.awardservice.processor;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MultipartFileProcessor<T> {

    List<T> process(MultipartFile file);

    List<String> getSupportedFileExtensions();
}
