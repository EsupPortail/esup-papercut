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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.esupportail.papercut.web.WebUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ContextUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	String username;
	
	Map<String, Set<GrantedAuthority>> contextAuthorities = new HashMap<String, Set<GrantedAuthority>>();
	Map<String, String> contextUids = new HashMap<String, String>(); 
	Map<String, String> contextEmails = new HashMap<String, String>(); 
	
	final List<GrantedAuthority> defaultAuthorities = Arrays.asList(new GrantedAuthority[] {new SimpleGrantedAuthority("ROLE_NONE")});
	
	List<String> availableContexts;

	public ContextUserDetails(String username, Map<String, Set<GrantedAuthority>> contextAuthorities, Map<String, String> contextUids, Map<String, String> contextEmails, List<String> availableContexts) {
		this.username = username;
		this.contextAuthorities = contextAuthorities;
		this.contextUids = contextUids;
		this.contextEmails = contextEmails;
		this.availableContexts = availableContexts;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
	        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
	        String papercutContext = WebUtils.getContext(request);
	        if(contextAuthorities.get(papercutContext) != null && !contextAuthorities.get(papercutContext).isEmpty()) {
	        	return contextAuthorities.get(papercutContext);
	        }
	    }
		return defaultAuthorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
	        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
	        String papercutContext = WebUtils.getContext(request);
	        if(contextUids.get(papercutContext) != null && !contextUids.get(papercutContext).isEmpty()) {
	        	return contextUids.get(papercutContext);
	        }
	    }
		return username;
	}
	
	public String getEmail() {
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
	        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
	        String papercutContext = WebUtils.getContext(request);
	        if(contextEmails.get(papercutContext) != null && !contextEmails.get(papercutContext).isEmpty()) {
	        	return contextEmails.get(papercutContext);
	        }
	    }
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<String> getAvailableContexts() {
		return availableContexts;
	}

}
