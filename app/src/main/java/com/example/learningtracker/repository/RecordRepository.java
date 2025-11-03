package com.example.learningtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learningtracker.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findByLearningSubjectIdOrderByIdAsc(Integer user_id);
    List<Record> findByLearningSubject_User_Id(Integer userId);
    List<Record> findByIsPublished(boolean isPublished);
}
