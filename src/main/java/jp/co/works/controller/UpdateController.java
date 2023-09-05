package jp.co.works.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.works.UpdateFormParam;
import jp.co.works.entity.Duty;
import jp.co.works.entity.Employee;
import jp.co.works.form.UpdateForm;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.EmployeeRepository;
import jp.co.works.utils.DateRange;

@Controller
public class UpdateController extends AccountController {
	@Autowired
	private DutyRepository dutyRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	/*
	 * 編集画面の初期表示
	 * path=/editから
	 */
	@PostMapping(path = "/edit/update")
	public String updateData(Model model) {
		LocalDate today = LocalDate.now();
		Optional<Employee> employeeOptional = employeeRepository.findById(getLoginUser());
		if (employeeOptional.isPresent()) {
			Employee employee = employeeOptional.get();
			String loggedInUserName = employee.getUserName();
			model.addAttribute("loggedInUserName", loggedInUserName);
		}
		LocalDate startDate = today.withDayOfMonth(16).minusMonths(1);
		LocalDate endDate = today.withDayOfMonth(15);

		prepareUpdateForm(model, startDate, endDate, "update");
		return "update";

	}

	private Duty findDutyForDate(List<Duty> dutyList, LocalDate date) {
		for (Duty duty : dutyList) {
			if (duty.getWorkDate().equals(date)) {
				return duty;
			}
		}
		return null;
	}

	@PostMapping(path = "/edit/updatelist")
	public String updateDatalist(@RequestParam("selectedPeriod") String selectedPeriod, Model model) {
		List<LocalDate> selectedDateList = DateRange.getSelectedDateList(selectedPeriod);
		Optional<Employee> employeeOptional = employeeRepository.findById(getLoginUser());
		if (employeeOptional.isPresent()) {
			Employee employee = employeeOptional.get();
			String loggedInUserName = employee.getUserName();
			model.addAttribute("loggedInUserName", loggedInUserName);
		}
		LocalDate startDate = selectedDateList.get(0);
		LocalDate endDate = selectedDateList.get(selectedDateList.size() - 1);

		// updateFormParam をモデルに追加
		model.addAttribute("updateFormParam", new UpdateFormParam());

		prepareUpdateForm(model, startDate, endDate, "updatelist");

		return "update";
	}

	//更新機能を実装
	@PostMapping(path = "/update/save")
	public String updateWork(@ModelAttribute UpdateFormParam updateFormParam, BindingResult bindingResult,
			@RequestParam("mode") String mode,
			Model model) {
		Integer userId = getLoginUser();
		List<UpdateForm> workStatusList = updateFormParam.getFormList();

		//現在日を取得
		LocalDate today = LocalDate.now();
		//先月16日から当月15日まで
		LocalDate startDate = today.withDayOfMonth(16).minusMonths(1);
		LocalDate endDate = today.withDayOfMonth(15);

		List<LocalDate> dateList = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			dateList.add(currentDate);
			currentDate = currentDate.plusDays(1);
		}

