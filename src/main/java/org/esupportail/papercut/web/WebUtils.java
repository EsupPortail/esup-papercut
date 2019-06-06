package org.esupportail.papercut.web;

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
		 ContextUserDetails userDetails = (ContextUserDetails)auth.getPrincipal();
		 return userDetails.getAvailableContexts();
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
		 return auth.getAuthorities().contains(new SimpleGrantedAuthority(roleName));
	}
}
