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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import navi.model.CheckData;
import navi.model.JourneyDate;
import navi.service.JourneyDateService;
import navi.service.PathService;


@Controller
public class PathController {
	
	@Autowired
	JourneyDateService journeyDateService;
	
	final int showListCount = 10;
	final int showPageCount = 5;
	

	// default path-list page
//	@RequestMapping("pathForm")
//	String listPath(Model model) {
//		
//		List<JourneyDate> journeys = journeyDateService.findAllBySort();
//		model.addAttribute("journeys", journeys);
//		
//	  	int currentPage = 1;
//		int first = 0;
//		int last = (journeys.size() > showListCount)? (showListCount-1) : journeys.size() - 1;
//	  	int totalPages = (journeys.size() / showListCount) + 1;
//        int startPage = ((currentPage-1) / showPageCount * showPageCount) + 1;
//       	int endPage = (startPage + (showPageCount-1) > totalPages)? totalPages 
//       			: startPage + (showPageCount-1);
//
//		model.addAttribute("currentPage", 1);
//		model.addAttribute("first", first);
//		model.addAttribute("last", last);
//		model.addAttribute("totalPages", totalPages);
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("endPage", endPage);		
//		
//		return "pathForm";
//	}
	
	
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
	
	
	// path-page page /pathPage?page=1&size=10
	@RequestMapping("pathPage")
	String pathPage(
			@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size,
			Model model) 
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

        int curTotalPages = journeys.getTotalPages();
        if (curTotalPages > 0) {
        	int startPage = ((currentPage-1) / showPageCount * showPageCount) + 1;
        	int endPage = (startPage + (showPageCount-1) > curTotalPages)? curTotalPages : startPage + (showPageCount-1);
            List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		
		return "pathPage";
	}
	

	// live-page page /live
	@RequestMapping("live")
	String live(Model model) 
	{		
		return "live";
	}
	
	// path show/hide check
	@RequestMapping(value = "pathShow", method = RequestMethod.PUT)
	@ResponseBody String pathShow(@RequestBody CheckData chkData) 
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
