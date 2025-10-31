package com.example.learningtracker.controller.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecordForm implements Serializable {
    
    private LocalDate date;

    
    private LocalTime startTime;

    
    private LocalTime stopTime;

    
    private LocalTime sumTime;

	@Pattern(regexp="\\d{0,2}", message="ポモドーロ数は2桁以内の半角数字で入力してください")
    private Integer pomodoro;

    
    private boolean usesPomodoro;

    @Size(max = 5000, message="メモは5000字以内にしてください")
    private String memo;

    
    private LocalTime breakTime;

    
    private boolean isPublished;
}
