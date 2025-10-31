package com.example.learningtracker.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.learningtracker.controller.form.LearningSubjectForm;
import com.example.learningtracker.entity.LearningSubject;
import com.example.learningtracker.repository.LearningSubjectRepository;
import com.example.learningtracker.config.LoginUserDetails;
import com.example.learningtracker.service.LearningSubjectService;

import jakarta.servlet.http.HttpSession;


@Controller
public class LearningSubjectController {
    @Autowired
    private HttpSession session;

    @Autowired
    private LearningSubjectRepository learningSubjectRepository;

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

    @PostMapping("/subject/{id}")
    public ModelAndView learningSubjectById(@PathVariable(name="id") int id, ModelAndView mv) {
        mv.setViewName("record/lSubjectForm");
        LearningSubject lSubject = learningSubjectRepository.findById(id).get();
        mv.addObject("learningSubjectForm", lSubject);
        session.setAttribute("mode", "update");
        return mv;
    }

    @PostMapping("/subject/create/form")
    public ModelAndView createLearningSubject(@AuthenticationPrincipal LoginUserDetails loginUserDetails, ModelAndView mv) {
        mv.setViewName("record/lSubjectForm");
        LearningSubject form = new LearningSubject();
        form.setUser(loginUserDetails.getUser());
        form.setUserId(loginUserDetails.getUser().getId());
        mv.addObject("learningSubjectForm", form);
        session.setAttribute("mode", "create");
        return mv;
    }

    @PostMapping("/subject/create/do")
    public String createLeaningSubject(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                        @ModelAttribute @Validated LearningSubjectForm learningSubjectForm,
                                        BindingResult result, Model model) {
        if (learningSubjectForm.getUserId() == loginUserDetails.getUser().getId()) {
            learningSubjectForm.setUserId(loginUserDetails.getUser().getId());
            model.addAttribute("learningSubjectForm", learningSubjectForm);

            learningSubjectService.setLSubject(learningSubjectForm, loginUserDetails);
            return "redirect:/subject";
        }

        // バリデーションエラーの場合
        if (result.hasErrors()) {
            model.addAttribute("learningSubjectForm", learningSubjectForm);
            return "record/lSubjectForm";
        }

        return "record/lSubjectForm";
    }

    @PostMapping("/subject/update")
    public String update(@AuthenticationPrincipal LoginUserDetails loginUserDetails, @ModelAttribute @Validated LearningSubjectForm learningSubjectForm, BindingResult result, Model model) {
        if (learningSubjectForm.getUserId() == loginUserDetails.getUser().getId()) {
            learningSubjectForm.setUserId(loginUserDetails.getUser().getId());
            model.addAttribute("learningSubjectForm", learningSubjectForm);

            learningSubjectService.update(learningSubjectForm, loginUserDetails);
            return "redirect:/subject";
        }

        // バリデーションエラーの場合
        if (result.hasErrors()) {
            model.addAttribute("learningSubjectForm", learningSubjectForm);
            return "record/lSubjectForm";
        }

        return "record/lSubjectForm";
    }

    @PostMapping("/subject/delete")
    public String deleteLearningSubject(@AuthenticationPrincipal LoginUserDetails loginUserDetails, @ModelAttribute LearningSubjectForm learningSubjectForm) {
        if (loginUserDetails.getUser().getId() == learningSubjectForm.getUserId()) {
            learningSubjectRepository.deleteById(learningSubjectForm.getId());
            return "redirect:/subject";
        }

        return "record/lSubjectForm";
    }

    @PostMapping("/subject/{id}/delete")
    public String deleteLearningSubject(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                        @ModelAttribute LearningSubjectForm learningSubjectForm,
                                        @PathVariable(name="id") int id) {
        Optional<LearningSubject> lSubject = learningSubjectRepository.findById(id);
        LearningSubject subject = lSubject.get();

        if (loginUserDetails.getUser().getId() == subject.getUserId()) {
            learningSubjectRepository.deleteById(subject.getId());
            return "redirect:/subject";
        }

        return "record/lSubjectForm";
    }

    @PostMapping("/subject/cancel")
    public String cancel() {
        return "redirect:/subject";
    }
}
