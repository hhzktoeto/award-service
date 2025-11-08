package ru.t2.awardservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.exception.UnsupportedFileExtensionException;
import ru.t2.awardservice.processor.MultipartFileProcessor;
import ru.t2.awardservice.processor.MultipartFileProcessorSupplier;
import ru.t2.awardservice.repository.AwardRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AwardServiceTest {

    @Mock
    private AwardRepository mockRepository;
    @Mock
    private MultipartFileProcessorSupplier<Award> mockProcessorSupplier;
    @Mock
    private MultipartFileProcessor<Award> mockProcessor;
    @InjectMocks
    private AwardService awardService;

    @Mock
    private MultipartFile mockFile;

    private static final String TEST_FILE_NAME = "test";
    private static final String VALID_EXTENSION = "csv";
    private static final String INVALID_EXTENSION = "png";

    private final List<Award> testAwards = List.of(new Award());

    @Test
    void upload_processorFoundAndProcessedFile_shouldSaveAllToDatabase() {
        when(mockFile.getName()).thenReturn(TEST_FILE_NAME + "." + VALID_EXTENSION);
        when(mockProcessorSupplier.getProcessor(VALID_EXTENSION)).thenReturn(mockProcessor);
        when(mockProcessor.process(mockFile)).thenReturn(testAwards);
        when(mockRepository.saveAll(testAwards)).thenReturn(testAwards);

        List<Award> result = awardService.upload(mockFile);

        assertAll(
                () -> verify(mockFile, times(1)).getName(),
                () -> verify(mockProcessorSupplier, times(1)).getProcessor(VALID_EXTENSION),
                () -> verify(mockProcessor, times(1)).process(mockFile),
                () -> verify(mockRepository, times(1)).saveAll(testAwards),
                () -> assertIterableEquals(testAwards, result)
        );
    }

    @Test
    void upload_processorNotFound_shouldThrowUnsupportedFileExtensionException() {
        when(mockFile.getName()).thenReturn(TEST_FILE_NAME + "." + INVALID_EXTENSION);
        when(mockProcessorSupplier.getProcessor(INVALID_EXTENSION)).thenThrow(UnsupportedFileExtensionException.class);

        assertAll(
                () -> assertThrows(UnsupportedFileExtensionException.class, () -> awardService.upload(mockFile)),
                () -> verify(mockFile, times(1)).getName(),
                () -> verify(mockProcessorSupplier, times(1)).getProcessor(INVALID_EXTENSION),
                () -> verifyNoInteractions(mockProcessor, mockRepository)
        );
    }
}
