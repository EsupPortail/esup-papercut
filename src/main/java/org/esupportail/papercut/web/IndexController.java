package org.esupportail.papercut.web;

import javax.annotation.Resource;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
		
	@Resource
	EsupPapercutConfig config;

	@GetMapping("/")
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("auth", auth);
		String defaultContext = config.getDefaultContext();
		return "redirect:/" + defaultContext;
	}
	
	@GetMapping("/{papercutContext}")
    public String papercutContext(@PathVariable String papercutContext) {	
    	return "redirect:/" + papercutContext + "/user";
    }

}
