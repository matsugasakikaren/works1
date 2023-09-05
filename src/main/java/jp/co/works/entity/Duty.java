package jp.co.works.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@Column(name = "break_time")
	private LocalTime breakTime;

	@ManyToOne
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "work_id")
	private Work work;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private Employee employee;

	@Column(name = "over_time")
	private LocalTime overTime;

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

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
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

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LocalTime getOverTime() {
		return overTime;
	}

	public void setOverTime(LocalTime overTime) {
		this.overTime = overTime;
	}
}