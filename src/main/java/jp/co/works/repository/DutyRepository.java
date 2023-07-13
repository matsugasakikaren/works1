package jp.co.works.repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Duty;

public interface DutyRepository extends JpaRepository<Duty, Integer> {
	List<Duty> findByUserIdAndWorkDateAndStartTime(Integer userId, Date WorkDate, Time startTime);

	List<Duty> findByUserIdAndWorkDate(Integer userId, Date WorkDate);

	List<Duty> findByUserId(Integer userId);

	//Dutyオブジェクトを保存・更新
	Duty save(Duty duty);

}
