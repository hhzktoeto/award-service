package ru.t2.awardservice.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t2.awardservice.exception.UnsupportedFileExtensionException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MultipartFileProcessorSupplier<T> {

    private final List<MultipartFileProcessor<T>> processors;

    public MultipartFileProcessor<T> getProcessor(String fileExtension) {
        return processors.stream()
                .filter(processor -> processor.getSupportedFileExtensions().contains(fileExtension))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileExtensionException(fileExtension));
    }
}
