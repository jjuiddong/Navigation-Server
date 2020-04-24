package navi.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import navi.model.JourneyDate;
import navi.service.JourneyDateService;
import navi.service.PathService;

@Controller
public class PathController {
	
	@Autowired
	JourneyDateService journeyDateService;
	

	@RequestMapping("pathForm")
	String listPath(Model model) {
		
		List<JourneyDate> journeys = journeyDateService.JourneyDateRepo.findAllByOrderByTimeIdDesc();
		model.addAttribute("journeys", journeys);		
		
		return "pathForm";
	}

	
	//@RequestMapping(value = "{date}", method = RequestMethod.GET)
	@RequestMapping("pathForm/{journeyId}")
	String pathForm( @PathVariable("journeyId") Long pathId,
			Model model) {
		
		List<JourneyDate> journeys = journeyDateService.JourneyDateRepo.findAll();
		model.addAttribute("journeys", journeys);		
		
		return "pathForm";
	}
	
	
}

