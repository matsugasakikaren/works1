package jp.co.works.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	//userIdとpasswordをDBから検索し返す
	Employee findByUserIdAndPassword(Integer userId, String password);
	Optional<Employee> findById(Integer userId);
}