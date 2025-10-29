package com.example.learningtracker.controller.form;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningSubjectForm {
	@Size(max = 255)
	@Pattern(regexp="^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$", message="項目名は漢字、ひらがな、またはカタカナで入力してください。")
  private String name;
}
