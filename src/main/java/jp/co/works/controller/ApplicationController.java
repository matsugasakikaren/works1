package jp.co.works.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.works.entity.EmployeeHoliday;
import jp.co.works.entity.Holiday;
import jp.co.works.repository.EmployeeHolidayRepository;
import jp.co.works.repository.EmployeeRepository;
import jp.co.works.repository.HolidayRepository;

@Controller
public class ApplicationController extends AccountController {
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeHolidayRepository employeeHolidayRepository;

	@GetMapping(path = "/application")
	public String showApplicationForm(Model model) {
		List<String> holidaystartDates = selectedHolidayDates();
		//Integer userId = getLoginUser();
		 // ログイン中のユーザーの休暇情報を取得
        List<EmployeeHoliday> employeeHolidays = getHolidaysForLoggedInUser();

		model.addAttribute("holidays", employeeHolidays);
		model.addAttribute("holidayDates", holidaystartDates);

		return "application";
	}

	@PostMapping(path = "/application/save")
	public String apply(@RequestParam("startDate") String selectedStartDate,
			@RequestParam("endDate") String selectedEndDate,
			@RequestParam("holidayName") String holidayName,
			Model model) {
		//日付文字列を "yyyy/MM/dd" から "yyyy-MM-dd" に変換
		String formattedStartDate = selectedStartDate.replace("/", "-");
		String formattedEndDate = selectedEndDate.replace("/", "-");
		//日付に変換
		Date holidayStart = Date.valueOf(formattedStartDate);
		Date holidayEnd = Date.valueOf(formattedEndDate);
		
		Integer userId = getLoginUser();

		//現在日を取得
		LocalDate today = LocalDate.now();

		//新しいHolidayを作成し保存
		Holiday newHoliday = new Holiday();
		newHoliday.setHolidayName(holidayName);
		newHoliday.setHolidayStart(holidayStart);
		newHoliday.setHolidayEnd(holidayEnd);
		newHoliday.setRequestDate(today);
		holidayRepository.save(newHoliday);
		// decisionIdを設定
		newHoliday.setDecisionId(2); 
		
		//新しいEmployeeHolidayを作成しユーザーと休暇情報を関連付けて保存
		Integer newHolidayId = newHoliday.getHolidayId();
		EmployeeHoliday newEmployeeHoliday = new EmployeeHoliday();
		newEmployeeHoliday.setUserId(userId);
		newEmployeeHoliday.setHoliday(newHoliday); // 休暇情報を設定
		// 新しいEmployeeHolidayを保存
		employeeHolidayRepository.save(newEmployeeHoliday);
		//申請後に最新の休暇情報を取得して表示
		List<String> holidaystartDates = selectedHolidayDates();
		// ログイン中のユーザーの休暇情報を取得
        List<EmployeeHoliday> employeeHolidays = getHolidaysForLoggedInUser();
		model.addAttribute("holidays", employeeHolidays);
		model.addAttribute("holidayDates", holidaystartDates);

		return "application";
	}

	//休暇期間の指定(１か月前～２か月先まで)
	public List<String> selectedHolidayDates() {
		LocalDate today = LocalDate.now();
		LocalDate oneMonthBefore = today.minusMonths(1);
		LocalDate oneMonthAfter = today.plusMonths(2);
		List<String> holidayDates = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		while (!oneMonthAfter.isBefore(oneMonthBefore)) {
			holidayDates.add(oneMonthAfter.format(formatter));
			oneMonthAfter = oneMonthAfter.minusDays(1);
		}
		return holidayDates;
	}
	
	 public List<EmployeeHoliday> getHolidaysForLoggedInUser() {
	        Integer userId = getLoginUser();
	        // ログイン中のユーザーのuserIdを使用してemployee_holidayテーブルからデータを取得
	        List<EmployeeHoliday> holidaysForLoggedInUser = employeeHolidayRepository.findByUserId(userId);   
	        return holidaysForLoggedInUser;
	    }
}