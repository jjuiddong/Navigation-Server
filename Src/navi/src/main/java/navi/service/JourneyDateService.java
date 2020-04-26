package navi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

	public List<JourneyDate> findAll() {
		return journeyDateRepo.findAll();
	}
	
	public long getCount() {
		return journeyDateRepo.count();
	}
	
}
