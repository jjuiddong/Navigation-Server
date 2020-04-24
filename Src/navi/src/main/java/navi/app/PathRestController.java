package navi.app;

//import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import navi.model.JourneyDate;
import navi.model.Path;
import navi.service.JourneyDateService;
import navi.service.PathService;


@RestController
@RequestMapping("path")
public class PathRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(PathRestController.class);

	@Autowired
	JourneyDateService journeyDateService;
	
	@Autowired
	PathService pathService;
	
	@RequestMapping(value = "{journeyId}/{journeyTimeId}", method = RequestMethod.GET)
	public @ResponseBody List<Path> path( @PathVariable Long journeyId
			, @PathVariable Long journeyTimeId ) 
	{
		
		logger.info("path request = " + journeyId + ", " + journeyTimeId);
		
		List<Path> list = pathService.pathRepo.findByJourneyTimeId(journeyTimeId);
		return list;		

//		Optional<JourneyDate> journey = journeyDateService.JourneyDateReop.findById(journeyId);
//		if (journey.get() != null)
//		{
//			JourneyDate j = journey.get();
//			List<Path> list = pathService.pathRepo.findByDateTimeBetween(
//					j.date, j.date.plusDays(1));
//			return list;
//		}
//		return new ArrayList<Path>();
	}
	
}
