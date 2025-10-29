package com.example.learningtracker.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.learningtracker.controller.form.UserForm;
import com.example.learningtracker.entity.User;
import com.example.learningtracker.repository.UserRepository;
import com.example.learningtracker.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.learningtracker.config.LoginUserDetailService;


@Controller
public class UserController {

    @Autowired
    private LoginUserDetailService loginUserDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public UserForm userForm() {
        return new UserForm();
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            model.addAttribute("message", "ログインしています");
            return "home";
        } else {
            HttpSession session = req.getSession(false);
            if (session != null) {
                SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                if (savedRequest != null) {
                    // 認証成功後のリダイレクト先
                    String redirectUrl = savedRequest.getRedirectUrl();
                    System.out.println(redirectUrl);
                }
            }
            return "user/login";
        }
    }

    @GetMapping("/signup")
    public String newSignup(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            model.addAttribute("message", "ログインしています");
            return "home";
        } else {
            return "user/signup";
        }
    }

    @PostMapping("/signup")
    String signup(@Validated UserForm userForm, BindingResult result, Model model) {

        LocalDateTime now = LocalDateTime.now();
        User user = new User();

        user.setAccountId(userForm.getAccountId());
        user.setName(userForm.getName());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        if (loginUserDetailService.isExistUser(userForm.getAccountId()) && result.hasErrors()) {
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            model.addAttribute("accountIdError", "ユーザー名 " + userForm.getAccountId() + "は既に登録されています");
            return "user/signup";
        } else if (loginUserDetailService.isExistUser(userForm.getAccountId())) {
            model.addAttribute("userForm", userForm);
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            model.addAttribute("accountIdError", "ユーザー名 " + userForm.getAccountId() + "は既に登録されています");
            return "user/signup";
        } else if (result.hasErrors()) {
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            return "user/signup";
        }

        try {
            userService.create(user, userForm.getPassword());
        } catch (DataAccessException e) {
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            return "user/signup";
        }

        model.addAttribute("signupSuccess", "ユーザー登録が完了しました");
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);

            // ログアウト処理が完了したらトップページへ
            redirectAttributes.addFlashAttribute("message", "ログアウトしました");
            return "redirect:/";
        }
        return "home";
    }
}
