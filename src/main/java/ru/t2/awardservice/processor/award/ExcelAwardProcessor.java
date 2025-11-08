package ru.t2.awardservice.processor.award;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.exception.UnexpectedBehaviourException;
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

        try (Workbook workbook = switch (FilenameUtils.getExtension(file.getName())) {
            case "xls" -> new HSSFWorkbook(file.getInputStream());
            case "xlsx" -> new XSSFWorkbook(file.getInputStream());
            default -> throw new UnexpectedBehaviourException("Non excel file reached excel processor class");
        }) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
                Row row = sheet.getRow(i);
                if (this.isEmpty(row)) {
                    continue;
                }
                long employeeId = Long.parseLong(this.getCellAsString(row.getCell(EMPLOYEE_ID_INDEX)));
                String employeeFullName = this.getCellAsString(row.getCell(EMPLOYEE_FULL_NAME_INDEX));
                long awardId = Long.parseLong(this.getCellAsString(row.getCell(AWARD_ID_INDEX)));
                String awardName = this.getCellAsString(row.getCell(AWARD_NAME_INDEX));
                LocalDate awardDate = LocalDate.parse(this.getCellAsString(row.getCell(AWARD_DATE_INDEX)), DateTimeFormatter.ISO_DATE);

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

    private String getCellAsString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    double num = cell.getNumericCellValue();
                    if (num == (long) num) {
                        yield String.valueOf((long) num);
                    } else {
                        yield String.valueOf(num);
                    }
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                yield switch (cellValue.getCellType()) {
                    case STRING -> cellValue.getStringValue();
                    case NUMERIC -> String.valueOf(cellValue.getNumberValue());
                    default -> null;
                };
            }
            default -> null;
        };
    }
}
