package com.example.learningtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.example.learningtracker.config.LoginUserDetails;
import com.example.learningtracker.service.RecordService;
import com.example.learningtracker.entity.Record;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class HomeController {
    @Autowired
    private RecordService recordService;

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal LoginUserDetails loginUser, @ModelAttribute("message") String message, ModelAndView mv) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            List<Record> recordList = recordService.findAllRecordsByUser(loginUser);
            mv.addObject("recordList", recordList);
            mv.addObject("userName", loginUser.getUser().getName());
            mv.addObject("isLogin", "ログインしています");

            mv.setViewName("user/userHome");
            return mv;
        } else {
            mv.addObject("lsLogin", message);
            mv.setViewName("home");
            return mv;
        }
    }

}
