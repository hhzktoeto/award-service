package ru.t2.awardservice.processor.award;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.service.EmployeeService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CSVAwardProcessor extends AwardProcessor {

    public CSVAwardProcessor(EmployeeService employeeService) {
        super(employeeService);
    }

    @Override
    protected List<Award> parseFile(MultipartFile file) throws IOException, NumberFormatException, DateTimeParseException {
        List<Award> awards = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVFormat format = CSVFormat.Builder.create()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .get();

            try (CSVParser parser = format.parse(reader)) {
                for (CSVRecord csvRecord : parser) {
                    if (this.isEmpty(csvRecord)) {
                        continue;
                    }
                    long employeeId = Long.parseLong(csvRecord.get(EMPLOYEE_ID_INDEX));
                    String employeeFullName = csvRecord.get(EMPLOYEE_FULL_NAME_INDEX);
                    long awardId = Long.parseLong(csvRecord.get(AWARD_ID_INDEX));
                    String awardName = csvRecord.get(AWARD_NAME_INDEX);
                    LocalDate awardDate = LocalDate.parse(csvRecord.get(AWARD_DATE_INDEX), DateTimeFormatter.ISO_DATE);

                    Optional<Award> optionalAward = this.getAward(employeeId, employeeFullName, awardId, awardName, awardDate);
                    optionalAward.ifPresent(awards::add);
                }
            }
        }

        return awards;
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of("csv");
    }
}
