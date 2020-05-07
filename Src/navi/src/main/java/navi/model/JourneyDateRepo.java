package navi.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JourneyDateRepo extends JpaRepository<JourneyDate, Long> {

	public List<JourneyDate> findAllByOrderByTimeIdDesc();
	
	public Page<JourneyDate> findAllByOrderByTimeIdDesc(Pageable pageable);
	
	public List<JourneyDate> findAll();
	
	public List<JourneyDate> findByDateBetween(LocalDate date0, LocalDate date1);

}
