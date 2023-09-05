package jp.co.works.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Approval;
import jp.co.works.entity.Holiday;

public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
	Optional<Holiday> findByDecisionId(Approval approval);
}