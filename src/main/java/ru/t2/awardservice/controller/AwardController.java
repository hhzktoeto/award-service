package ru.t2.awardservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.t2.awardservice.dto.RequestResponse;
import ru.t2.awardservice.entity.Award;
import ru.t2.awardservice.service.AwardService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/awards")
public class AwardController {

    private final AwardService awardService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RequestResponse upload(@RequestParam("file") MultipartFile file) {
        List<Award> uploadedAwards = awardService.upload(file);

        return RequestResponse.builder()
                .message("%d award(-s) were processed and uploaded".formatted(uploadedAwards.size()))
                .build();
    }
}
