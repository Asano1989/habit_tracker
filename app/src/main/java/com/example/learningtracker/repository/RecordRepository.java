package com.example.learningtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learningtracker.entity.Record;
import java.time.LocalDate;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findByLearningSubjectIdOrderByIdAsc(Integer user_id);
    List<Record> findByLearningSubject_User_IdOrderByIdDesc(Integer userId);
    List<Record> findByLearningSubject_User_IdOrderByCreatedAtDesc(Integer userId);
    List<Record> findByDateAndLearningSubject_User_Id(LocalDate date, Integer userId);
    List<Record> findByIsPublished(boolean isPublished);
}
