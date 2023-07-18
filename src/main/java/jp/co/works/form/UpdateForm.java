package jp.co.works.form;

import java.sql.Time;

public class UpdateForm {
	private static Integer userId;
	private Time starttime;
	private Time endtime;
	private Time breaktime;
	public static Integer getUserId() {
		return userId;
	}
	public static void setUserId(Integer userId) {
		UpdateForm.userId = userId;
	}
	public Time getStarttime() {
		return starttime;
	}
	public void setStarttime(Time starttime) {
		this.starttime = starttime;
	}
	public Time getEndtime() {
		return endtime;
	}
	public void setEndtime(Time endtime) {
		this.endtime = endtime;
	}
	public Time getBreaktime() {
		return breaktime;
	}
	public void setBreaktime(Time breaktime) {
		this.breaktime = breaktime;
	}
	
}
	
	