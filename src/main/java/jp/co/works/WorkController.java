package jp.co.works;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.works.entity.Duty;
import jp.co.works.entity.Employee;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.WorkRepository;

@Controller
public class WorkController {
	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private WorkRepository workRepository;

	@Autowired
	private HttpSession session;

	/*
	 * showEmploymentPageメソッド
	 * 出退勤画面を表示
	 * 
	 * @param workName 業務内容
	 * @param model workRepository.findAllメソッドで取得した情報をworkListという名前で追加
	 * @return employment
	 * 
	 */
	@RequestMapping(path = "/employment")
	public String showEmploymentPage(String workName, Model model) {

		model.addAttribute("workList", workRepository.findAll());
		return "employment";
	}
	
	private Time convertToTime(String timeString) {
	    LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
	    return Time.valueOf(localTime);
	}


	/*
	 * startWorkメソッド
	 * 出勤ボタンを押した後の出退勤画面を表示
	 * 
	 * @return employment
	 * 
	 */
	@RequestMapping(path = "/employment/startWork", method = RequestMethod.POST)
	public String startWork() {
		
		Integer startLogin = getLoginUser();
		
		Duty duty = new Duty();
		String starttime = abc();
		Time startTime = convertToTime(starttime);
		duty.setStartTime(startTime);
		duty.setUserId(startLogin);
		dutyRepository.save(duty);

		return "employment";
	}

	/*
	 * endWorkメソッド
	 * 退勤ボタンを押した後の出退勤画面を表示
	 * 
	 * @param model endTimeという名前で退勤時刻を追加
	 * @return employment
	 * 
	 */
	@RequestMapping(path = "/employment/endWork", method = RequestMethod.GET)
	public String endWork(Model model) {
		
		Integer startLogin = getLoginUser();
		String endTime = abc();

		
		Duty duty = new Duty();

		dutyRepository.save(duty);
		//modelに追加
		model.addAttribute("endTime", endTime);
		return "employment";
	}

	/*
	 * abcメソッド
	 * 
	 * @return startTime 時刻を取得 
	 */
	private String abc() {
		LocalTime time1 = LocalTime.now();
		DateTimeFormatter dtformat1 = DateTimeFormatter.ofPattern("HH:mm:ss");
		String startTime = dtformat1.format(time1);
		return startTime;
	}
	
	
	


	//ログインユーザーの情報を取得
	private Integer getLoginUser() {
		Employee logInUser = (Employee) session.getAttribute("users");
		Integer userId = logInUser.getUserId();
		return userId;
	}

}
