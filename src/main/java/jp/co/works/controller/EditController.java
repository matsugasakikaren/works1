package jp.co.works.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.works.entity.Duty;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.EmployeeRepository;
import jp.co.works.service.DutyService;
import jp.co.works.utils.DateRange;

@Controller
public class EditController extends AccountController {
	private final DutyRepository dutyRepository;
	private final EmployeeRepository employeeRepository;
	private final jp.co.works.service.DutyService dutyService;

	private List<LocalDate> selectedDateList;
	private List<Duty> dutyList;

	@Autowired
	public EditController(DutyRepository dutyRepository, EmployeeRepository employeeRepository,
			DutyService dutyService) {
		this.dutyRepository = dutyRepository;
		this.employeeRepository = employeeRepository;
		this.dutyService = dutyService;
	}

	/*@param Model model
	* @param workingHours 出勤から退勤までの就業時間
	* @param brakTime 休憩時間
	* @param workTime 就業時間から休憩時間を引いた労働時間
	*
	*/
	@RequestMapping(path = "/edit", method = RequestMethod.GET)
	public String showEditPage(Model model) {
		//ログイン中のユーザーIDを取得
		Integer userId = getLoginUser();

		//現在日を取得
		LocalDate today = LocalDate.now();
		//先月16日から当月15日まで
		LocalDate startDate = today.withDayOfMonth(16).minusMonths(1);
		LocalDate endDate = today.withDayOfMonth(15);

		//該当期間のレコードを取得
		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDateBetween(userId, startDate, endDate);

		//ドロップダウンリストの選択肢を生成
		List<String> periodOptions = DateRange.generateDateRanges();
		model.addAttribute("periodOptions", periodOptions);

		//期間の日付を1日ずつ生成してリストに格納
		List<LocalDate> dateRangeList = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			dateRangeList.add(currentDate);
			currentDate = currentDate.plusDays(1);
		}
		model.addAttribute("dateRangeList", dateRangeList);

		//Mapを定義
		Map<LocalDate, Duty> workStatusMap = new HashMap<>();

		//日付ごとにdutyテーブルの情報を取得、mapに格納
		for (LocalDate date : dateRangeList) {
			Duty dutyForDate = findDutyForDate(dutyList, date);
			if (dutyForDate == null) {
				dutyForDate = new Duty();
			}
			workStatusMap.put(date, dutyForDate);
		}

		// workStatusMapを日付でソートしたMapを作成
		Map<LocalDate, Duty> workStatusMapSorted = workStatusMap.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		//総労働日数、総休憩時間、総残業時間の計算、総労働時間を計算、 総残業時間を計算
		int totalWorkingDays = GetTotalWorkingDays(dutyList);
		String totalBreakTime = calculateTotalBreakTime(dutyList);
	    String totalWorkingHours = calculateTotalWorkingHours(dutyList);
	    String totalOvertime = calculateTotalOvertime(dutyList);
	    
	   

		//インスタンス変数に代入
		this.selectedDateList = dateRangeList;
		this.dutyList = dutyList;

		//modelにデータを追加
		model.addAttribute("workStatusMapSorted", workStatusMapSorted);
		model.addAttribute("dutyList", dutyList);
		model.addAttribute("totalWorkingDays", totalWorkingDays);
		model.addAttribute("totalBreakTime", totalBreakTime);
		model.addAttribute("totalWorkingHours", totalWorkingHours);
		model.addAttribute("totalOvertime", totalOvertime);
		model.addAttribute("mode", "show");
		return "edit";
	}

	private Duty findDutyForDate(List<Duty> dutyList, LocalDate date) {
		for (Duty duty : dutyList) {
			if (duty.getWorkDate().equals(date)) {
				return duty;
			}
		}
		return null;
	}

	@PostMapping(path = "/edit/list")
	public String handleEditForm(@RequestParam("selectedPeriod") String selectedPeriod, Model model) {
		// 選択された期間の日付リストを取得
		List<LocalDate> selectedDateList = DateRange.getSelectedDateList(selectedPeriod);

		LocalDate startDate = selectedDateList.get(0); // 開始日を取得
		LocalDate endDate = selectedDateList.get(selectedDateList.size() - 1); // 終了日を取得
		Integer userId = getLoginUser();
		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDateBetween(userId, startDate, endDate);

		Map<LocalDate, Duty> workStatusMap = new HashMap<>();
		for (LocalDate date : selectedDateList) {
			Duty dutyForDate = findDutyForDate(dutyList, date);
			if (dutyForDate == null) {
				dutyForDate = new Duty();
			}
			workStatusMap.put(date, dutyForDate);
		}

		// workStatusMapを日付でソートしたMapを作成
		Map<LocalDate, Duty> workStatusMapSorted = workStatusMap.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		int totalWorkingDays = GetTotalWorkingDays(dutyList);
		String totalBreakTime = calculateTotalBreakTime(dutyList);
		String totalWorkingHours = calculateTotalWorkingHours(dutyList);
	    String totalOvertime = calculateTotalOvertime(dutyList);

		model.addAttribute("periodOptions", DateRange.generateDateRanges());
		model.addAttribute("selectedPeriod", selectedPeriod);
		model.addAttribute("dateRangeList", selectedDateList);
		model.addAttribute("workStatusMapSorted", workStatusMapSorted);
		model.addAttribute("dutyList", dutyList);
		model.addAttribute("totalWorkingDays", totalWorkingDays);
		model.addAttribute("totalBreakTime", totalBreakTime);
		model.addAttribute("totalWorkingHours", totalWorkingHours);
		model.addAttribute("totalOvertime", totalOvertime);
		model.addAttribute("mode", "edit");

		// updateData メソッドに引き継ぐためのURLパラメータを設定
		model.addAttribute("selectedPeriodParam", selectedPeriod);

		return "edit";
	}

	private DateRange parseSelectedPeriod(String selectedPeriod) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String[] dates = selectedPeriod.split("～");
		LocalDate startDate = LocalDate.parse(dates[0], formatter);
		LocalDate endDate = LocalDate.parse(dates[1], formatter);

		return new DateRange(startDate, endDate);
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
	                long workingMinutes = ((duty.getEndTime() != null ? duty.getEndTime().getHour() * 60 + duty.getEndTime().getMinute() : 0)
	                        - (duty.getStartTime() != null ? duty.getStartTime().getHour() * 60 + duty.getStartTime().getMinute() : 0)
	                        - (duty.getBreakTime() != null ? duty.getBreakTime().getHour() * 60 + duty.getBreakTime().getMinute() : 0)
	                        + (duty.getOverTime() != null ? duty.getOverTime().getHour() * 60 + duty.getOverTime().getMinute() : 0));
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