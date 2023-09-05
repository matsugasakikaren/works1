package jp.co.works.form;

import java.util.Date;

public class HolidayForm {
	private String holidayName;
	private Date holidayStart;
	private Date holidayEnd;
	private int decisionId;
	private Date requestDate;
	private String approvalStatus;

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public Date getHolidayStart() {
		return holidayStart;
	}

	public void setHolidayStart(Date holidayStart) {
		this.holidayStart = holidayStart;
	}

	public Date getHolidayEnd() {
		return holidayEnd;
	}

	public void setHolidayEnd(Date holidayEnd) {
		this.holidayEnd = holidayEnd;
	}

	public int getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(int decisionId) {
		this.decisionId = decisionId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
}