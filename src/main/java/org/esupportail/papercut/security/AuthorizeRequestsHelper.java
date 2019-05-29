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

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

public class AuthorizeRequestsHelper {

	public static void setAuthorizeRequests(HttpSecurity http, String[] nfcWsAccessAuthorizeIps) throws Exception {
		http.logout().logoutSuccessUrl("/").permitAll();
		AccessDeniedHandlerImpl accessDeniedHandlerImpl = new AccessDeniedHandlerImpl();
		accessDeniedHandlerImpl.setErrorPage("/denied");
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl);
		for (String ip : nfcWsAccessAuthorizeIps) {
			http.authorizeRequests().antMatchers("/nfc-ws/**").access("hasIpAddress('"+ ip +"')");			
		}
		http.authorizeRequests()
		.antMatchers("/login/impersonate").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/admin/", "/admin/**").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/manager/", "/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/user/", "/user/**").access("isAuthenticated()")
		.antMatchers("/**").access("permitAll");
		
	}

}
