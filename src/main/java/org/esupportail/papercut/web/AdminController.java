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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.AdminProfilDaoService;
import org.esupportail.papercut.dao.AdminProfilRepository;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.AdminProfil;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.domain.PayPapercutTransactionLogSearch;
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
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/{papercutContext}/admin")
public class AdminController {

    final static Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Resource
	EsupPapercutConfig config;
	
	@Autowired
	PapercutDaoService papercutDaoService;
	
	@Autowired
	StatsService statsService;

	@Autowired
	private AdminProfilDaoService adminProfilDaoService;

	@Autowired
	private ObjectMapper objectMapper;

	@ModelAttribute("active")
	public String getActiveMenu() {
		return "admin";
	}

	@ModelAttribute("papercutContext")
	public String getPapercutContext() {
		return ContextHelper.getCurrentContext();
	}

	@GetMapping(produces = "text/html")
    public String historyList(@PageableDefault(size = 10, direction = Direction.DESC, sort = "transactionDate") Pageable pageable,
    		PayPapercutTransactionLogSearch payPapercutTransactionLogSearch,
    		Model uiModel) {

    	Page<PayPapercutTransactionLog> pageLogs;
    	// Si tous les critères sont vides, on récupère tous les logs
    	if((payPapercutTransactionLogSearch.searchCriteriasAreEmpty())) {
    		pageLogs = papercutDaoService.findAllPayPapercutTransactionLogs(pageable);
    	} else {
    		pageLogs = papercutDaoService.findPayPapercutTransactionLogsByExample(payPapercutTransactionLogSearch, pageable);
    	}

        uiModel.addAttribute("pageLogs", pageLogs);
		uiModel.addAttribute("payPapercutTransactionLogSearch", payPapercutTransactionLogSearch);

		EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		if(context.getExportPublicHashEnabled()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String uid = auth.getName();
			AdminProfil adminProfil = adminProfilDaoService.findOrCreateAdminProfil(uid);
			uiModel.addAttribute("exportPublicHashKey", adminProfil.getApiJsonHashKey());
		}

        return "history";
    }
	
    
    @GetMapping(produces = "text/html", params = {"id"})
    public String viewTransactionLog(@RequestParam Long id, Model uiModel) {
    	uiModel.addAttribute("plog", papercutDaoService.findById(id));
    	uiModel.addAttribute("itemId", id);
        return "show-transactionlog";
    }
    
    @Transactional
    @PostMapping(value = "archive")                                                                                                                                          
    public String archive(@RequestParam Long id, @PathVariable String papercutContext) {
        PayPapercutTransactionLog txLog =  papercutDaoService.findById(id);
        txLog.setArchived(true);
        return "redirect:/" + papercutContext + "/admin?id=" + id;
    }
    
    @Transactional
    @PostMapping(value = "archiveAll")                                                                                                                                          
    public String archiveAll(@PathVariable String papercutContext) {

    	Page<PayPapercutTransactionLog> txLogsPage = papercutDaoService.findAllPayPapercutTransactionLogs(PageRequest.of(0, 1000));
    	while(true) {
    		for(PayPapercutTransactionLog txLog : txLogsPage.getContent()) {
    			txLog.setArchived(true);
    		}
    		if(txLogsPage.hasNext()) {
    			Pageable pageable = txLogsPage.nextPageable();
    			txLogsPage = papercutDaoService.findAllPayPapercutTransactionLogs(pageable);
    		} else {
    			break;
    		}
    	}
        return "redirect:/" + papercutContext + "/admin";
    }

    @RequestMapping("/csv")
    @Transactional
    public void getCsv(HttpServletResponse response) throws IOException {

    	StopWatch stopWatch = new StopWatch("Stream - build CSV and send it");
    	stopWatch.start();

    	response.setContentType("text/csv");
    	response.setCharacterEncoding("utf-8");   
    	response.setHeader("Content-Disposition","attachment; filename=\"pay_papercut_transaction_log.csv\"");

    	Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF8");

    	String csv = "Date transaction,uid,montant,ID transaction,context,payMode";
    	writer.write(csv);

    	Page<PayPapercutTransactionLog> txLogsPage = papercutDaoService.findAllPayPapercutTransactionLogs(PageRequest.of(0, 100000, Sort.by("transactionDate").ascending()));

    	int nbLine = 0;
    	while(true) {
    		for(PayPapercutTransactionLog txLog : txLogsPage.getContent()) {
    			csv = "";
    			csv = csv + "\r\n";
    			csv = csv + txLog.getTransactionDate() + ",";
    			csv = csv + txLog.getUid() + ",";
    			csv = csv + txLog.getMontant() + ",";
    			csv = csv + txLog.getIdtrans() + ",";
    			csv = csv + txLog.getPapercutContext() + ",";
    			csv = csv + txLog.getPayMode();
    			writer.write(csv);
    			nbLine++;
    		} 
    		if(txLogsPage.hasNext()) {
    			Pageable pageable = txLogsPage.nextPageable();
    			txLogsPage = papercutDaoService.findAllPayPapercutTransactionLogs(pageable);
    		} else {
    			break;
    		}	
    	}
    	stopWatch.stop();	    	
    	log.info("CSV of " + nbLine + " lines sent in " + stopWatch.getTotalTimeSeconds() + " sec.");

    	writer.close();
    }

	@RequestMapping("/csv-online")
	@Transactional
	public void getCsv(@RequestHeader("x-APIKey") String hashKey, HttpServletResponse response) throws IOException {
		AdminProfil profil = adminProfilDaoService.findByApiJsonHashKey(hashKey);
		EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		if(context==null || !context.getExportPublicHashEnabled() || profil==null) {
			response.setStatus(403);
		} else {
			getCsv(response);
		}
	}

    @RequestMapping(value="/stats")
    public String getStats(Model uiModel) {
    	uiModel.addAttribute("active", "stats"); 
    	return "stats";
    }

    @RequestMapping(value="/statsPapercut")
    public void getStatsInfos(HttpServletResponse response) throws IOException, ServletRequestBindingException {

    	String jsonString = "Aucune statistique à récupérer";

    	EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());

    	try {
    		jsonString = objectMapper.writeValueAsString(statsService.getStatsPapercut(context));
    	} catch (Exception e) {
    		log.warn("Impossible de récupérer les statistiques", e);
    	}

    	response.setContentType("application/json");
    	response.setCharacterEncoding("utf-8");

    	response.getOutputStream().write(jsonString.getBytes("utf-8"));
    }

}    
