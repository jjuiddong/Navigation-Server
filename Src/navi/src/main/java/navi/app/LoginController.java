package navi.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@RequestMapping("login")
	String loginForm() {
		return "loginForm";
	}
	
}
