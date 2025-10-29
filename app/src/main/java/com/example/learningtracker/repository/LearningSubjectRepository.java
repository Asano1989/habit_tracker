package com.example.learningtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learningtracker.entity.User;

public interface LearningSubjectRepository extends JpaRepository<User, Integer> {

}
