package com.example.learningtracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.example.learningtracker.entity.LearningSubject;
import com.example.learningtracker.controller.form.LearningSubjectForm;
import com.example.learningtracker.repository.LearningSubjectRepository;
import com.example.learningtracker.config.LoginUserDetails;

@Service
public class LearningSubjectService {

		@Autowired
		private LearningSubjectRepository learningSubjectRepository;
		
		public void setLearningSubject(LearningSubjectForm form, @AuthenticationPrincipal LoginUserDetails loginUser) {
				LearningSubject subject = new LearningSubject();
				subject.setUserId(loginUser.getUser().getId());
				subject.setName(form.getName());
				learningSubjectRepository.save(subject);
		}
		
		public List<LearningSubject> findAllLSubjects(){
			return learningSubjectRepository.findAll();
		}

		public List<LearningSubject> findAllByUserId(@AuthenticationPrincipal LoginUserDetails loginUser){
			return learningSubjectRepository.findByUserId(loginUser.getId());
		}
}