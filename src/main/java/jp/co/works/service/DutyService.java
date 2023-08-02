package jp.co.works.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.works.UpdateFormParam;
import jp.co.works.entity.Duty;
import jp.co.works.form.UpdateForm;
import jp.co.works.repository.DutyRepository;

@Service
public class DutyService {
	@Autowired
	private final DutyRepository dutyRepository;

	@Autowired
	public DutyService(DutyRepository dutyRepository) {
		this.dutyRepository = dutyRepository;
	}

	public List<Duty> getAllDuties() {
		return dutyRepository.findAll();
	}

	/**
	 *ユーザー情報 全検索
	 * @return 検索結果
	 */
	public UpdateFormParam searchAll() {
		//ユーザー情報を取得
		List<Duty> dutyList = dutyRepository.findAll();
		UpdateFormParam updateFormParam = new UpdateFormParam();
		List<UpdateForm> list = new ArrayList<UpdateForm>();
		for (Duty duty : dutyList) {
			UpdateForm updateform = new UpdateForm();
			updateform.setUserId(duty.getUserId());
			updateform.setStartTime(duty.getStartTime());
			updateform.setEndTime(duty.getEndTime());
			updateform.setBreakTime(duty.getBreakTime());
			updateform.setOverTime(duty.getOverTime());
			list.add(updateform);
		}
		updateFormParam.setFormList(list);
		return updateFormParam;
	}

	/**
	 * ユーザー情報更新
	 *  @param param 画面パラメータ
	 */
	public void updateAll(List<UpdateForm> formList) {
		for (UpdateForm updateForm : formList) {
			// UpdateFormから必要なデータを取得
			Date workDate = updateForm.getWorkDate();
			Time startTime = updateForm.getStartTime();
			Time endTime = updateForm.getEndTime();
			Time breakTime = updateForm.getBreakTime();
			Time overTime = updateForm.getOverTime();

			// データベースから対応するDutyを取得
			Duty duty = dutyRepository.findByWorkDate(workDate);
			if (duty == null) {
				// 該当するDutyが存在しない場合はスキップ
				continue;
			}

			// 取得したDutyの情報を更新
			duty.setStartTime(startTime);
			duty.setEndTime(endTime);
			duty.setBreakTime(breakTime);
			duty.setOverTime(overTime);

			// データベースを更新
			dutyRepository.save(duty);
		}
	}

	public void updateDuty(UpdateForm updateForm) {
		Date workDate = updateForm.getWorkDate();
		Time startTime = updateForm.getStartTime();
		Time endTime = updateForm.getEndTime();
		Time breakTime = updateForm.getBreakTime();
		Time overTime = updateForm.getOverTime();

		// データベースから対応するDutyを取得
		Duty duty = dutyRepository.findByWorkDate(workDate);
		if (duty == null) {
			// 該当するDutyが存在しない場合はスキップ
			return;
		}

		// 取得したDutyの情報を更新
		duty.setStartTime(startTime);
		duty.setEndTime(endTime);
		duty.setBreakTime(breakTime);
		duty.setOverTime(overTime);

		// データベースを更新
		dutyRepository.save(duty);
	}
}