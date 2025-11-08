package ru.t2.awardservice.test.data;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.entity.Employee;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.List;

public final class TestData {

    private TestData() {}

    public static MultipartFile validAwardsCSV() {
        try {
            return new MockMultipartFile("awards.csv", new ClassPathResource("awards.csv").getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static MultipartFile invalidAwardsCSV() {
        try {
            return new MockMultipartFile("invalid_awards.csv", new ClassPathResource("invalid_awards.csv").getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static MultipartFile validAwardsXLS() {
        try {
            return new MockMultipartFile("awards.xls", new ClassPathResource("awards.xls").getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static MultipartFile validAwardsXLSX() {
        try {
            return new MockMultipartFile("awards.xlsx", new ClassPathResource("awards.xlsx").getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static MultipartFile invalidAwardsXLSX() {
        try {
            return new MockMultipartFile("invalid_awards.xlsx", new ClassPathResource("invalid_awards.xlsx").getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<Award> awards() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFullName("Иванов Иван Иванович");
        Award award = new Award();
        award.setId(1L);
        award.setEmployee(employee);
        award.setDate(LocalDate.parse("2023-11-07"));
        award.setName("Работник месяца");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFullName("Алексеев Алексей Алексеевич");
        Award award2 = new Award();
        award2.setId(2L);
        award2.setEmployee(employee2);
        award2.setDate(LocalDate.parse("2023-12-09"));
        award2.setName("За особые заслуги");

        Employee employee3 = new Employee();
        employee3.setId(3L);
        employee3.setFullName("Иванова Иоанна Ивановна");
        Award award3 = new Award();
        award3.setId(3L);
        award3.setEmployee(employee3);
        award3.setDate(LocalDate.parse("2025-03-14"));
        award3.setName("За вклад в науку");

        Employee employee4 = new Employee();
        employee4.setId(4L);
        employee4.setFullName("Валериева Валерия Валериевна");
        Award award4 = new Award();
        award4.setId(4L);
        award4.setEmployee(employee4);
        award4.setDate(LocalDate.parse("2025-05-22"));
        award4.setName("Молодец");

        return List.of(award, award2, award3, award4);
    }
}
