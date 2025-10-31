package com.example.learningtracker.service;

import java.time.LocalDateTime;
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

		public void setLSubject(LearningSubjectForm form, @AuthenticationPrincipal LoginUserDetails loginUser) {
				LocalDateTime now = LocalDateTime.now();
				LearningSubject subject = new LearningSubject();

				if (loginUser.getUser().getId() == form.getUserId()) {
						subject.setId(form.getId());
						subject.setName(form.getName());
						subject.setUser(loginUser.getUser());
						subject.setUserId(form.getUserId());
						subject.setDescription(form.getDescription());
						subject.setCreatedAt(now);
						subject.setUpdatedAt(now);
						
						learningSubjectRepository.save(subject);
				}
		}

		public void update(LearningSubjectForm form, @AuthenticationPrincipal LoginUserDetails loginUser) {
				LearningSubject subject = new LearningSubject();

				if (loginUser.getUser().getId() == form.getUserId()) {
						subject.setId(form.getId());
						subject.setName(form.getName());
						subject.setUser(loginUser.getUser());
						subject.setUserId(form.getUserId());
						subject.setDescription(form.getDescription());
						subject.setUpdatedAt(LocalDateTime.now());
						
						learningSubjectRepository.save(subject);
				}
		}
		
		public List<LearningSubject> findAllLSubjects(){
			return learningSubjectRepository.findAll();
		}

		public List<LearningSubject> findAllByUserId(@AuthenticationPrincipal LoginUserDetails loginUser){
			return learningSubjectRepository.findByUserIdOrderByIdAsc(loginUser.getId());
		}
}