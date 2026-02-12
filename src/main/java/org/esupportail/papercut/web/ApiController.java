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
package org.esupportail.papercut.web;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.AdminProfilDaoService;
import org.esupportail.papercut.domain.AdminProfil;
import org.esupportail.papercut.security.ContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/{papercutContext}/api")
public class ApiController {

    final static Logger log = LoggerFactory.getLogger(ApiController.class);

	@Resource
	EsupPapercutConfig config;

	@Autowired
	private AdminProfilDaoService adminProfilDaoService;

	@Resource
	AdminController adminController;

	@RequestMapping("/csv-online")
	@Transactional
	public void getCsv(@RequestHeader(value = "x-APIKey", required = false) String hashKey, HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.trace("Call of /api/csv-online with hashKey {}", hashKey);
		AdminProfil profil = adminProfilDaoService.findByApiJsonHashKey(hashKey);
		EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		if(context==null || !context.getExportPublicHashEnabled() || profil==null) {
			log.warn("Call of /api/csv-online from IP {} with bad hashKey", request.getRemoteAddr(), hashKey);
			response.setStatus(403);
		} else {
			log.info("Call of /api/csv-online from IP {} with adminProfil {} OK", request.getRemoteAddr(), profil.getUid());
			adminController.getCsv(response);
		}
	}

}    



