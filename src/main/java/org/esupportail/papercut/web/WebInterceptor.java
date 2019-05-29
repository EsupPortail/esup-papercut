package org.esupportail.papercut.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class WebInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);

		if(modelAndView != null) {
			boolean isViewObject = modelAndView.getView() == null;
			boolean isRedirectView = !isViewObject && modelAndView.getView() instanceof RedirectView;
			boolean viewNameStartsWithRedirect = isViewObject && modelAndView.getViewName().startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
	
			if (!isRedirectView && !viewNameStartsWithRedirect) {
				String path = request.getServletPath();
				String papercutContext = path.replaceFirst("/([^/]*).*", "$1");
				modelAndView.addObject("pContext", papercutContext);
			}
		}

	}

}
