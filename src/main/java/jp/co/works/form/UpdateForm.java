package jp.co.works.form;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.Pattern;

public class UpdateForm {
	private Integer dutyId;
	private Integer userId;
	
	@Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "hh:mmの形式で入力してください")
	private LocalTime startTime;
	@Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "hh:mmの形式で入力してください")
	private LocalTime endTime;
	@Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "hh:mmの形式で入力してください")
	private LocalTime breakTime;
	@Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "hh:mmの形式で入力してください")
	private LocalTime overTime;
	
	private LocalDate workDate;

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalTime getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(LocalTime breakTime) {
		this.breakTime = breakTime;
	}

	public LocalTime getOverTime() {
		return overTime;
	}

	public void setOverTime(LocalTime overTime) {
		this.overTime = overTime;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate localDate) {
		this.workDate = localDate;
	}
}