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
		//int page = (pageable.getPageNumber() == 0)? 0 : pageable.getPageNumber() - 1;
//		int currentPage = pageable.getPageNumber();
//		int pageSize = pageable.getPageSize();
//		pageable = PageRequest.of(currentPage,  pageSize);
		return journeyDateRepo.findAllByOrderByTimeIdDesc(pageable);
		
//		List<JourneyDate> list = JourneyDateRepo.findAllByOrderByTimeIdDesc();
//		Page<JourneyDate> journeyPage 
//			= new PageImpl<JourneyDate>(list, pageable, list.size());
//		return journeyPage;		
	}
	
	public List<JourneyDate> findAll() {
		return journeyDateRepo.findAll();
	}
	
	public long getCount() {
		return journeyDateRepo.count();
	}
	
}
