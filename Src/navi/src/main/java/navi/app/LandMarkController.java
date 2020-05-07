package navi.app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import navi.Packet.ReqLandMarkAddr;
import navi.Packet.ReqLandMarkCommentUpdate;
import navi.model.LandMark;
import navi.service.LandMarkService;

@Controller
public class LandMarkController {

	@Autowired
	LandMarkService landMarkService;

	
	
	// landmark address upload page
	@RequestMapping("landMarkAddr")
	String landMarkAddrUpload(Model model) 
	{
		List<LandMark> landMarks = landMarkService.findAll();
		model.addAttribute("landMarks", landMarks);
		return "landmarkaddr";
	}
	
	
	// landmark address upload request
	@RequestMapping(value = "landMarkAddrUpload", method = RequestMethod.PUT)
	@ResponseBody String landMarkAddrUpload(@RequestBody ReqLandMarkAddr data) 
	{
		Optional<LandMark> landMark = landMarkService.findById(data.id);
		if (landMark.get() != null)
		{
			if (data.addr != null)
			{
				landMark.get().address = data.addr;
				landMarkService.save(landMark.get());
			}
		}
		return "ok";
	}
	
	
	// request landmark data
	// url : landMarkInfo?user=1&id=1
	@RequestMapping(value = "landMarkInfo", method = RequestMethod.GET)
	@ResponseBody LandMark landMarkInfo(@RequestParam("user") Optional<Integer> userId,
			@RequestParam("id") Optional<Long> landMarkId) 
	{
		Optional<LandMark> landMark = landMarkService.findById(landMarkId.get());
		return landMark.get();
	}
	
	
	// request landmark comment update
	// url : landMarkCommentUpload
	@RequestMapping(value = "landMarkCommentUpload", method = RequestMethod.PUT)
	@ResponseBody String landMarkCommentUpload(@RequestBody ReqLandMarkCommentUpdate data) 
	{
		Optional<LandMark> landMark = landMarkService.findById(data.id);
		if (landMark.get() != null)
		{
			landMark.get().comment = data.comment;
			landMarkService.save(landMark.get());
		}
		return "Ok";
	}
	

	// request landmark delete
	// url : landMarkDelete?user=1&id=10
	@RequestMapping(value = "landMarkDelete", method = RequestMethod.GET)
	@ResponseBody String landMarkDelete(@RequestParam("user") Optional<Integer> userId, 
			@RequestParam("id") Optional<Long> landMarkId)
	{
		if (landMarkId.get() != null)
			landMarkService.removeLandMark(landMarkId.get());
		return "Ok";
	}

	
	// request landmark add
	// url : landMarkAdd
	@RequestMapping(value = "landMarkAdd", method = RequestMethod.POST)
	@ResponseBody String landMarkAdd(@RequestBody LandMark data)
	{
		LandMark landMark = new LandMark();
		landMark.userId = data.userId;
		landMark.comment = data.comment;
		landMark.address = data.address;
		landMark.dateTime = LocalDateTime.now();
		landMark.lon = data.lon;
		landMark.lat = data.lat;
		landMarkService.save(landMark);		
		return "Ok";
	}

}
