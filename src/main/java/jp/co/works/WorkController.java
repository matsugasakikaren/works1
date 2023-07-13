package jp.co.works;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.works.entity.Duty;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.WorkRepository;

@Controller
public class WorkController extends LoginController {
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

	/*
	 * convertToTimeメソッド
	 * String型からTime型に変換
	 * 
	 * @param timeString
	 * @return Time.valueOf(localTime)
	 */
	private Time convertToTime(String timeString) {
		LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
		return Time.valueOf(localTime);
	}

	private Date convertToDate(String strDate) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = sdFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
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
	@RequestMapping(path = "/employment/endWork", method = RequestMethod.GET)
	public String endWork(Model model) {
		Integer startLogin = getLoginUser();

		String endTime = abc();
		Time EndTime = convertToTime(endTime);
		String newdate = getDate();
		Date workDate = convertToDate(newdate);

		List<Duty> dutyList = dutyRepository.findByUserIdAndWorkDate(startLogin, workDate);

		if (!dutyList.isEmpty()) {
			Duty duty = dutyList.get(0);
			duty.setEndTime(EndTime);
			dutyRepository.save(duty);
			model.addAttribute("endTime", EndTime);
		}

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
		String Time = dtformat1.format(time1);
		return Time;
	}

	/*
	 * getDateメソッド
	 * 
	 * @return newdate 現在日を取得
	 */
	private String getDate() {
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		String newdate = date.format(cd.getTime());
		return newdate;
	}

}
