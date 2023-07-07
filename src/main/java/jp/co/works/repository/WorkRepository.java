package jp.co.works.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Work;

public interface WorkRepository extends JpaRepository<Work, Integer> {
	//Workオブジェクトをlistに追加
	List<Work> findAll();

}
