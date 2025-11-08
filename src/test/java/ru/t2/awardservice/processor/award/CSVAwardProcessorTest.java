package ru.t2.awardservice.processor.award;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.exception.IncorrectFileDataException;
import ru.t2.awardservice.service.EmployeeService;
import ru.t2.awardservice.test.data.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVAwardProcessorTest {

    @Mock
    private EmployeeService mockEmployeeService;
    @InjectMocks
    private CSVAwardProcessor processor;

    private final List<Award> testAwards = TestData.awards();

    private final MultipartFile validAwardsFile = TestData.validAwardsCSV();
    private final MultipartFile invalidAwardsFile = TestData.invalidAwardsCSV();

    @Test
    void process_fileIsValid_shouldReturnParsedAwards() {
        when(mockEmployeeService.exists(anyLong())).thenReturn(true);

        List<Award> result = processor.process(validAwardsFile);

        assertAll(
                () -> verify(mockEmployeeService, times(4)).exists(anyLong()),
                () -> assertIterableEquals(testAwards, result)
        );
    }

    @Test
    void process_oneEmployeeIsNotInDatabase_shouldSkipOneEmployee() {
        when(mockEmployeeService.exists(1L)).thenReturn(true);
        when(mockEmployeeService.exists(2L)).thenReturn(true);
        when(mockEmployeeService.exists(3L)).thenReturn(true);
        when(mockEmployeeService.exists(4L)).thenReturn(false);

        List<Award> result = processor.process(validAwardsFile);

        assertAll(
                () -> verify(mockEmployeeService, times(4)).exists(anyLong()),
                () -> assertNotEquals(result.size(), testAwards.size()),
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result).extracting(Award::getName).doesNotContain("Молодец")
        );
    }

    @Test
    void process_allEmployeesAreNotInDatabase_shouldSkipAllEmployees() {
        when(mockEmployeeService.exists(anyLong())).thenReturn(false);

        List<Award> result = processor.process(validAwardsFile);

        assertThat(result).isEmpty();
    }

    @Test
    void process_fileIsInvalid_shouldThrowIncorrectFileDataException() {
        assertAll(
                () -> assertThrows(IncorrectFileDataException.class, () -> processor.process(invalidAwardsFile)),
                () -> verify(mockEmployeeService, atMost(3)).exists(anyLong())
        );
    }
}
