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
		Map<String, String> contextUids = new HashMap<String, String>(); 
		Map<String, String> contextEmails = new HashMap<String, String>(); 
		List<String> availableContexts = new ArrayList<String>(); 
		
		
		for(String contextKey : config.getContexts().keySet()) {
			
			EsupPapercutContext context = config.getContext(contextKey);
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			
			for(String adminRuleKey : context.getEsupPapercutCasAttributeRuleAdmin().keySet()) {
				for(String attributeValue : getAttrValuesAsList(assertion.getPrincipal().getAttributes().get(adminRuleKey))) {
					String adminRuleRegex =  context.getEsupPapercutCasAttributeRuleAdmin().get(adminRuleKey);
					if(attributeValue.matches(adminRuleRegex)) {
						authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
						break;
					}
				}
			}
			
			for(String managerRuleKey : context.getEsupPapercutCasAttributeRuleManager().keySet()) {
				for(String attributeValue : getAttrValuesAsList(assertion.getPrincipal().getAttributes().get(managerRuleKey))) {
					String managerRuleRegex =  context.getEsupPapercutCasAttributeRuleManager().get(managerRuleKey);
					if(attributeValue.matches(managerRuleRegex)) {
						authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
						break;
					}
				}
			}

			for(String userRuleKey : context.getEsupPapercutCasAttributeRuleUser().keySet()) {
				for(String attributeValue : getAttrValuesAsList(assertion.getPrincipal().getAttributes().get(userRuleKey))) {
					String userRuleRegex =  context.getEsupPapercutCasAttributeRuleUser().get(userRuleKey);
					if(attributeValue.matches(userRuleRegex)) {
						authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
						break;
					}
				}
			}
			
			if(!authorities.isEmpty()) {
				availableContexts.add(contextKey);
			}
			
			contextAuthorities.put(contextKey, authorities);

			if(context.getPapercutUserUidAttr() != null && !context.getPapercutUserUidAttr().isEmpty() && assertion.getPrincipal().getAttributes().get(context.getPapercutUserUidAttr()) != null) {
				contextUids.put(contextKey, (String)assertion.getPrincipal().getAttributes().get(context.getPapercutUserUidAttr()));
			}
			if(context.getUserEmail() != null && !context.getUserEmail().isEmpty() && assertion.getPrincipal().getAttributes().get(context.getUserEmail()) != null) {
				contextEmails.put(contextKey, (String)assertion.getPrincipal().getAttributes().get(context.getUserEmail()));
			}
			
		}
		
		ContextUserDetails contextUserDetails =
                new ContextUserDetails(assertion.getPrincipal().getName(), contextAuthorities, contextUids, contextEmails, availableContexts);

		return contextUserDetails;
	}

	private List<String> getAttrValuesAsList(Object attrValues) {
		if(attrValues == null) {
			return new ArrayList<String>(); 
		} else if(attrValues instanceof List) {
			return (List<String>)attrValues;
		} else {
			List<String> attrs = new ArrayList<String>();
			attrs.add((String) attrValues);
			return attrs;
		} 
	}

}


