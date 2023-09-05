package jp.co.works.utils;

import java.sql.Date;
import java.time.LocalTime;

public class DutyInfo {
	private Date workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private LocalTime overTime;
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime localTime) {
		this.startTime = localTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime localTime) {
		this.endTime = localTime;
	}
	public LocalTime getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(LocalTime localTime) {
		this.breakTime = localTime;
	}
	public LocalTime getOverTime() {
		return overTime;
	}
	public void setOverTime(LocalTime localTime) {
		this.overTime = localTime;
	}
}