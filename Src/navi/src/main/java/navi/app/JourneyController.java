package navi.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import navi.Packet.ReqDetailJourneyInfo;
import navi.model.JourneyDate;
import navi.model.JourneyInfo;
import navi.model.Path;
import navi.service.JourneyDateService;
import navi.service.PathService;


@Controller
public class JourneyController {

	@Autowired
	JourneyDateService journeyDateService;

	@Autowired
	PathService pathService;
	

	// show journey detail info 
	@RequestMapping(value = "showDetail", method = RequestMethod.PUT)
	@ResponseBody JourneyDate showDetail(@RequestBody ReqDetailJourneyInfo journeyInfo,
			Model model) 
	{
		Optional<JourneyDate> journey = journeyDateService.findById(journeyInfo.journeyId);
		return journey.get();
	}
	
	
	// journey popup page /journeyPopup?journeyId=1&journeyTimeId=10
	@RequestMapping(value = "journeyPopup/{journeyId}/{journeyTimeId}", method = RequestMethod.GET)
	public String journeyPopup( @PathVariable Long journeyId, 
			@PathVariable Long journeyTimeId,
			Model model )
	{
		return "journeyPopup";
	}
	
	
	// request today journey data
	@RequestMapping(value = "todayJourneyInfo", method = RequestMethod.GET)
	@ResponseBody List<JourneyDate> todayJourneyInfo(@RequestParam("user") Optional<Integer> userId) 
	{
		//LocalDateTime now = LocalDateTime.of(2020, 4, 24, 0, 0, 0);
		LocalDateTime now = LocalDateTime.now();
		
		LocalDate date0 = now.toLocalDate();
		LocalDate date1 = date0.plusDays(1);
		List<JourneyDate> list = journeyDateService.findByDate(date0, date1);
		return list;
	}
	
	// request journey data
	// ex) journeyInfo?journeyId=0
	@RequestMapping(value = "journeyDetail", method = RequestMethod.GET)
	@ResponseBody JourneyInfo journeyDetail(
			@RequestParam("journeyId") Optional<Long> journeyId) 
	{
		Optional<JourneyDate> journey = journeyDateService.findById(journeyId.get());
		if (journey.get() == null)
			return null;

		List<Path> path = pathService.pathRepo.findByJourneyTimeId(journey.get().timeId);

		JourneyInfo journeyInfo = new JourneyInfo();
		journeyInfo.id = journey.get().id;
		journeyInfo.date = journey.get().date;
		journeyInfo.timeId = journey.get().timeId;
		journeyInfo.journeyTime = journey.get().journeyTime;
		journeyInfo.distance = journey.get().distance;
		journeyInfo.userId = journey.get().userId;
		
		if (!path.isEmpty())
		{
			journeyInfo.startTime = path.get(0).dateTime;
			journeyInfo.endTime = path.get(path.size()-1).dateTime;
			journeyInfo.updateCount = path.size();
		}
		
		return journeyInfo;
	}	
	
}
