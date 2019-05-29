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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Group2UserRoleService {

	protected GroupService groupService;

	protected Map<String, String> mappingGroupesRoles;
	
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setMappingGroupesRoles(Map<String, String> mappingGroupesRoles) {
		this.mappingGroupesRoles = mappingGroupesRoles;
	}
	
	public Set<String> getRoles(String eppn) {
		Set<String> roles = new HashSet<String>();
		for(String groupName : groupService.getGroups(eppn)) {
			if(mappingGroupesRoles.containsKey(groupName)) {
				String role = mappingGroupesRoles.get(groupName);
				roles.add(role);
			}
		}
		return roles;
	}
	
}