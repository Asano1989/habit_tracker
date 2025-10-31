package com.example.learningtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.example.learningtracker.entity.Record;
import com.example.learningtracker.repository.RecordRepository;
import com.example.learningtracker.service.RecordService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class HomeController {
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordService recordService;

    @GetMapping("/")
    public ModelAndView home(Model model, @ModelAttribute("message") String message, ModelAndView mav) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            mav.addObject("message", "ログインしています");

            List<Record> recordList = recordService.findAllIsPublished();
            if (recordList.size() > 0) {
                mav.addObject("recordList", recordList);
            }

            mav.setViewName("home");
            return mav;
        } else {
            model.addAttribute("message", message);
            mav.setViewName("home");
            return mav;
        }
    }

}
