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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import navi.model.JourneyDate;
import navi.service.JourneyDateService;
import navi.service.PathService;


@Controller
public class PathController {
	
	@Autowired
	JourneyDateService journeyDateService;
	
	final int showPageList = 10;
	final int showPageCount = 5;
	

	// default path-list page
	@RequestMapping("pathForm")
	String listPath(Model model) {
		
		List<JourneyDate> journeys = journeyDateService.journeyDateRepo.findAllByOrderByTimeIdDesc();
		model.addAttribute("journeys", journeys);
		
		return "pathForm";
	}

	
	// path-page page /pathPage?page=1&size=10
	@RequestMapping("pathPage")
	String pathPage(
			@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size,
			Model model) 
	{		
		int totalPages = (int)(journeyDateService.getCount() / showPageList) + 1;
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
	
}
