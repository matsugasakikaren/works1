package jp.co.works.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Holiday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int holidayId;

	private String holidayName;

	private Date holidayStart;
	private Date holidayEnd;
	private Date requestDate;
	private int decisionId;

	public int getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(int holidayId) {
		this.holidayId = holidayId;
	}

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

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public int getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(int decisionId) {
		this.decisionId = decisionId;
	}

}
