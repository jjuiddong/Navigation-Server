package navi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PathRepo extends JpaRepository<Path, Long> {

	List<Path> findByDateTime(LocalDate date);
	
	List<Path> findByDateTimeBetween(LocalDateTime date0, LocalDateTime date1);
	
	List<Path> findByJourneyTimeId(Long journeyTimeId);
	
}
