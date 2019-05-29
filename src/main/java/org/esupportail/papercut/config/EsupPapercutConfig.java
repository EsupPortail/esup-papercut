package org.esupportail.papercut.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="esup")
@PropertySource(value = "esup-papercut.properties", encoding = "UTF-8")
public class EsupPapercutConfig {

	String defaultContext = null;
	
	Map<String, EsupPapercutContext> contexts;

	public Map<String, EsupPapercutContext> getContexts() {
		return contexts;
	}

	public void setContexts(Map<String, EsupPapercutContext> contexts) {
		this.contexts = contexts;
	}

	public void setDefaultContext(String defaultContext) {
		this.defaultContext = defaultContext;
	}

	public String getDefaultContext() {
		if(defaultContext != null) {
			return defaultContext;
		} else {
			return contexts.keySet().iterator().next();
		}
	}

	public EsupPapercutContext getContext(String contextKey) {
		return contexts.get(contextKey);
	}
	
}

