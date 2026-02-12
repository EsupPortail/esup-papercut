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
package org.esupportail.papercut.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.esupportail.papercut.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class ContextFilter  implements Filter {

	Logger log = LoggerFactory.getLogger(ContextFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.trace("ContextFilter initialized");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String context = WebUtils.getContext(request);
		if (StringUtils.hasLength(context)) {
			ContextHelper.setCurrentContext(context);
			log.trace("ContextFilter set context: {} for url {}", context, request.getRequestURI());
		} 
		filterChain.doFilter(servletRequest, servletResponse);
		log.trace("ContextFilter finished - clearing context");
		ContextHelper.clear();
	}

}
