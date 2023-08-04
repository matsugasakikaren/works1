package jp.co.works.controller;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.works.UpdateFormParam;
import jp.co.works.entity.Duty;
import jp.co.works.form.UpdateForm;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.EmployeeRepository;
import jp.co.works.service.DutyService;
import jp.co.works.utils.DateRange;

@Controller
public class EditController extends AccountController {
	private final DutyRepository dutyRepository;
	private final EmployeeRepository employeeRepository;
	private final jp.co.works.service.DutyService dutyService;

	//private static final Logger logger = LoggerFactory.getLogger(EditController.class);

	@Autowired
	public EditController(DutyRepository dutyRepository, EmployeeRepository employeeRepository,
			DutyService dutyService) {
		this.dutyRepository = dutyRepository;
		this.employeeRepository = employeeRepository;
		this.dutyService = dutyService;
	}

	/* @param Model model
	* @param workingHours 出勤から退勤までの就業時間
	* @param brakTime 休憩時間
	* @param workTime 就業時間から休憩時間を引いた労働時間
	*
	*/
	@RequestMapping(path = "/edit", method = RequestMethod.GET)
	public String showEditPage(Model model, @RequestParam(required = false) String selectedPeriod) {

		//ログイン中のユーザーIDを取得
		Integer userId = getLoginUser();
		List<Duty> dutyList = dutyRepository.findByUserId(userId);
		model.addAttribute("dutyList", dutyList);

		// ドロップダウンリストの選択肢を生成
        List<String> periodOptions = DateRange.generateDateRanges();
        model.addAttribute("periodOptions", periodOptions);
        //model.addAttribute("selectedPeriod", selectedPeriod);
	    
		// 選択された期間の日付リストを取得
		if (selectedPeriod != null) {
			List<LocalDate> selectedDateList = DateRange.getSelectedDateList(selectedPeriod);
			model.addAttribute("selecteDateList", selectedDateList);
		}
		//期間リストを格納
		List<LocalDate> currentDateRangeList = DateRange.getCurrentDateRange();

		//1日ずつリストを格納
		List<LocalDate> selectedLocalDateList = DateRange.getSelectedDateList(selectedPeriod);

		//Mapを定義
		Map<LocalDate, Duty> dutyMap = new HashMap<>();

		//currentDateRangeListの日付を１日ずつdutyMapに追加
		for (LocalDate date : currentDateRangeList) {
			dutyMap.put(date, null);

			//dutyテーブルから日付を取得
			Duty dutyRecord = dutyRepository.findByWorkDate(date);

			//合致するレコードがある場合dutyMapに日付とDutyオブジェクトを追加
			if (dutyRecord != null) {
				Duty dutyEntry = new Duty();
				dutyEntry.setWorkDate(date);
				dutyEntry.setStartTime(dutyRecord.getStartTime());
				dutyEntry.setEndTime(dutyRecord.getEndTime());
				dutyEntry.setBreakTime(dutyRecord.getBreakTime());
				dutyEntry.setOverTime(dutyRecord.getOverTime());
				dutyMap.put(date, dutyEntry);
			} else {
				//存在しない場合は日付のみをdutyMapに追加
				Duty dutyEntry = new Duty();
				dutyEntry.setWorkDate(date);
				dutyMap.putIfAbsent(date, dutyEntry);
			}
		}
		
		//dutyMapをmodelに渡す。
		model.addAttribute("dutyMap", dutyMap);

		//総労働日数を取得
		int totalWorkingDays = GetTotalWorkingDays(dutyList);
		model.addAttribute("totalWorkingDays", totalWorkingDays);

		//総休憩時間を取得
		String totalBreakTime = TotalBreakTime(dutyList);
		model.addAttribute("totalBreakTime", totalBreakTime);

		List<UpdateForm> updatedFormList = (List<UpdateForm>) model.asMap().get("updatedFormList");
		if (updatedFormList != null) {
			model.addAttribute("formList", updatedFormList);
		}
		return "edit";
	}

	@RequestMapping(path = "/edit/update", method = RequestMethod.POST)
	public String showupdatePage(Model model
	//, @ModelAttribute("updateForm") List<UpdateForm> updateForm
	) {
		Integer userId = getLoginUser();
		List<Duty> dutyList = dutyRepository.findByUserId(userId);

		List<UpdateForm> formList = new ArrayList<UpdateForm>();
		for (Duty duty : dutyList) {
			UpdateForm updateform1 = new UpdateForm();
			updateform1.setWorkDate(duty.getWorkDate());
			updateform1.setStartTime(duty.getStartTime());
			updateform1.setEndTime(duty.getEndTime());
			updateform1.setBreakTime(duty.getBreakTime());
			updateform1.setOverTime(duty.getOverTime());
			formList.add(updateform1);

			//      logger.info("showupdatePage: Added to formList: {}", updateform1.toString());
		}
		model.addAttribute("formList", formList);
		//    model.addAttribute("dutyList", dutyList);
		//logger.info("showupdatePage: formList size = {}", formList.size()); // ログを追加

		return "update";
	}

	@PostMapping(path = "/edit/update/save")
	public String updateDuty(@ModelAttribute UpdateFormParam formParam) {
		dutyService.updateAll(formParam.getFormList());
		List<UpdateForm> formList = formParam.getFormList();
		for (UpdateForm updateForm : formList) {
			dutyService.updateAll(formList);

			//	logger.info("updateDuty: Updated data: {}", updateForm.toString());
		}
		//logger.info("updateDuty: formList size = {}", formList.size()); // ログを追加
		return "redirect:/edit";
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
	 * totalBreakTimeメソッド    
	 * 総休憩時間を求める
	 */
	private String TotalBreakTime(List<Duty> dutyList) {
		long totalBreaktime = dutyList.stream()
				.mapToLong(duty -> {
					Time breakTime = duty.getBreakTime() != null ? duty.getBreakTime() : Time.valueOf("00:00:00");
					return (breakTime.getHours() * 60) + breakTime.getMinutes();
				})
				.sum();

		// 総休憩時間を時間と分の組み合わせに変換
		int totalBreakHours = (int) totalBreaktime / 60;
		int totalBreakMinutesPart = (int) totalBreaktime % 60;
		return String.format("%02d:%02d", totalBreakHours, totalBreakMinutesPart);
	}
}