package com.example.learningtracker.controller.form;


import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UserForm implements Serializable {
    @NotBlank(message = "アカウント名を入力してください")
    @Size(max = 100, message = "アカウント名は100文字以内で入力してください")
    @Pattern(regexp = "^[ -~]+$", message = "アカウント名は半角英数字と記号で入力してください")
    private String accountId;

    @NotBlank(message = "パスワードを入力してください")
    @Size(max = 255, message = "パスワードは255文字以内で入力してください")
    private String password;

    @NotBlank(message = "名前を入力してください")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    @Pattern(regexp="[^\\s|　]+", message="名前に空白文字を含めることはできません")
    private String name;

    /* 
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    */
}
