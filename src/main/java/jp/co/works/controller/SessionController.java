package jp.co.works.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.works.entity.Employee;
import jp.co.works.form.LoginForm;
import jp.co.works.repository.EmployeeRepository;

@Controller
public class SessionController {
	@Autowired
	private EmployeeRepository userRepository; //EmployeeRepositoryインターフェース実装クラスのオブジェクト生成

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserIdValidator());
	}

	@RequestMapping("/login")
	public String login(@ModelAttribute LoginForm form) {
		return "login";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST) //loginにアクセスしpostリクエストを送信した場合下記のメソッドを実行
	public String dologin(@Validated LoginForm form, BindingResult result, HttpSession session, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("errMessage",
					"社員IDもしくはパスワードが間違っています。");
			return "login";
		}

		Integer userId = null;
		if (form.getUserId() != null) {
			userId = Integer.parseInt(form.getUserId());
		}

		String password = form.getPassword();
		Employee user = userRepository.findByUserIdAndPassword(userId, password);

		if (user != null && user.getUserId() != null) {
			session.setAttribute("users", user);
			return "redirect:/employment";
		} else {
			if (userRepository.existsById(userId)) {
				model.addAttribute("errMessage",
						"社員IDもしくはパスワードが間違っています。");
			} else {
				model.addAttribute("errMessage", "アカウントが存在しません。管理者にお問い合わせください。");
			}
			return "login";
		}
	}


	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}