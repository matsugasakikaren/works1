package jp.co.works;

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

public class EditController {
	@Autowired
	private DutyRepository dutyRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@RequestMapping(path = "/edit", method = RequestMethod.GET)
	public String showEditPage(Model model) {
		List<Duty> dutyList = dutyRepository.findAll();
		model.addAttribute("dutyList", dutyList);
		return "edit";
	}
	

}
