package navi.service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import navi.model.JourneyDateRepo;

@Service
@Transactional
public class JourneyDateService {

	@Autowired
	public JourneyDateRepo JourneyDateRepo;

}