		if ("update".equals(mode)) {
			//更新処理
			if (bindingResult.hasErrors()) {
				List<String> errorMessages = new ArrayList<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			model.addAttribute("errorMessages", "hh:mmの形式で再度入力してください");
			return "update";
		}

		for (UpdateForm updateForm : workStatusList) { // updateFormをworkStatusListに変更
			for (LocalDate workDate : dateList) {
				List<Duty> dutyListForDate = dutyRepository.findByUserIdAndWorkDate(userId, workDate);

				Duty dutyForDate;
				if (!dutyListForDate.isEmpty()) {
					dutyForDate = dutyListForDate.get(0);
				} else {
					dutyForDate = new Duty();
					dutyForDate.setUserId(userId);
					dutyForDate.setWorkDate(workDate);
				}
				// 各日付ごとに UpdateForm のデータを取得
				UpdateForm updateFormData = workStatusList.get(dateList.indexOf(workDate));
				// 各日付ごとのデータを設定
				dutyForDate.setStartTime(updateFormData.getStartTime());
				dutyForDate.setEndTime(updateFormData.getEndTime());
				dutyForDate.setBreakTime(updateFormData.getBreakTime());
				dutyForDate.setOverTime(updateFormData.getOverTime());

				dutyRepository.save(dutyForDate);
			}
		}
		return "redirect:/edit";
	}else if ("updatelist".equals(mode)) {
		//更新処理を行わない
		prepareUpdateForm(model, startDate, endDate, mode);
		return "update";
	}
		return "update";
	}

	private void prepareUpdateForm(Model model, LocalDate startDate, LocalDate endDate, String mode) {
		Integer userId = getLoginUser();
		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDateBetween(userId, startDate, endDate);

		List<LocalDate> dateRangeList = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			dateRangeList.add(currentDate);
			currentDate = currentDate.plusDays(1);
		}

		List<UpdateForm> workStatusList = new ArrayList<>();
		for (LocalDate date : dateRangeList) {
			Duty dutyForDate = findDutyForDate(dutyList, date);
			UpdateForm updateForm = new UpdateForm();

			if (dutyForDate != null) {
				updateForm.setWorkDate(dutyForDate.getWorkDate());
				updateForm.setStartTime(dutyForDate.getStartTime());
				updateForm.setEndTime(dutyForDate.getEndTime());
				updateForm.setBreakTime(dutyForDate.getBreakTime());
				updateForm.setOverTime(dutyForDate.getOverTime());
			}
			workStatusList.add(updateForm);
		}

		//総労働日数、総休憩時間、総残業時間の計算、総労働時間を計算、 総残業時間を計算
		int totalWorkingDays = GetTotalWorkingDays(dutyList);
		String totalBreakTime = calculateTotalBreakTime(dutyList);
		String totalWorkingHours = calculateTotalWorkingHours(dutyList);
		String totalOvertime = calculateTotalOvertime(dutyList);

		model.addAttribute("totalWorkingDays", totalWorkingDays);
		model.addAttribute("totalBreakTime", totalBreakTime);
		model.addAttribute("totalWorkingHours", totalWorkingHours);
		model.addAttribute("totalOvertime", totalOvertime);
		model.addAttribute("periodOptions", DateRange.generateDateRanges());
		model.addAttribute("workStatusList", workStatusList);
		model.addAttribute("dateRangeList", dateRangeList);
		model.addAttribute("mode", mode);
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

	// 総労働時間を計算する関数
	private String calculateTotalWorkingHours(List<Duty> dutyList) {
		long totalWorkingMinutes = dutyList.stream()
				.mapToLong(duty -> {
					// 総労働時間の計算: (退勤時刻 - 出勤時刻) - 休憩時間 + 残業時間
					long workingMinutes = ((duty.getEndTime() != null
							? duty.getEndTime().getHour() * 60 + duty.getEndTime().getMinute()
							: 0)
							- (duty.getStartTime() != null
									? duty.getStartTime().getHour() * 60 + duty.getStartTime().getMinute()
									: 0)
							- (duty.getBreakTime() != null
									? duty.getBreakTime().getHour() * 60 + duty.getBreakTime().getMinute()
									: 0)
							+ (duty.getOverTime() != null
									? duty.getOverTime().getHour() * 60 + duty.getOverTime().getMinute()
									: 0));
					return workingMinutes;
				})
				.sum();

		// 総労働時間を時間と分に分割
		int totalWorkingHours = (int) totalWorkingMinutes / 60;
		int totalWorkingMinutesPart = (int) totalWorkingMinutes % 60;

		return String.format("%02d", totalWorkingHours); // 時間部分のみを返す
	}

	// 総休憩時間を計算する関数
	private String calculateTotalBreakTime(List<Duty> dutyList) {
		long totalBreaktime = dutyList.stream()
				.mapToLong(duty -> {
					LocalTime breakTime = duty.getBreakTime() != null ? duty.getBreakTime() : LocalTime.of(0, 0);
					return breakTime.getHour() * 60 + breakTime.getMinute();
				})
				.sum();

		// 総休憩時間を時間と分に分割
		int totalBreakHours = (int) totalBreaktime / 60;
		int totalBreakMinutesPart = (int) totalBreaktime % 60;

		return String.format("%02d", totalBreakHours); // 時間部分のみを返す
	}

	// 総残業時間を計算する関数
	private String calculateTotalOvertime(List<Duty> dutyList) {
		long totalOvertimeMinutes = dutyList.stream()
				.mapToLong(duty -> {
					LocalTime overtime = duty.getOverTime() != null ? duty.getOverTime() : LocalTime.of(0, 0);
					return overtime.getHour() * 60 + overtime.getMinute();
				})
				.sum();

		// 総残業時間を時間と分に分割
		int totalOvertimeHours = (int) totalOvertimeMinutes / 60;
		int totalOvertimeMinutesPart = (int) totalOvertimeMinutes % 60;

		return String.format("%02d", totalOvertimeHours); // 時間部分のみを返す
	}
}