package jp.co.works.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.EmployeeHoliday;

public interface EmployeeHolidayRepository extends JpaRepository<EmployeeHoliday, Integer> {
	List<EmployeeHoliday> findAll();
	List<EmployeeHoliday> findByUserId(Integer userId);
}