package com.example.learningtracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.learningtracker.controller.form.LearningSubjectForm;
import com.example.learningtracker.entity.LearningSubject;
import com.example.learningtracker.config.LoginUserDetails;
import com.example.learningtracker.service.LearningSubjectService;


@Controller
public class LearningSubjectController {

    @Autowired
    private LearningSubjectService learningSubjectService;

    @ModelAttribute
    public LearningSubjectForm learningSubjectForm() {
        return new LearningSubjectForm();
    }

    @GetMapping("/subject")
    public String viewSubject(Model model, @AuthenticationPrincipal LoginUserDetails loginUser) {
      List<LearningSubject> subjectList = learningSubjectService.findAllByUserId(loginUser);
      model.addAttribute("subjectList", subjectList);
      
      return "record/lSubject";
    }
  
}
