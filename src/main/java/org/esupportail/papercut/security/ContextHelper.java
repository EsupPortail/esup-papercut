package org.esupportail.papercut.security;

public class ContextHelper {

	private static ThreadLocal<String> currentContext = new ThreadLocal<>();

	public static String getCurrentContext() {
		return currentContext.get();
	}

	public static void setCurrentContext(String context) {
		currentContext.set(context);
	}

	public static void clear() {
		currentContext.set(null);
	}

}
