package ru.t2.awardservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t2.awardservice.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public boolean exists(long id) {
        return repository.existsById(id);
    }
}
