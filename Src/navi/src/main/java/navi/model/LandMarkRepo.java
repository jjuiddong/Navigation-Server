package navi.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandMarkRepo extends JpaRepository<LandMark, Long> {
	
	public Page<LandMark> findAll(Pageable pageable);
	
	public List<LandMark> findAll();
	
	public List<LandMark> findByUserId(Long userId);
	
}
