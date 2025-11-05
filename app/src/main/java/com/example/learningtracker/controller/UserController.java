package com.example.learningtracker.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.learningtracker.entity.User;
import com.example.learningtracker.form.UserForm;
import com.example.learningtracker.entity.Record;
import com.example.learningtracker.repository.UserRepository;
import com.example.learningtracker.service.RecordService;
import com.example.learningtracker.service.UserService;
import com.example.learningtracker.validation.ValidationGroups.Update;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.learningtracker.config.LoginUserDetailService;
import com.example.learningtracker.config.LoginUserDetails;


@Controller
public class UserController {

    @Autowired
    private LoginUserDetailService loginUserDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordService recordService;

    @ModelAttribute
    public UserForm userForm() {
        return new UserForm();
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest req, @AuthenticationPrincipal LoginUserDetails loginUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            List<Record> recordList = recordService.findAllRecordsByUser(loginUser);
            model.addAttribute("recordList", recordList);
            model.addAttribute("message", "ログインしています");
            return "user/userHome";
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
            return "home";
        }
    }

    @GetMapping("/login")
    public String loginView(Model model, HttpServletRequest req, @AuthenticationPrincipal LoginUserDetails loginUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            List<Record> recordList = recordService.findAllRecordsByUser(loginUser);
            model.addAttribute("recordList", recordList);
            model.addAttribute("message", "ログインしています");
            return "user/userHome";
        } else {
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

        String accountIdError = "ユーザー名 " + userForm.getAccountId() + "は既に登録されています";
        String signupFailed = "ユーザー登録に失敗しました";

        LocalDateTime now = LocalDateTime.now();
        User user = new User();

        user.setAccountId(userForm.getAccountId());
        user.setName(userForm.getName());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        if (loginUserDetailService.isExistUser(userForm.getAccountId()) && result.hasErrors()) {
            model.addAttribute("signupError", signupFailed);
            model.addAttribute("accountIdError", accountIdError);
            return "user/signup";
        } else if (loginUserDetailService.isExistUser(userForm.getAccountId())) {
            model.addAttribute("userForm", userForm);
            model.addAttribute("signupError", signupFailed);
            model.addAttribute("accountIdError", accountIdError);
            return "user/signup";
        } else if (result.hasErrors()) {
            model.addAttribute("signupError", signupFailed);
            return "user/signup";
        }

        // パスワード確認用フィールドとの一致をここでチェック
        if (!userForm.getPassword().equals(userForm.getPasswordConfirmation())) {
            // FieldErrorを手動で追加
            result.rejectValue("passwordConfirmation", "error.passwordConfirmation", "パスワードが一致しません");
            model.addAttribute("signupError", signupFailed);
            return "user/signup";
        }

        try {
            userService.create(user, userForm.getPassword());
        } catch (DataAccessException e) {
            model.addAttribute("signupError", signupFailed);
            return "user/signup";
        }

        model.addAttribute("signupSuccess", "ユーザー登録が完了しました");
        return "home";
    }


    @GetMapping("/user")
    ModelAndView showUser(@AuthenticationPrincipal LoginUserDetails loginUser, ModelAndView mv) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            mv.setViewName("user/userEditForm");
            User user = userRepository.findById(loginUser.getUser().getId()).get();
            mv.addObject("userForm", user);
        } else {
            mv.setViewName("home");
        }
        
        return mv;
    }


    @PostMapping("/user")
    String updateUser(@AuthenticationPrincipal LoginUserDetails loginUser, @Validated(Update.class) UserForm userForm, BindingResult result, Model model) {

        String accountIdError = "ユーザー名 " + userForm.getAccountId() + "は既に登録されています";
        String updateFailed = "ユーザー情報更新に失敗しました";

        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.getReferenceById(loginUser.getUser().getId());

        user.setAccountId(userForm.getAccountId());
        user.setName(userForm.getName());
        user.setUpdatedAt(now);

        // ユーザー名重複チェックとバリデーションエラーの判定
        boolean isAccountIdExist = loginUserDetailService.isExistUserWithId(userForm.getAccountId(), loginUser.getUser().getId());

        if (result.hasErrors() || isAccountIdExist) {
            if (isAccountIdExist) {
                model.addAttribute("accountIdError", accountIdError);
            }
            model.addAttribute("updateError", updateFailed);
            return "user/userEditForm";
        }

        // パスワードの更新処理
        // userForm.getPassword() が空文字列の場合 (ユーザーが何も入力しなかった場合)
        if (userForm.getPassword() == null || userForm.getPassword().isEmpty()) {
            // パスワードはそのまま（変更しない）
            try {
                userRepository.save(user);
            } catch (DataAccessException e) {
                model.addAttribute("updateError", updateFailed);
                return "user/userEditForm";
            }
        } else {
            // パスワード確認用フィールドとの一致をここでチェック
            if (!userForm.getPassword().equals(userForm.getPasswordConfirmation())) {
                // FieldErrorを手動で追加
                result.rejectValue("passwordConfirmation", "error.passwordConfirmation", "パスワードが一致しません");
                model.addAttribute("updateError", updateFailed);
                return "user/userEditForm";
            }

            try {
                // パスワードを更新（userServiceでエンコード処理などを実行）
                userService.update(user, userForm.getPassword()); 
            } catch (DataAccessException e) {
                model.addAttribute("updateError", updateFailed);
                return "user/userEditForm";
            }
        }

        List<Record> recordList = recordService.findAllRecordsByUser(loginUser);
        model.addAttribute("recordList", recordList);
        model.addAttribute("updateSuccess", "ユーザー情報の更新が完了しました");
        return "user/userHome";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);

            // ログアウト処理が完了したらトップページへ
            return "redirect:/";
        }
        return "home";
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal LoginUserDetails loginUser, Model model, ModelAndView mav) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            List<Record> recordList = recordService.findAllRecordsByUser(loginUser);
            model.addAttribute("recordList", recordList);

            mav.addObject("message", "ようこそ、" + authentication.getName() + "さん！");
            mav.setViewName("user/userHome");
            return mav;
        } else {
            model.addAttribute("message", "ログインしてください");
            mav.setViewName("home");
            return mav;
        }
    }
}
