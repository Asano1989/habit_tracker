package com.example.learningtracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.example.learningtracker.entity.LearningSubject;
import com.example.learningtracker.entity.Record;
import com.example.learningtracker.form.LearningSubjectForm;
import com.example.learningtracker.form.RecordForm;
import com.example.learningtracker.repository.LearningSubjectRepository;
import com.example.learningtracker.repository.RecordRepository;
import com.example.learningtracker.config.LoginUserDetails;

@Service
public class RecordService {

	@Autowired
	private RecordRepository recordRepository;

    @Autowired
    private LearningSubjectRepository learningSubjectRepository;
		
	public List<Record> findAllRecords(){
        return recordRepository.findAll();
	}

    public List<Record> findAllIsPublished() {
        return recordRepository.findByIsPublished(true);
    }

	public List<Record> findRecordsByLSubject(LearningSubject lSubject){
        return recordRepository.findByLearningSubjectIdOrderByIdAsc(lSubject.getId());
	}

    public List<Record> findAllRecordsByUser(LoginUserDetails loginUser) {
        Integer userId = loginUser.getUser().getId();
        List<Record> userRecords = recordRepository.findByLearningSubject_User_Id(userId);
        return userRecords;
    }

    public void update(RecordForm form, @AuthenticationPrincipal LoginUserDetails loginUser) {
        LearningSubject learningSubject = learningSubjectRepository.findById(form.getLearningSubjectId())
        .orElseThrow(() -> new RuntimeException("LearningSubject not found"));
		
		if (loginUser.getUser().getId().equals(learningSubject.getUserId())) {
            Record record = new Record();
            record.setId(form.getId());
            record.setLearningSubjectId(form.getLearningSubjectId());
            record.setDate(form.getDate());
            record.setStartTime(form.getStartTime());
            record.setStopTime(form.getStopTime());
            record.setBreakTime(form.getBreakTime());
            record.setSumTime(form.getSumTime());
            record.setPomodoro(form.getPomodoro());
            record.setUsesPomodoro(form.getUsesPomodoro());
            record.setMemo(form.getMemo());
            record.setIsPublished(form.getIsPublished());
            record.setUpdatedAt(LocalDateTime.now());
            
            recordRepository.save(record);
        } 
    }

}
