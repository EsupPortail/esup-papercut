package org.esupportail.papercut.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ContextUserDetailsService extends AbstractCasAssertionUserDetailsService {

	EsupPapercutConfig config;
	
	public ContextUserDetailsService(EsupPapercutConfig config) {
		this.config = config;
	}

	@Override
	protected UserDetails loadUserDetails(Assertion assertion) {
		
		Map<String, Set<GrantedAuthority>> contextAuthorities = new HashMap<String, Set<GrantedAuthority>>();
		List<String> availableContexts = new ArrayList<String>(); 
		
		List<String> groups = (List<String>) assertion.getPrincipal().getAttributes().get("memberOf");
		
		for(String contextKey : config.getContexts().keySet()) {
			EsupPapercutContext context = config.getContext(contextKey);
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			if(groups.contains(context.getEsupPapercutAdminAttributeValue())) {
				authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}
			if(groups.contains(context.getEsupPapercutManagerAttributeValue())) {
				authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
			}
			if(groups.contains(context.getEsupPapercutUserAttributeValue())) {
				authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			}
			if(!authorities.isEmpty()) {
				availableContexts.add(contextKey);
			}
			contextAuthorities.put(contextKey, authorities);
		}
		
		ContextUserDetails contextUserDetails =
                new ContextUserDetails(assertion.getPrincipal().getName(), contextAuthorities, availableContexts);

		return contextUserDetails;
	}

}
