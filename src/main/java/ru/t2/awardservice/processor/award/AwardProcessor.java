package ru.t2.awardservice.processor.award;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.entity.Employee;
import ru.t2.awardservice.exception.FileProcessingException;
import ru.t2.awardservice.exception.IncorrectFileDataException;
import ru.t2.awardservice.processor.MultipartFileProcessor;
import ru.t2.awardservice.service.EmployeeService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AwardProcessor implements MultipartFileProcessor<Award> {

    private final EmployeeService employeeService;

    protected static final int EMPLOYEE_ID_INDEX = 0;
    protected static final int EMPLOYEE_FULL_NAME_INDEX = 1;
    protected static final int AWARD_ID_INDEX = 2;
    protected static final int AWARD_NAME_INDEX = 3;
    protected static final int AWARD_DATE_INDEX = 4;

    protected abstract List<Award> parseFile(MultipartFile file) throws IOException, NumberFormatException, DateTimeParseException;

    @Override
    public List<Award> process(MultipartFile file) {
        try {
            return parseFile(file);
        } catch (IOException e) {
            log.error("Exception occurred, trying to process awards file");
            throw new FileProcessingException(e.getMessage());
        } catch (NumberFormatException | DateTimeParseException e) {
            log.error("Could not format data from file");
            throw new IncorrectFileDataException(e.getMessage());
        }
    }

    protected Optional<Award> getAward(long employeeId, String employeeFullName, long awardId, String awardName,
                                       LocalDate awardDate) {
        if (!employeeService.exists(employeeId)) {
            log.info("Skipping an award process for employee with id {}, who does not exist in DB", employeeId);
            return Optional.empty();
        }

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setFullName(employeeFullName);
        Award award = new Award();
        award.setId(awardId);
        award.setEmployee(employee);
        award.setName(awardName);
        award.setDate(awardDate);

        return Optional.of(award);
    }

    protected boolean isEmpty(Object record) {
        return switch (record) {
            case Row row -> isRowEmpty(row);
            case CSVRecord csvRecord -> isRecordEmpty(csvRecord);
            default -> true;
        };
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private boolean isRecordEmpty(CSVRecord record) {
        if (record == null || record.size() == 0) {
            return true;
        }
        for (String value : record) {
            if (value != null && !value.isBlank()) {
                return false;
            }
        }
        return true;
    }
}
