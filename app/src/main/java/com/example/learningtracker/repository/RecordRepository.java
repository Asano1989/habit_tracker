package com.example.learningtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learningtracker.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Integer> {
}
