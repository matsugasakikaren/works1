package jp.co.works.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval")
public class Approval {
	@Id
	@Column(name = "decision_id")
	private Integer decisionId;

	@Column(name = "decision_name", length = 20)
	private String decisionName;

	public Integer getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(Integer decisionId) {
		this.decisionId = decisionId;
	}

	public String getDecisionName() {
		return decisionName;
	}

	public void setDecisionName(String decisionName) {
		this.decisionName = decisionName;
	}

}
