package jp.co.works.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.works.HolidayDto;
import jp.co.works.entity.Approval;
import jp.co.works.entity.Holiday;
import jp.co.works.form.HolidayForm;
import jp.co.works.repository.ApprovalRepository;
import jp.co.works.repository.HolidayRepository;

@Service
public class HolidayService {
	private final HolidayRepository holidayRepository;
    private final ApprovalRepository approvalRepository;

    @Autowired
    public HolidayService(HolidayRepository holidayRepository, ApprovalRepository approvalRepository) {
        this.holidayRepository = holidayRepository;
        this.approvalRepository = approvalRepository;
    }
    

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }
    
    public List<HolidayDto> getAllHolidayDto() {
        List<Holiday> holidays = holidayRepository.findAll();
        return holidays.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // エンティティをDTOに変換するメソッド
    private HolidayDto convertToDto(Holiday holiday) {
        HolidayDto holidayDto = new HolidayDto();
        holidayDto.setHolidayId(holiday.getHolidayId());
        holidayDto.setHolidayStart(holiday.getHolidayStart());
        holidayDto.setHolidayEnd(holiday.getHolidayEnd());
        // 他の必要な情報をセットする
        return holidayDto;
    }


	public void saveHoliday(Integer userId, HolidayForm holidayForm) {	
		String holidayName = holidayForm.getHolidayName();
	    Date holidayStart = holidayForm.getHolidayStart();
	    Date holidayEnd = holidayForm.getHolidayEnd();
	    int decisionId = holidayForm.getDecisionId();
	    Date requestDate = new Date(); 
	    // Holidayエンティティのインスタンスを作成
	    Holiday holiday = new Holiday();
	    holiday.setHolidayName(holidayName);
	    holiday.setHolidayStart((java.sql.Date) holidayStart);
	    holiday.setHolidayEnd((java.sql.Date) holidayEnd);
	    holiday.setDecisionId(decisionId);
	    holiday.setRequestDate((java.sql.Date) requestDate);


	    // データベースに休暇情報を保存
	    holidayRepository.save(holiday);
	}

	public HolidayForm getHolidayFormByHolidayId(int holidayId) {
	    Holiday holiday = holidayRepository.findById(holidayId).orElse(null);
	    HolidayForm holidayForm = new HolidayForm();
	    if (holiday != null) {
	        holidayForm.setHolidayStart(holiday.getHolidayStart());
	        holidayForm.setHolidayEnd(holiday.getHolidayEnd());
	        holidayForm.setRequestDate(holiday.getRequestDate());

	        Approval approval = approvalRepository.findById(holiday.getDecisionId()).orElse(null);
	        if (approval != null) {
	            holidayForm.setApprovalStatus(approval.getDecisionName());
	        }
	    }
	    return holidayForm;
	}
	
}
