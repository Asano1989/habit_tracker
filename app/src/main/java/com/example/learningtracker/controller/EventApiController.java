package com.example.learningtracker.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.learningtracker.config.LoginUserDetails;
import com.example.learningtracker.entity.FullCalendarEventDto;
import com.example.learningtracker.entity.Record;
import com.example.learningtracker.service.RecordService;

@RestController
public class EventApiController {

    private final RecordService recordService;

    public EventApiController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/api/daily-records")
    public List<FullCalendarEventDto> getDailyRecords(@AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Record> records = recordService.findAllRecordsByUser(loginUser);
        Map<LocalDate, Long> recordsByDate = records.stream()
            .collect(Collectors.groupingBy(Record::getDate, Collectors.counting()));
        return recordsByDate.entrySet().stream().map(entry -> {
            LocalDate date = entry.getKey();
            Long count = entry.getValue();
            
            String title = count + "件の記録";
            
            // 遷移先URL: /record/{date}
            String url = "/record/" + date.toString();
            
            return new FullCalendarEventDto(
                "event-" + date.toString(),
                title,
                date.toString(),
                url
            );
        }).collect(Collectors.toList());
    }
}
