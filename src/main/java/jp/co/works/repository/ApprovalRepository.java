package jp.co.works.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Integer> {

}
