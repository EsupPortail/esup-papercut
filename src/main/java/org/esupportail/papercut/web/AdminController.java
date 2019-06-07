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
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/{papercutContext}/admin")
public class AdminController {
	

    final static Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Resource 
	EsupPaperCutService esupPaperCutService;
	
	@Resource
	EsupPapercutConfig config;
	
	@Autowired
	PapercutDaoService papercutDaoService;
	
	@Autowired
	StatsService statsService;

    @GetMapping(produces = "text/html")
    public String historyList(@PageableDefault(size = 10, direction = Direction.DESC, sort = "transactionDate") Pageable pageable, 
    		Model uiModel) { 	
        uiModel.addAttribute("pageLogs", papercutDaoService.findAllPayboxPapercutTransactionLogs(pageable));
        uiModel.addAttribute("active", "admin"); 	
        return "history";
    }
	
    
    @GetMapping(produces = "text/html", params = {"id"})
    public String viewTransactionLog(@RequestParam Long id, Model uiModel) {
    	uiModel.addAttribute("plog", papercutDaoService.findById(id).get());
    	uiModel.addAttribute("itemId", id);
    	uiModel.addAttribute("active", "admin"); 	
        return "show-transactionlog";
    }
    
    @Transactional
    @PostMapping(value = "archive")                                                                                                                                          
    public String archive(@RequestParam Long id, @PathVariable String papercutContext) {

        PayboxPapercutTransactionLog txLog =  papercutDaoService.findById(id).get();
        txLog.setArchived(true);
       
        return "redirect:/" + papercutContext + "/admin?id=" + id;
    }


    @RequestMapping("/csv")
    @Transactional
    public void getCsv(HttpServletResponse response) throws IOException {

    	StopWatch stopWatch = new StopWatch("Stream - build CSV and send it");
    	stopWatch.start();

    	response.setContentType("text/csv");
    	response.setCharacterEncoding("utf-8");   
    	response.setHeader("Content-Disposition","attachment; filename=\"paybox_papercut_transaction_log.csv\"");

    	Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF8");

    	String csv = "Date transaction,uid,montant,ID transaction paybox";
    	writer.write(csv);

    	Page<PayboxPapercutTransactionLog> txLogsPage = papercutDaoService.findAllPayboxPapercutTransactionLogs(PageRequest.of(0, 1000, Sort.by("transactionDate").ascending()));

    	int nbLine = 0;
    	while(true) {
    		for(PayboxPapercutTransactionLog txLog : txLogsPage.getContent()) {
    			csv = "";
    			csv = csv + "\r\n";
    			csv = csv + txLog.getTransactionDate() + ",";
    			csv = csv + txLog.getUid() + ",";
    			csv = csv + txLog.getMontant() + ",";
    			csv = csv + txLog.getIdtrans();
    			writer.write(csv);
    			nbLine++;
    		} 
    		if(txLogsPage.hasNext()) {
    			Pageable pageable = txLogsPage.nextPageable();
    			txLogsPage = papercutDaoService.findAllPayboxPapercutTransactionLogs(pageable);
    		} else {
    			break;
    		}	
    	}
    	stopWatch.stop();	    	
    	log.info("CSV of " + nbLine + " lines sent in " + stopWatch.getTotalTimeSeconds() + " sec.");

    	writer.close();
    }

    @RequestMapping(value="/stats")
    public String getStats(Model uiModel) {
    	uiModel.addAttribute("active", "stats"); 
    	return "stats";
    }

    @RequestMapping(value="/statsPapercut")
    public void getStatsInfos(HttpServletResponse response) throws IOException, ServletRequestBindingException {
		
    	String flexJsonString = "Aucune statistique à récupérer";
    	
    	EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		
    	try {

    		JSONSerializer serializer = new JSONSerializer();
    		flexJsonString = serializer.deepSerialize(statsService.getStatsPapercut(context));

    	} catch (Exception e) {
    		log.warn("Impossible de récupérer les statistiques", e);
    	}

    	InputStream jsonStream = IOUtils.toInputStream(flexJsonString, "utf-8");

    	response.setContentType("application/json");
    	response.setCharacterEncoding("utf-8");   

    	FileCopyUtils.copy(jsonStream, response.getOutputStream());
    }

}    



