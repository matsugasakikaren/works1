package jp.co.works.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpSession;
import jp.co.works.entity.Employee;

@Controller
public class AccountController {
	@Autowired
	protected HttpSession session;

	/*
	 * getLoginUserメソッド
	 * 
	 * @return userId
	 */
	protected Integer getLoginUser() {
		Employee logInUser = (Employee) session.getAttribute("users");
		Integer userId = logInUser.getUserId();
		return userId;
	}
}
