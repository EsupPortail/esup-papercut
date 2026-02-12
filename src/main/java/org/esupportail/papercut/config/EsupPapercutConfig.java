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
package org.esupportail.papercut.config;

import java.util.List;
import java.util.Map;

import org.esupportail.papercut.web.WebUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="esup")
@PropertySource(value = "classpath:/esup-papercut.properties", encoding = "UTF-8")
@EnableMethodSecurity
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
		List<String> availableContexts = WebUtils.availableContexts();
		if(!availableContexts.isEmpty()) {
			if(defaultContext != null && availableContexts.contains(defaultContext)) {
				return defaultContext;
			} else {
				return availableContexts.get(0);
			}
		}
		return defaultContext;
	}

	public EsupPapercutContext getContext(String contextKey) {
		return contexts.get(contextKey);
	}
	
}

