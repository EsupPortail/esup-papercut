/**
 * Licensed to EsupPortail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * EsupPortail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.papercut.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.papercut.security.ContextHelper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class WebInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		String context = WebUtils.getContext(request);
		ContextHelper.setCurrentContext(context);
		
		super.postHandle(request, response, handler, modelAndView);

		if(modelAndView != null) {
			boolean isViewObject = modelAndView.getView() == null;
			boolean isRedirectView = !isViewObject && modelAndView.getView() instanceof RedirectView;
			boolean viewNameStartsWithRedirect = isViewObject && modelAndView.getViewName().startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
	
			if (!isRedirectView && !viewNameStartsWithRedirect) {
				modelAndView.addObject("pContext", ContextHelper.getCurrentContext());
				modelAndView.addObject("isAdmin", WebUtils.isAdmin());
				modelAndView.addObject("isManager", WebUtils.isManager());
				modelAndView.addObject("availableContexts", WebUtils.availableContexts());
			}
		}

	}

}

