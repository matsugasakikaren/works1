package jp.co.works.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.works.HolidayDto;
import jp.co.works.form.HolidayForm;
import jp.co.works.service.DutyService;
import jp.co.works.service.HolidayService;

@Controller
public class ApplicationController extends AccountController {
	private final HolidayService holidayService;
	private final DutyService dutyService;

	@Autowired
	public ApplicationController(HolidayService holidayService, DutyService dutyService) {
		this.holidayService = holidayService;
		this.dutyService = dutyService;
	}

	@GetMapping("/application")
	public String showHolidayForm(Model model) {
		// 申請日の選択肢を生成（当日から1か月前までと1か月先まで）
		LocalDate today = LocalDate.now();
		LocalDate oneMonthBefore = today.minusMonths(1);
		LocalDate oneMonthAfter = today.plusMonths(1);
		List<String> holidayDates = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		while (!oneMonthAfter.isBefore(oneMonthBefore)) {
			holidayDates.add(oneMonthAfter.format(formatter));
			oneMonthAfter = oneMonthAfter.minusDays(1);
		}
		model.addAttribute("holidayDates", holidayDates);

		// 休暇名の選択肢を設定
		List<String> holidayNames = new ArrayList<>();
		holidayNames.add("有給休暇");
		holidayNames.add("特別休暇");
		holidayNames.add("夏季休暇");
		holidayNames.add("慶弔休暇");
		holidayNames.add("年末年始休暇");
		holidayNames.add("代休");
		holidayNames.add("振替");
		holidayNames.add("半休");
		model.addAttribute("holidayNames", holidayNames);

		// これまでの申請情報を取得
		List<HolidayDto> holidayDtoList = holidayService.getAllHolidayDto();
		model.addAttribute("holidayDtoList", holidayDtoList);
		model.addAttribute("holidayForm", new HolidayForm());
		return "application";
	}

	@PostMapping("/save")
	public String saveHoliday(@ModelAttribute("holidayForm") @Valid HolidayForm holidayForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			// 入力エラーがある場合は申請フォームに戻す
			// エラーメッセージが表示されるようにモデルにエラー情報をセット
			model.addAttribute("holidayForm", holidayForm);
			return "application";
		}
		Integer userId = getLoginUser();
		holidayService.saveHoliday(userId, holidayForm);
		return "redirect:/application"; // 申請後に申請フォームにリダイレクト
	}
}