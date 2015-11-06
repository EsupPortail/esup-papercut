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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.esupportail.papercut.domain.EsupPapercutSessionObject;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.esupportail.papercut.services.StatsService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;

import flexjson.JSONSerializer;

@Controller
@Scope("request")
public class PayBoxResourceController {

	private final Logger log = Logger.getLogger(getClass());
	
	@Resource 
	Map<String, EsupPaperCutService> esupPaperCutServices;
	
	@Resource 
	StatsService statsService;

	@RequestMapping("/csv")
	public void getCsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		
		String sharedSessionId = request.getParameter("sharedSessionId");
		if(sharedSessionId != null) {
			EsupPapercutSessionObject objectShared  = (EsupPapercutSessionObject)session.getAttribute(sharedSessionId);
		
			if(objectShared.isIsAdmin()) {
				String csv = "Date transaction,uid,montant,ID transaction paybox";
		    	
		    	List<PayboxPapercutTransactionLog> txLogs = PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByPaperCutContextEquals(objectShared.getPaperCutContext(), "transactionDate", "asc").getResultList();
		    	for(PayboxPapercutTransactionLog txLog : txLogs) {
		    		csv = csv + "\r\n";
		    		csv = csv + txLog.getTransactionDate() + ",";
		    		csv = csv + txLog.getUid() + ",";
		    		csv = csv + txLog.getMontant() + ",";
		    		csv = csv + txLog.getIdtrans();
		    	} 
		        InputStream csvStream = IOUtils.toInputStream(csv, "utf-8");
		        
		        response.setContentType("text/csv");
		        response.setCharacterEncoding("utf-8");   
		        response.setHeader("Content-Disposition","attachment; filename=\"paybox_papercut_transaction_log.csv\"");
		        
		        FileCopyUtils.copy(csvStream, response.getOutputStream());
			}
    	}
	 }
	
	@RequestMapping(value="/statsPapercut")
	public void getStats(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
	
		HttpSession session = request.getSession();
		
		String sharedSessionId = request.getParameter("sharedSessionId");
		if(sharedSessionId != null) {
			EsupPapercutSessionObject objectShared  = (EsupPapercutSessionObject)session.getAttribute(sharedSessionId);
			if(objectShared.isIsAdmin()) {
				String flexJsonString = "Aucune statistique à récupérer";
				try {
					
					JSONSerializer serializer = new JSONSerializer();
					flexJsonString = serializer.deepSerialize(statsService.getStatsPapercut());
					
				} catch (Exception e) {
					log.warn("Impossible de récupérer les statistiques", e);
				}
				
		        InputStream jsonStream = IOUtils.toInputStream(flexJsonString, "utf-8");
		        
		        response.setContentType("application/json");
		        response.setCharacterEncoding("utf-8");   
		        
		        FileCopyUtils.copy(jsonStream, response.getOutputStream());
			}

		}
	}    
}
