package jp.co.works.entity;

import java.sql.Time;
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
@Table(name = "duty")
public class Duty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "duty_id")
	private Integer dutyId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "work_date")
	private LocalDate workDate;

	@Column(name = "start_time")
	private Time startTime;

	@Column(name = "end_time")
	private Time endTime;

	@Column(name = "break_time")
	private Time breakTime;

	@ManyToOne
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "work_id")
	private Work work;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private Employee employee;

	@Column(name = "over_time")
	private Time overTime;

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Time getOverTime() {
		return overTime;
	}

	public void setOverTime(Time overTime) {
		this.overTime = overTime;
	}
}