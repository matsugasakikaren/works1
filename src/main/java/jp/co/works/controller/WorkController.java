package jp.co.works.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.works.entity.Duty;
import jp.co.works.entity.Work;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.WorkRepository;

@Controller
public class WorkController extends AccountController {
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
		Integer userId = getLoginUser();
		LocalDate today = LocalDate.now();
		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDate(userId, today);

		if (!dutyList.isEmpty()) {
			Duty latestDuty = dutyList.get(0);
			model.addAttribute("startTime", latestDuty.getStartTime());
			model.addAttribute("endTime", latestDuty.getEndTime());
		} else {
			model.addAttribute("startTime", null);
			model.addAttribute("endTime", null);
		}
		model.addAttribute("workList", workRepository.findAll());
		model.addAttribute("selectedWorkId", 0);
		model.addAttribute("currentDate", getDate()); // 現在日
		model.addAttribute("RealtimeClockArea", abc()); // 現在時刻

		return "employment";
	}

	/*
	 * startWorkメソッド
	 * 出勤ボタンを押した後の出退勤画面を表示
	 * 
	 * @return employment
	 * 
	 */
	@RequestMapping(path = "/employment/startWork", method = RequestMethod.POST)
	public String startWork(Model model) {
		Integer startLogin = getLoginUser();
		LocalTime startTime = LocalTime.now();
		LocalDate workDate = LocalDate.now();

		List<Duty> dutyList1 = dutyRepository.findByUserIdAndWorkDate(startLogin, workDate);

		if (dutyList1.isEmpty()) {
			Duty duty = new Duty();
			duty.setStartTime(startTime);
			duty.setUserId(startLogin);
			duty.setWorkDate(workDate);
			dutyRepository.save(duty);
			model.addAttribute("startTimeFormatted", startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
		} else {
			//model.addAttribute("errorMessage", "既に出勤済みです。");
			LocalTime existingStartTime = dutyList1.get(0).getStartTime();
			model.addAttribute("startTimeFormatted", existingStartTime.format(DateTimeFormatter.ofPattern("HH:mm")));
		}

		return "redirect:/employment";
	}

	/*
	 * endWorkメソッド
	 * 退勤ボタンを押した後の出退勤画面を表示
	 * 
	 * @param model endTimeという名前で退勤時刻を追加
	 * @return employment
	 * 
	 */
	@PostMapping(path = "/employment/endWork")
	public String endWork(@RequestParam("workId") Integer workId, Model model) {
		Integer startLogin = getLoginUser();

		LocalTime endTime = LocalTime.now();
		LocalDate workDate = LocalDate.now();

		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDate(startLogin, workDate);

		if (dutyList.isEmpty()) {
			//model.addAttribute("errorMessage", "出勤していません。");
		} else {
			Duty duty = dutyList.get(0);

			if (duty.getEndTime() == null) {
				LocalTime endTime1 = LocalTime.now();
				duty.setEndTime(endTime1);

				Work work = workRepository.findById(workId).orElse(null);
				duty.setWork(work);

				setBreakhours(duty, duty.getStartTime(), endTime1);

				LocalTime overTime = Overtimehours(duty.getStartTime(), endTime1, duty.getBreakTime());
				duty.setOverTime(overTime);

				dutyRepository.save(duty);
				model.addAttribute("endTime", endTime1.format(DateTimeFormatter.ofPattern("HH:mm")));
				model.addAttribute("overTime", overTime);
			} else {
				//model.addAttribute("errorMessage", "既に退勤済みです。");
			}
		}

		return "redirect:/employment";
	}

	/*
	 * abcメソッド
	 * 
	 * @return startTime 時刻を取得 
	 */
	private String abc() {
		LocalTime localtime = LocalTime.now();
		DateTimeFormatter datetimeformat = DateTimeFormatter.ofPattern("HH:mm");
		String Time = datetimeformat.format(localtime);
		return Time;
	}

	/*
	 * getDateメソッド
	 * 
	 * @return newdate 現在日を取得
	 */
	@ModelAttribute("currentDate")
	private String getDate() {
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		String newdate = date.format(cd.getTime());
		return newdate;
	}

	/*
	 * setBreakhoursメソッド
	 * 休憩時間の設定
	 * 
	 */
	private void setBreakhours(Duty duty, LocalTime localTime, LocalTime endTime) {
		long actualworkingminutes = ChronoUnit.MINUTES.between(localTime, endTime);

		int hours = (int) actualworkingminutes / 60;
		int minutes = (int) actualworkingminutes % 60;

		if (hours >= 8) {
			duty.setBreakTime(LocalTime.of(1, 0)); // 1hと設定
		} else if (hours >= 6 && actualworkingminutes < 8 * 60) {
			int breakHours = hours >= 1 ? 1 : 0;
			duty.setBreakTime(LocalTime.of(breakHours, minutes));
		} else {
			duty.setBreakTime(LocalTime.of(0, 0));
		}
	}

	// 残業時間を計算するメソッド
	private LocalTime Overtimehours(LocalTime localTime, LocalTime endTime, LocalTime localTime2) {
		if (localTime != null && endTime != null && localTime2 != null) {
			LocalTime startTimeLocal = localTime;
			LocalTime endTimeLocal = endTime;
			LocalTime breakTimeLocal = localTime2;

			long workingMinutes = startTimeLocal.until(endTimeLocal, ChronoUnit.MINUTES);
			long breakMinutes = breakTimeLocal.getHour() * 60 + breakTimeLocal.getMinute();

			long expectedWorkingMinutes = 8 * 60;
			long actualWorkingMinutes = workingMinutes - breakMinutes;

			if (actualWorkingMinutes > expectedWorkingMinutes) {
				long overtimeMinutes = actualWorkingMinutes - expectedWorkingMinutes;
				int hours = (int) (overtimeMinutes / 60);
				int minutes = (int) (overtimeMinutes % 60);

				return LocalTime.of(hours, minutes);
			} else {
				return LocalTime.of(0, 0);
			}
		}

		return null;
	}
}