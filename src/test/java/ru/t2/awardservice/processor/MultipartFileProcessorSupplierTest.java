package ru.t2.awardservice.processor;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.exception.UnsupportedFileExtensionException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultipartFileProcessorSupplierTest {

    private final MultipartFileProcessorSupplier<?> supplier = new MultipartFileProcessorSupplier<>(
            List.of(new TestAwardsProcessor())
    );

    private static final String VALID_EXTENSION = "csv";
    private static final String INVALID_EXTENSION = "txt";

    @Test
    void getProcessor_processorFound_shouldReturnProcessor() {
        MultipartFileProcessor<?> processor = supplier.getProcessor(VALID_EXTENSION);

        assertAll(
                () -> assertNotNull(processor),
                () -> assertInstanceOf(TestAwardsProcessor.class, processor)
        );
    }

    @Test
    void getProcessor_processorNotFound_shouldThrowUnsupportedFileExtensionException() {
        assertThrows(UnsupportedFileExtensionException.class, () -> supplier.getProcessor(INVALID_EXTENSION));
    }

    private static final class TestAwardsProcessor implements MultipartFileProcessor<Object> {

        @Override
        public List<Object> process(MultipartFile file) {
            return Collections.emptyList();
        }

        @Override
        public List<String> getSupportedFileExtensions() {
            return List.of("csv");
        }
    }
}
