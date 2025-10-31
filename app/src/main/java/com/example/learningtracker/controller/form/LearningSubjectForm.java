package com.example.learningtracker.controller.form;

import java.io.Serializable;

import com.example.learningtracker.entity.User;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LearningSubjectForm implements Serializable {
    private Integer id;
    private Integer userId;

	@Size(max = 255, message = "項目名は255文字以内で入力してください")
    @Pattern(regexp="[a-zA-Z0-9亜-熙ぁ-んァ-ヶ+&@#/%?=~_|!:,.;]+", message="項目名はひらがな、カタカナ、漢字、半角の英数字と記号で入力してください")
	private String name;

    @Size(max = 255, message = "説明は500文字以内で入力してください")
    @Pattern(regexp="[a-zA-Z0-9亜-熙ぁ-んァ-ヶ+&@#/%?=~_|!:,.;]+", message="説明はひらがな、カタカナ、漢字、半角の英数字と記号で入力してください")
    private String description;
    
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
