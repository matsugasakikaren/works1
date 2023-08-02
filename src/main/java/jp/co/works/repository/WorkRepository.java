package jp.co.works.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.works.entity.Work;

public interface WorkRepository extends JpaRepository<Work, Integer> {
	Optional<Work> findById(Integer workId);
	Work findByWorkName(String workName);

	//Workオブジェクトをlistに追加
	List<Work> findAll();
	Work findByWorkId(Integer workId);
}
