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
package org.esupportail.papercut.dao;

import org.esupportail.papercut.domain.AdminProfil;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.domain.PayPapercutTransactionLog.PayMode;
import org.esupportail.papercut.security.ContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Random;

@Service
public class AdminProfilDaoService {
	
	@PersistenceContext
	public EntityManager entityManager;


	@Autowired
	private AdminProfilRepository adminProfilRepository;

	Random random = new Random();


	public AdminProfil findByApiJsonHashKey(String hashKey) {
		return adminProfilRepository.findByApiJsonHashKey(hashKey);
	}

	public AdminProfil findOrCreateAdminProfil(String uid) {
		AdminProfil adminProfil = adminProfilRepository.findByUid(uid);
		if(adminProfil == null) {
			adminProfil = new AdminProfil();
			adminProfil.setUid(uid);
			adminProfil.setPapercutContext(ContextHelper.getCurrentContext());
		}
		if(adminProfil.getApiJsonHashKey() == null) {
			adminProfil.setApiJsonHashKey(generateApiJsonHashKey());
		}
		adminProfilRepository.save(adminProfil);
		return adminProfil;
	}

	private String generateApiJsonHashKey() {
		return Long.toHexString(random.nextLong())+Long.toHexString(random.nextLong())+Long.toHexString(random.nextLong());
	}
}

