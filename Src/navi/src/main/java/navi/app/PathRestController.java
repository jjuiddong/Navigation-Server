package navi.app;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import navi.model.JourneyDate;
import navi.model.Path;
import navi.service.JourneyDateService;
import navi.service.PathService;


@RestController
//@RequestMapping("path")
public class PathRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(PathRestController.class);

	@Autowired
	JourneyDateService journeyDateService;
	
	@Autowired
	PathService pathService;
	
	private LocalDateTime userRefreshTime;
	
	
	@RequestMapping(value = "path/{journeyId}/{journeyTimeId}", method = RequestMethod.GET)
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
	
	
	// ex) pathInit?userId=1
	@RequestMapping(value = "pathInit", method = RequestMethod.GET)
	public @ResponseBody List<Path> pathInit(
			@RequestParam("userId") Optional<Long> userId) 
	{
//		LocalDateTime now = LocalDateTime.now();
//		userRefreshTime = LocalDateTime.now();
//		LocalDateTime date0 = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
//		LocalDateTime date1 = date0.plusDays(1);
//		List<Path> list = pathService.pathRepo.findByDateTimeBetween(date0, date1);
		
		
		userRefreshTime = LocalDateTime.of(2020, 4, 24, 15, 3, 45);
		LocalDateTime date0 = LocalDateTime.of(2020, 4, 24, 0, 0, 0);
		LocalDateTime date1 = LocalDateTime.of(2020, 4, 24, 15, 3, 45);
		List<Path> list = pathService.pathRepo.findByDateTimeBetween(date0, date1);
		return list;
	}
	
	
	// ex) pathUpdate?userId=1
	@RequestMapping(value = "pathUpdate", method = RequestMethod.GET)
	public @ResponseBody List<Path> pathUpdate(
			@RequestParam("userId") Optional<Long> userId) 
	{
//		LocalDateTime now = LocalDateTime.now();
//		List<Path> list = pathService.pathRepo.findByDateTimeBetween(userRefreshTime, now);
//		userRefreshTime = now;
		
		LocalDateTime date1 = userRefreshTime;		
		date1 = date1.plusMinutes(1);
		List<Path> list = pathService.pathRepo.findByDateTimeBetween(userRefreshTime, date1);
		userRefreshTime = userRefreshTime.plusMinutes(1);
		return list;
	}
	
}
