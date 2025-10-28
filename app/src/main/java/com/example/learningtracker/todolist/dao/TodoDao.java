package com.example.learningtracker.todolist.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.example.learningtracker.todolist.entity.Todo;
import com.example.learningtracker.todolist.form.TodoQuery;

public interface TodoDao {
  // JPQLによる検索
  List<Todo> findByJPQL(TodoQuery todoQuery);
  Page<Todo> findByJPQL(TodoQuery todoQuery, Pageable pageable);
}