package jp.co.works.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Duty;

public interface DutyRepository extends JpaRepository<Duty, Integer> {
	//Dutyオブジェクトを保存・更新
	Duty save(Duty duty);
}
