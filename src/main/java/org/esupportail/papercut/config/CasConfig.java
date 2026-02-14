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

import org.apereo.cas.client.session.SingleSignOutFilter;
import org.apereo.cas.client.session.SingleSignOutHttpSessionListener;
import org.apereo.cas.client.validation.Cas20ServiceTicketValidator;
import org.apereo.cas.client.validation.TicketValidator;
import org.esupportail.papercut.security.ContextCasAuthenticationProvider;
import org.esupportail.papercut.security.ContextUserDetailsService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@ConfigurationProperties(prefix="cas")
public class CasConfig {

	String url;

	String service;

	String key;

	public String getUrl() {
		return url;
	}

	public String getService() {
		return service;
	}

	public String getKey() {
		return key;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLogoutUrl() {
		String encodedService = URLEncoder.encode(service, StandardCharsets.UTF_8);
		return url + "/logout?service=" + encodedService;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(service + "/login");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	@Primary
	public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties sP) {
		CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
		entryPoint.setLoginUrl(url + "/login");
		entryPoint.setServiceProperties(sP);
		return entryPoint;
	}

	@Bean
	public TicketValidator ticketValidator() {
		return new Cas20ServiceTicketValidator(url);
	}

	@Bean
	public ContextCasAuthenticationProvider casAuthenticationProvider(EsupPapercutConfig config, ServiceProperties serviceProperties, TicketValidator ticketValidator) {
		ContextCasAuthenticationProvider provider = new ContextCasAuthenticationProvider();
		provider.setServiceProperties(serviceProperties);
		provider.setTicketValidator(ticketValidator);
		provider.setAuthenticationUserDetailsService(new ContextUserDetailsService(config));
		provider.setKey(key);
		return provider;
	}


	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

	@Bean
	public LogoutFilter logoutFilter(SecurityContextLogoutHandler securityContextLogoutHandler) {
        return new LogoutFilter(getLogoutUrl(), securityContextLogoutHandler);
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}

	@Bean
	public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener() {
		return new SingleSignOutHttpSessionListener();
	}

	/**
	 * Désactive l'enregistrement automatique du SingleSignOutFilter pour éviter qu'il soit appelé deux fois.
	 * Le filtre est ajouté manuellement dans la chaîne de filtres de sécurité.
	 */
	@Bean
	public FilterRegistrationBean<SingleSignOutFilter> singleSignOutFilterRegistration(SingleSignOutFilter filter) {
		FilterRegistrationBean<SingleSignOutFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}

	/**
	 * Désactive l'enregistrement automatique du LogoutFilter pour éviter qu'il soit appelé deux fois.
	 * Le filtre est ajouté manuellement dans la chaîne de filtres de sécurité.
	 */
	@Bean
	public FilterRegistrationBean<LogoutFilter> logoutFilterRegistration(LogoutFilter filter) {
		FilterRegistrationBean<LogoutFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}
}
