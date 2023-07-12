package jp.co.works.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Duty;

public interface DutyRepository extends JpaRepository<Duty, Integer> {
	//Dutyオブジェクトを保存・更新
	Duty save(Duty duty);
	
	List<Duty> findByUserIdAndWorkDate(Integer userId, Date WorkDate);
	

}
