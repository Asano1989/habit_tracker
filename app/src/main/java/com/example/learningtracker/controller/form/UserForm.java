package com.example.learningtracker.controller.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UserForm implements Serializable {
    @NotBlank(message = "アカウント名を入力してください")
    @Size(min = 5, max = 50, message = "アカウント名は5文字以上、50文字以内で入力してください")
    @Pattern(regexp = "^[ -~]+$", message = "アカウント名は半角英数字と記号で入力してください")
    private String accountId;

    @NotBlank(message = "パスワードを入力してください")
    @Size(max = 255, message = "パスワードは255文字以内で入力してください")
    private String password;

    @NotBlank(message = "名前を入力してください")
    @Size(min = 1, max = 50, message = "名前は50文字以内で入力してください")
    @Pattern(regexp="[a-zA-Z0-9亜-熙ぁ-んァ-ヶ]+", message="名前はひらがな、カタカナ、漢字、半角英数字で入力してください")
    private String name;
}
