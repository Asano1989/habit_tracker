package com.example.learningtracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.example.learningtracker.entity.LearningSubject;
import com.example.learningtracker.entity.Record;
import com.example.learningtracker.controller.form.RecordForm;
import com.example.learningtracker.repository.RecordRepository;
import com.example.learningtracker.config.LoginUserDetails;

@Service
public class RecordService {

		@Autowired
		private RecordRepository recordRepository;
		
		public List<Record> findAllLSubjects(){
			  return recordRepository.findAll();
		}

		public List<Record> findRecordsByLSubject(LearningSubject lSubject){
			  return recordRepository.findByLearningSubjectIdOrderByIdAsc(lSubject.getId());
		}

    public List<Record> findAllRecordsByUser(LoginUserDetails loginUser) {
        Integer userId = loginUser.getUser().getId();
        List<Record> userRecords = recordRepository.findByLearningSubject_User_Id(userId);
        return userRecords;
    }
}
