package org.esupportail.papercut.web;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	public static String getContext(HttpServletRequest request) {
		String path = request.getServletPath();
		String papercutContext = path.replaceFirst("/([^/]*).*", "$1");
		return papercutContext;
	}

}
