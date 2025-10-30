package com.example.learningtracker.controller.form;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningSubjectForm {
    private int userId;

	@Size(max = 255, message = "項目名は255文字以内で入力してください")
	private String name;

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
