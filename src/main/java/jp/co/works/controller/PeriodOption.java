package jp.co.works.controller;

import java.util.Date;

public class PeriodOption {
	private String label;
	private int value;
	private Date startDate;
	private Date endDate;

	public PeriodOption(String label, int value, Date startDate, Date endDate) {
		this.label = label;
		this.value = value;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getLabel() {
		return label;
	}

	public int getValue() {
		return value;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return label;
	}
}