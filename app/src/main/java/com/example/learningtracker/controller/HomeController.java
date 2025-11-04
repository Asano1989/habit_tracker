package com.example.learningtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home(Model model, @ModelAttribute("message") String message, ModelAndView mv) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            mv.addObject("message", "ログインしています");
            mv.addObject("loggedin", true);

            mv.setViewName("home");
            return mv;
        } else {
            mv.addObject("message", message);
            mv.setViewName("home");
            return mv;
        }
    }

}
