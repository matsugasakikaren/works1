package jp.co.works.entity;


import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "holiday")
public class Holiday {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private int holidayId;

	@Column(name = "holiday_name")
	private String holidayName;
	
	@Column(name = "holiday_start")
	private Date holidayStart;
	
	@Column(name = "holiday_end")
	private Date holidayEnd;
	
	@Column(name = "request_date")
	private LocalDate requestDate;
	
	@ManyToOne
	@JoinColumn(name = "decision_id")
	private Approval approval;
	
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

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate today) {
		this.requestDate = today;
	}

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}

	public void setDecisionId(int decisionId) {
		if (approval == null) {
			approval = new Approval();
		}
		approval.setDecisionId(decisionId);
		
	}
}