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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.esupportail.papercut.security.ContextUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebUtils {

	public static String getContext(HttpServletRequest request) {
		String path = request.getServletPath();
		if("/error".equals(path)) {
			path = (String)request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		}
		String papercutContext = path.replaceFirst("/([^/]*).*", "$1");
		return papercutContext;
	}
	
	public static List<String> availableContexts() {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 if(auth != null && auth.getPrincipal() instanceof ContextUserDetails) {
			 ContextUserDetails userDetails = (ContextUserDetails)auth.getPrincipal();
			 return userDetails.getAvailableContexts();
		 } else {
			 return new ArrayList<String>();
		 }
	}

	public static boolean isUser() {
		return hasRole("ROLE_USER");
	}
	
	public static boolean isManager() {
		return hasRole("ROLE_MANAGER");
	}
	
	public static boolean isAdmin() {
		 return hasRole("ROLE_ADMIN");
	}
	
	private static boolean hasRole(String roleName) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 return auth!=null && auth.getAuthorities() != null && auth.getAuthorities().contains(new SimpleGrantedAuthority(roleName));
	}
}
