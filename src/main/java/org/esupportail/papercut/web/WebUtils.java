package org.esupportail.papercut.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebUtils {

	public static String getContext(HttpServletRequest request) {
		String path = request.getServletPath();
		String papercutContext = path.replaceFirst("/([^/]*).*", "$1");
		return papercutContext;
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
