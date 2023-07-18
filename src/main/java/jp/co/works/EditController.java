package jp.co.works;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.works.entity.Duty;
import jp.co.works.form.UpdateForm;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.EmployeeRepository;

@Controller

public class EditController extends AccountController {
	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	/*
	 * @param workingHours 出勤から退勤までの就業時間
	 * @param brakTime 休憩時間
	 * @param workTime 就業時間から休憩時間を引いた労働時間
	 * 
	 */
	@RequestMapping(path = "/edit", method = RequestMethod.GET)
	public String showEditPage(Model model) {
		Integer userId = getLoginUser(); //ログイン中のユーザーIDを取得
		List<Duty> dutyList = dutyRepository.findByUserId(userId);
		model.addAttribute("dutyList", dutyList);
		
		int totalWorkingDays = GetTotalWorkingDays(dutyList); //総労働日数を取得
	    model.addAttribute("totalWorkingDays", totalWorkingDays);
	    	    
	    List<PeriodOption> periodOptions = GetPeriodOption();
	    model.addAttribute("periodOptions", periodOptions);
	    
		return "edit";
	}

	@RequestMapping(path = "/edit/update", method = RequestMethod.POST)
	public String showupdatePage(Model model
			//, UpdateForm form
			) {
		Integer userId = getLoginUser();
		List<Duty> dutyList = dutyRepository.findByUserId(userId);
		List<UpdateForm> formList = new ArrayList<UpdateForm>();
		for (Duty duty : dutyList) {
			UpdateForm updateform1 = new UpdateForm();
			updateform1.setStarttime(duty.getStartTime());
			updateform1.setEndtime(duty.getEndTime());
			updateform1.setBreaktime(duty.getBreakTime());
			formList.add(updateform1);
	}
		model.addAttribute("formList", formList);
	//	model.addAttribute("dutyList", dutyList);
		
		return "update";
	}

	
	/*
	 * GetTotalWorkingDaysメソッド
	 * start_timeがnullでない場合総労働日数に1足して表示する。
	 * 
	 * @param totalWorkingDays 総労働日数
	 * @return totalWorkingDays
	 */
	private int GetTotalWorkingDays(List<Duty> dutyList) {
		int totalWorkingDays = 0;
		
		for (Duty duty : dutyList) {
			if (duty.getStartTime() != null) {
				totalWorkingDays++;
			}
		}
		return totalWorkingDays;
	}
	
	/*
	 * 2022年12月16日からのドロップリスト生成
	 */
	private List<PeriodOption> GetPeriodOption() {
		List<PeriodOption> periodOptions = new ArrayList<>();
		
		LocalDate startDate = LocalDate.of(2022, 12, 16);
		LocalDate endDate = startDate.plusMonths(1).minusDays(1);
	
	while (startDate.getYear() <= 2023) {
		String optionLabel = startDate.format(DateTimeFormatter.ofPattern("yyyy年M月d日"))
				+ "～" + endDate.format(DateTimeFormatter.ofPattern("yyyy年/M月/d日"));
		String optionValue = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
				+ "～" + endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		periodOptions.add(new PeriodOption(optionLabel, optionValue));
		
		startDate = endDate.plusDays(1);
		endDate = startDate.plusMonths(1).minusDays(1);	
	}
	
	return periodOptions;
	
	}
}