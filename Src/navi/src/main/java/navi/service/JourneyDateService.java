package navi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import navi.model.JourneyDate;
import navi.model.JourneyDateRepo;

@Service
@Transactional
public class JourneyDateService {

	@Autowired
	public JourneyDateRepo journeyDateRepo;
	
	
	public Page<JourneyDate> getJourneyDateList(Pageable pageable) 
	{		
		return journeyDateRepo.findAllByOrderByTimeIdDesc(pageable);
	}

	public List<JourneyDate> findAllBySort() {
		return journeyDateRepo.findAllByOrderByTimeIdDesc();
	}
	
	public List<JourneyDate> findAll() {
		return journeyDateRepo.findAll();
	}
	
	public Optional<JourneyDate> findById(Long journeyId) {
		return journeyDateRepo.findById(journeyId);
	}
	
	public List<JourneyDate> findByDate(LocalDate date0, LocalDate date1) {
		return journeyDateRepo.findByDateBetween(date0,  date1);
	}
	
	
	public long getCount() {
		return journeyDateRepo.count();
	}
	
	public boolean save(JourneyDate journey) {
		journeyDateRepo.save(journey);
		return true;
	}
	
	
//	@Modifying
//	@Query(value=" UPDATE CM_USR_LOC "
//			   + " SET SHOW_YN = 'N' "
//			   + " WHERE USER_ID = :userId and SHOW_YN = 'Y' ", nativeQuery = true)
//	void updateShowYn(@Param("userId")String userId) throws Exception;
	
}
