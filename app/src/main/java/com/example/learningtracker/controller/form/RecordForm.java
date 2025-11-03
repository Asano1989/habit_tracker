package com.example.learningtracker.controller.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.learningtracker.entity.LearningSubject;
import com.example.learningtracker.entity.User;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecordForm implements Serializable {
    private Integer id;
    private Integer learningSubjectId;
    
    private LocalDate date;

    
    private LocalTime startTime;

    
    private LocalTime stopTime;

    
    private LocalTime sumTime;

	// @Pattern(regexp="\\d{0,2}", message="ポモドーロ数は2桁以内の半角数字で入力してください")
    private Integer pomodoro;

    
    private boolean usesPomodoro;

    @Size(max = 5000, message="メモは5000字以内にしてください")
    private String memo;

    
    private LocalTime breakTime;

    
    private boolean isPublished;


    private LearningSubject lSubject;

    public LearningSubject getLearningSubject() {
        return lSubject;
    }

    public void setLearningSubject(LearningSubject lSubject) {
        this.lSubject = lSubject;
    }

    public boolean getUsesPomodoro() {
        return usesPomodoro;
    }

    public void setUsesPomodoro(boolean usesPomodoro) {
        this.usesPomodoro = usesPomodoro;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

}
