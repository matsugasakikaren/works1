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
import jp.co.works.repository.DutyRepository;
import jp.co.works.repository.EmployeeRepository;

@Controller

public class EditController extends LoginController {
	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@RequestMapping(path = "/edit", method = RequestMethod.GET)
	public String showEditPage(Model model) {
		Integer userId = getLoginUser(); // ログイン中のユーザーIDを取得
		List<Duty> dutyList = dutyRepository.findByUserId(userId);
		model.addAttribute("dutyList", dutyList);

		List<String> dateList = cDateList();
		model.addAttribute("dateList", dateList);
		return "edit";
	}

	@RequestMapping(path = "/edit/update", method = RequestMethod.POST)
	public String showupdatePage(Model model) {
		List<Duty> dutyList = dutyRepository.findAll();
		model.addAttribute("dutyList", dutyList);
		return "update";
	}

	private List<String> cDateList() {
		List<String> dateList = new ArrayList<>();
		LocalDate cDate = LocalDate.now();
		LocalDate startDate = cDate.withDayOfMonth(16);
		LocalDate endDate = startDate.plusMonths(1).minusDays(1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
			dateList.add(startDate.format(formatter));
			startDate = startDate.plusDays(1);
		}

		return dateList;
	}
}