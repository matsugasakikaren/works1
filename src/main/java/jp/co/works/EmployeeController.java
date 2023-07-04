package jp.co.works;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.works.entity.Employee;
import jp.co.works.form.LoginForm;
import jp.co.works.repository.EmployeeRepository;

@Controller
public class EmployeeController {
	@Autowired
	private EmployeeRepository userRepository; //EmployeeRepositoryインターフェース実装クラスのオブジェクト生成

	@RequestMapping("/login") //リクエスト受け取り \loginメソッド実行 login.htmlを表示
	public String login(@ModelAttribute LoginForm form) {
		return "login";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST) //loginにアクセスしpostリクエストを送信した場合下記のメソッドを実行
	public String dologin(@Validated LoginForm form, BindingResult result, HttpSession session, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("errMessage",
					"社員ID、またはパスワードが間違っています。");
			return "login";
		}

		Integer userId = null;
		if (form.getUserId() != null) {
			userId = Integer.parseInt(form.getUserId());
		}
		//Integer userId = Integer.parseInt(form.getUserId());
		String password = form.getPassword();
		Employee user = userRepository.findByUserIdAndPassword(userId, password);

		if (user != null) {
			session.setAttribute("users", user);
			return "redirect:/employment";

		} else {
			model.addAttribute("errMessage", "社員ID、またはパスワードが間違っています。");
			return "login";
		}
	}

	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(path = "/employment")
    public String showEmploymentPage() {
        return "employment";
	}
	
	@RequestMapping(path = "/edit")
	public String edit() {
		return "/edit";
	}
	
	@RequestMapping(path = "/app")
	public String app() {
		return "/app";
	}
	

}