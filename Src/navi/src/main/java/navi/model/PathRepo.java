package navi.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PathRepo extends JpaRepository<Path, Long> {

	List<Path> findByDateTime(LocalDate date);
	
	List<Path> findByDateTimeBetween(LocalDate date1, LocalDate date2);
	
	List<Path> findByJourneyTimeId(Long journeyTimeId);
	
}
