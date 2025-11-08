package ru.t2.awardservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.processor.MultipartFileProcessor;
import ru.t2.awardservice.processor.MultipartFileProcessorSupplier;
import ru.t2.awardservice.repository.AwardRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository repository;
    private final MultipartFileProcessorSupplier<Award> processorSupplier;

    public List<Award> upload(MultipartFile file) {
        log.info("Processing saving request for award");
        String fileExtension = FilenameUtils.getExtension(file.getName());
        MultipartFileProcessor<Award> processor = processorSupplier.getProcessor(fileExtension);

        List<Award> processedAwards = processor.process(file);
        List<Award> saved = repository.saveAll(processedAwards);
        log.info("Saving request for award processed");

        return saved;
    }
}
