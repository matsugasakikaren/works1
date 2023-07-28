package jp.co.works.form;

import java.sql.Time;
import java.util.Date;

public class UpdateForm {
	private Integer dutyId;
	private Integer userId;
	private Time startTime;
	private Time endTime;
	private Time breakTime;
	private Time overTime;
	private Date workDate;

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

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(Time breakTime) {
		this.breakTime = breakTime;
	}

	public Time getOverTime() {
		return overTime;
	}

	public void setOverTime(Time overTime) {
		this.overTime = overTime;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

}