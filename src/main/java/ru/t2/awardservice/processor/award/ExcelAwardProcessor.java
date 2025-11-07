package ru.t2.awardservice.processor.award;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.service.EmployeeService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ExcelAwardProcessor extends AwardProcessor {

    public ExcelAwardProcessor(EmployeeService employeeService) {
        super(employeeService);
    }

    @Override
    protected List<Award> parseFile(MultipartFile file) throws IOException, NumberFormatException, DateTimeParseException {
        List<Award> awards = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            // skipping header
            for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
                Row row = sheet.getRow(i);
                if (this.isEmpty(row)) {
                    continue;
                }
                long employeeId = Long.parseLong(row.getCell(EMPLOYEE_ID_INDEX).getStringCellValue());
                String employeeFullName = row.getCell(EMPLOYEE_FULL_NAME_INDEX).getStringCellValue();
                long awardId = Long.parseLong(row.getCell(AWARD_ID_INDEX).getStringCellValue());
                String awardName = row.getCell(AWARD_NAME_INDEX).getStringCellValue();
                LocalDate awardDate = LocalDate.parse(row.getCell(AWARD_DATE_INDEX).getStringCellValue(), DateTimeFormatter.ISO_DATE);

                Optional<Award> optionalAward = this.getAward(employeeId, employeeFullName, awardId, awardName, awardDate);
                optionalAward.ifPresent(awards::add);
            }
        }

        return awards;
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of("xls", "xlsx");
    }
}
