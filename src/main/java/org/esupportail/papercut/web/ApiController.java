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

import flexjson.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.AdminProfilDaoService;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.AdminProfil;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.security.ContextHelper;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.esupportail.papercut.services.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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



