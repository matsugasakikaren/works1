package jp.co.works;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.works.entity.Duty;
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.WorkRepository;

@Controller
public class WorkController {
	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private WorkRepository workRepository;

	@RequestMapping(path = "/employment")
	public String showEmploymentPage(String workName, Model model) {
		//追記
		model.addAttribute("workList", workRepository.findAll());
		return "employment";
	}

	//employment/startWorkにアクセス POSTリクエストを処理
	@RequestMapping(path = "/employment/startWork", method = RequestMethod.GET)
	public String startWork(Model model) {
		//現在の時刻を取得
		LocalTime time1 = LocalTime.now();

		//表示形式の指定
		DateTimeFormatter dtformat1 = DateTimeFormatter.ofPattern("HH:mm:ss");

		//String型に変換
		String startTime = dtformat1.format(time1);

		//Dutyオブジェクト生成
		Duty duty = new Duty();
		duty.setStartTime(Time.valueOf(time1));

		dutyRepository.save(duty);

		//modelに追加
		model.addAttribute("startTime", startTime);

		return "employment";
	}

	@RequestMapping(path = "/employment/endWork", method = RequestMethod.GET)
	public String endWork(Model model) {
		//現在の時刻を取得
		LocalTime time2 = LocalTime.now();

		//表示形式の指定
		DateTimeFormatter dtformat2 = DateTimeFormatter.ofPattern("HH:mm:ss");

		//String型に変換
		String endTime = dtformat2.format(time2);

		//Dutyオブジェクト生成
		Duty duty = new Duty();
		duty.setStartTime(Time.valueOf(time2));

		dutyRepository.save(duty);
		//modelに追加
		model.addAttribute("endTime", endTime);
		return "employment";
	}
}
