package jp.co.works.repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.works.entity.Duty;

public interface DutyRepository extends JpaRepository<Duty, Long> {
	List<Duty> findByUserIdAndWorkDateAndStartTime(Integer userId, Date WorkDate, Time startTime);

	List<Duty> findByUserIdAndWorkDate(Integer userId, Date WorkDate);

	List<Duty> findByUserId(Integer userId);

	List<Duty> findByUserIdAndWorkDate(Integer userId, LocalDate now);	
	
	List<Duty> getReferenceByUserId(Integer userId);
	//Dutyオブジェクトを保存・更新
	Duty save(Duty duty);

	List<Duty> findByUserIdOrderByWorkDateDesc(Integer userId);
	
	@Query("SELECT d FROM Duty d WHERE d.userId = ?1 AND d.workDate BETWEEN ?2 AND ?3")
    List<Duty> findByUserIdAndWorkDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);

	List<Duty> findByUserIdAndWorkDateIn(Integer userId, List<LocalDate> dateList);

	Duty findByWorkDate(LocalDate workDate);

	Optional<Duty> findByDutyId(Integer dutyId);
}