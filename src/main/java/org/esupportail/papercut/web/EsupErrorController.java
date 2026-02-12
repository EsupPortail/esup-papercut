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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
