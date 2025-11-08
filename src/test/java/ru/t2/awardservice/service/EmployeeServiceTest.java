package ru.t2.awardservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t2.awardservice.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository mockRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private static final long TEST_ID = 1L;

    @Test
    void exists_employeeExists_shouldReturnTrue() {
        when(mockRepository.existsById(TEST_ID)).thenReturn(true);

        assertTrue(employeeService.exists(TEST_ID));
    }

    @Test
    void exists_employeeDoesNotExist_shouldReturnFalse() {
        when(mockRepository.existsById(TEST_ID)).thenReturn(false);

        assertFalse(employeeService.exists(TEST_ID));
    }
}
