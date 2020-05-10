//
// pathForm, pathPage web page control
//
//

package navi.app;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import navi.Packet.ReqCheckData;
import navi.model.JourneyDate;
import navi.model.LandMark;
import navi.service.JourneyDateService;
import navi.service.LandMarkService;
import navi.user.NaviUser;


@Controller
public class PathController {
	
	@Autowired
	JourneyDateService journeyDateService;
	
	@Autowired
	LandMarkService landMarkService;
	
	final int showListCount = 10;
	final int showPageCount = 5;
	
	// default path-list page
	@RequestMapping("pathForm")
	String listPath(
			@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size,
			Model model) {
		
		List<JourneyDate> journeys = journeyDateService.findAllBySort();
		model.addAttribute("journeys", journeys);
		
	  	int currentPage = page.orElse(1);
		int first = (currentPage-1) * showListCount;
		int last = first + showListCount;
		last = (journeys.size() < last)? journeys.size() - 1 : last;
	  	int totalPages = (journeys.size() / showListCount) + 1;
        int startPage = ((currentPage-1) / showPageCount * showPageCount) + 1;
       	int endPage = (startPage + (showPageCount-1) > totalPages)? totalPages 
       			: startPage + (showPageCount-1);

		model.addAttribute("currentPage", 1);
		model.addAttribute("first", first);
		model.addAttribute("last", last);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);		
		
		return "pathForm";
	}
	
	
	// path-page page /pathPage?page=1&size=10&page2=1
	@RequestMapping("pathPage")
	String pathPage(
			@AuthenticationPrincipal NaviUser naviUser,
			@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("page2") Optional<Integer> page2, 
			Model model) 
	{
		// journey_date
		{
			int totalPages = (int)(journeyDateService.getCount() / showListCount) + 1;
			int currentPage = page.orElse(1);
	        currentPage = (currentPage < 1)? 1 
	        		: (currentPage > totalPages)? totalPages : currentPage;
	        int pageSize = size.orElse(10);

			Page<JourneyDate> journeys = journeyDateService.getJourneyDateList(
					PageRequest.of(currentPage - 1, pageSize));
			model.addAttribute("page", journeys);
			model.addAttribute("journeys", journeys.getContent());
			model.addAttribute("detail", new JourneyDate()); // temporal data

	        int curTotalPages = journeys.getTotalPages();
	        if (curTotalPages > 0) {
	        	int startPage = ((currentPage-1) / showPageCount * showPageCount) + 1;
	        	int endPage = (startPage + (showPageCount-1) > curTotalPages)? curTotalPages : startPage + (showPageCount-1);
	            List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
		}
		
		// landmark
		{
			int totalPages = (int)(landMarkService.getCount() / showListCount) + 1;
			int currentPage = page2.orElse(1);
	        currentPage = (currentPage < 1)? 1 
	        		: (currentPage > totalPages)? totalPages : currentPage;
	        int pageSize = size.orElse(10);

			Page<LandMark> landMarks = landMarkService.findPage(
					PageRequest.of(currentPage - 1, pageSize));
			model.addAttribute("page2", landMarks);
			model.addAttribute("landMarks", landMarks.getContent());
			
	        int curTotalPages = landMarks.getTotalPages();
	        if (curTotalPages > 0) {
	        	int startPage = ((currentPage-1) / showPageCount * showPageCount) + 1;
	        	int endPage = (startPage + (showPageCount-1) > curTotalPages)? curTotalPages : startPage + (showPageCount-1);
	            List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers2", pageNumbers);
	        }
		}
		
		return "pathPage";
	}
	

	// live-page page /live
	@RequestMapping("live")
	String live(Model model) 
	{
		return "live";
	}
	
	
	// detail-page page /live
	@RequestMapping("detail")
	String detail(Model model) 
	{		
		List<JourneyDate> journeys = journeyDateService.findAllBySort();
		model.addAttribute("journeys", journeys);
		return "detail";
	}
	
	
	// path show/hide check
	@RequestMapping(value = "pathShow", method = RequestMethod.PUT)
	@ResponseBody String pathShow(@RequestBody ReqCheckData chkData) 
	{
		Optional<JourneyDate> journey = journeyDateService.findById(chkData.journeyId);
		if (journey.get() != null)
		{
			journey.get().checked = chkData.checked;
			journeyDateService.save(journey.get());			
		}
		return "ok";
	}


}
