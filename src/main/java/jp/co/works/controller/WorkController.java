package jp.co.works.controller;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
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
	 * convertToTimeメソッド
	 * String型からTime型に変換
	 * 
	 * @param timeString
	 * @return Time.valueOf(localTime)
	 */
	private Time convertToTime(String timeString) {
		LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
		return Time.valueOf(localTime);
	}

	private Date convertToDate(String strDate) {
		SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = simpledateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

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
		Integer userId = getLoginUser(); //ログインユーザーの情報を取得
		Date today = convertToDate(getDate()); //現在日の取得
		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDate(userId, today); //当日のレコードがあるか検索

		if (!dutyList.isEmpty()) {
			Duty latestDuty = dutyList.get(0);
			model.addAttribute("startTime", latestDuty.getStartTime());
			model.addAttribute("endTime", latestDuty.getEndTime());
		} else {
			model.addAttribute("startTime", null); // 出勤時刻が未登録の場合はnullを設定
			model.addAttribute("endTime", null); // 退勤時刻が未登録の場合はnullを設定
		}
		model.addAttribute("workList", workRepository.findAll());
		model.addAttribute("selectedWorkId", 0); // デフォルトの選択値を設定

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
		String starttime = abc();
		Time startTime = convertToTime(starttime);
		String newdate = getDate();
		Date workDate = convertToDate(newdate);

		// 現在日のレコードがあるか検索
		List<Duty> dutyList1 = dutyRepository.findByUserIdAndWorkDate(startLogin, workDate);

		if (dutyList1.isEmpty()) {
			//レコードがない場合 新しいレコード作成
			Duty duty = new Duty();
			duty.setStartTime(startTime);
			duty.setUserId(startLogin);
			duty.setWorkDate(workDate);
			dutyRepository.save(duty);
		} else {
			//既存のレコードを更新
			Duty duty1 = dutyList1.get(0);
			duty1.setStartTime(startTime);
			dutyRepository.save(duty1);
			model.addAttribute("startTime", startTime);
		}

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
	@PostMapping(path = "/employment/endWork")
	public String endWork(@RequestParam("workId") Integer workId, Model model) {
		Integer startLogin = getLoginUser();

		String endTime = abc();
		Time EndTime = convertToTime(endTime);
		String newdate = getDate();
		Date workDate = convertToDate(newdate);

		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDate(startLogin, workDate);

		if (!dutyList.isEmpty()) {
			Duty duty = dutyList.get(0);
			duty.setEndTime(EndTime);

			Work work = workRepository.findById(workId).orElse(null);
			duty.setWork(work);

			setBreakhours(duty, duty.getStartTime(), EndTime);

			Time overTime = Overtimehours(duty.getStartTime(), EndTime, duty.getBreakTime());
			duty.setOverTime(overTime);

			dutyRepository.save(duty);
			model.addAttribute("endTime", EndTime);
			model.addAttribute("overTime", overTime);
		}
		return "employment";
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
	private void setBreakhours(Duty duty, Time DecidedstartTime, Time DecidedendTime) {
		long actualworkinghours = (DecidedendTime.getTime() - DecidedstartTime.getTime()) / (60 * 1000);

		int hours = (int) actualworkinghours / 60;
		int minutes = (int) actualworkinghours % 60;

		if (hours >= 8 * 60) { //8h以上の場合
			duty.setBreakTime(Time.valueOf("01:00:00")); //1hと設定
		} else if (hours >= 6 * 60 && actualworkinghours < 8 * 60) { //6h以上8h未満
			duty.setBreakTime(Time.valueOf("00:" + String.format("%02d", minutes) + ":00"));
		} else { //それ以外
			duty.setBreakTime(Time.valueOf("00:00:00"));
		}
	}

	// 残業時間を計算するメソッド
	private Time Overtimehours(Time startTime, Time endTime, Time breakTime) {
		if (startTime != null && endTime != null && breakTime != null) {
			LocalTime startTimeLocal = startTime.toLocalTime();
			LocalTime endTimeLocal = endTime.toLocalTime();
			LocalTime breakTimeLocal = breakTime.toLocalTime();

			long workingMinutes = startTimeLocal.until(endTimeLocal, ChronoUnit.MINUTES);
			long breakMinutes = breakTimeLocal.getHour() * 60 + breakTimeLocal.getMinute();

			long expectedWorkingMinutes = 8 * 60;
			long actualWorkingMinutes = workingMinutes - breakMinutes;

			if (actualWorkingMinutes > expectedWorkingMinutes) {
				long overtimeMinutes = actualWorkingMinutes - expectedWorkingMinutes;
				int hours = (int) (overtimeMinutes / 60);
				int minutes = (int) (overtimeMinutes % 60);

				return Time.valueOf(String.format("%02d:%02d:00", hours, minutes));
			} else {
				return Time.valueOf("00:00:00"); // 残業時間がない場合は00:00:00を設定
			}
		}

		// 残業時間がない場合や異常な場合はnullを返す
		return null;
	}
}
