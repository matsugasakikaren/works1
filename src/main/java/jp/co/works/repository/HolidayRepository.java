package jp.co.works.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
	Holiday save(Holiday holiday);

	Holiday findByHolidayId(Holiday holidayId);
}