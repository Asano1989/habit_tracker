package com.example.learningtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learningtracker.entity.LearningSubject;

public interface LearningSubjectRepository extends JpaRepository<LearningSubject, Integer> {
    List<LearningSubject> findByUserId(Integer user_id);
    List<LearningSubject> findByUserIdOrderByIdAsc(Integer user_id);
}
