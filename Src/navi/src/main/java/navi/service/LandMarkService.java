package navi.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import navi.model.LandMark;
import navi.model.LandMarkRepo;

@Service
@Transactional
public class LandMarkService {
	
	@Autowired
	LandMarkRepo landMarkRepo;

	public Page<LandMark> findPage(Pageable pageable) {
		return landMarkRepo.findAll(pageable);
	}
	
	public List<LandMark> findAll() {
		return landMarkRepo.findAll();
	}
	
	public List<LandMark> findByUserId(Long userId) {
		return landMarkRepo.findByUserId(userId);
	}
	
	public Optional<LandMark> findById(Long id) {
		return landMarkRepo.findById(id);
	}
	
	public long getCount() {
		return landMarkRepo.count();
	}
	
	public boolean save(LandMark landMark) {
		landMarkRepo.save(landMark);
		return true;
	}
	
}
