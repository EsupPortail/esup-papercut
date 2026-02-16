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
package org.esupportail.papercut.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDef(name = "contextFilter", parameters = {@ParamDef(name = "papercutContext", type = String.class)})
@Filter(name = "contextFilter", condition = "papercut_context = :papercutContext")
public class AdminProfil implements ContextSupport {

    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "papercut_admin_profil_seq_gen")
	@SequenceGenerator(name = "papercut_admin_profil_seq_gen", sequenceName = "hibernate_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    private String papercutContext;

	private String uid;

    private String apiJsonHashKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPapercutContext() {
		return papercutContext;
	}

	@Override
	public void setPapercutContext(String papercutContext) {
		this.papercutContext = papercutContext;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getApiJsonHashKey() {
		return apiJsonHashKey;
	}

	public void setApiJsonHashKey(String apiJsonHashKey) {
		this.apiJsonHashKey = apiJsonHashKey;
	}
}
