package jp.co.works.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import jp.co.works.form.LoginForm;

public class UserIdValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginForm form = (LoginForm) target;
		String userId = form.getUserId();

		if (userId == null || !userId.matches("^[0-9]+$")) {
            errors.rejectValue("userId", "invalid.userId", "社員IDは半角数字で入力してください。");
        }
    }
}