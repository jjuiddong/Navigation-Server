package navi.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import navi.model.PathRepo;

@Service
@Transactional
public class PathService {

	@Autowired
	public PathRepo pathRepo;
	
}
