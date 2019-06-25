package org.esupportail.papercut.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EsupErrorController extends BasicErrorController {

	public EsupErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
	}

	protected boolean isIncludeStackTrace(HttpServletRequest request,
			MediaType produces) {
		return WebUtils.isAdmin() || WebUtils.isManager();
	}
	
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		if(response.getStatus() == HttpStatus.NOT_FOUND.value()) {
			modelAndView.setViewName("error-404");
		} else if(response.getStatus() == HttpStatus.FORBIDDEN.value()) {
			modelAndView.setViewName("error-403");
		} else {
			 modelAndView = super.errorHtml(request, response);
		}
		return modelAndView;
	}
}
