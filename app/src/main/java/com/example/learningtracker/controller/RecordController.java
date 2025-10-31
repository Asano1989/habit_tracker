package com.example.learningtracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.learningtracker.config.LoginUserDetailService;
import com.example.learningtracker.config.LoginUserDetails;
import com.example.learningtracker.controller.form.RecordForm;
import com.example.learningtracker.entity.Record;
import com.example.learningtracker.repository.RecordRepository;
import com.example.learningtracker.service.RecordService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RecordController {

    @Autowired
    private LoginUserDetailService loginUserDetailService;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordService recordService;

    @ModelAttribute
    public RecordForm recordForm() {
        return new RecordForm();
    }

    @GetMapping("/record")
    public String viewRecord(Model model, @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Record> recordList = recordService.findAllRecordsByUser(loginUser);
        model.addAttribute("recordList", recordList);
        
        return "record/records";
    }
}
