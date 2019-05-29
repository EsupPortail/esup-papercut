package org.esupportail.papercut.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
	
	final List<GrantedAuthority> defaultAuthorities = Arrays.asList(new GrantedAuthority[] {new SimpleGrantedAuthority("ROLE_NONE")});

	public ContextUserDetails(String username, Map<String, Set<GrantedAuthority>> contextAuthorities) {
		this.username = username;
		this.contextAuthorities = contextAuthorities;
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
		return username;
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

}
