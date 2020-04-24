package navi.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JourneyDateRepo extends JpaRepository<JourneyDate, Long> {

	public List<JourneyDate> findAllByOrderByTimeIdDesc();
	
}
