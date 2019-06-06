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
import javax.annotation.Resource;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(produces = "text/html")
    public String historyList(@PageableDefault(size = 10, direction = Direction.DESC, sort = "transactionDate") Pageable pageable, 
    		Model uiModel) { 	
    	String a = null;
    	if(a.equals("toto")) {

        uiModel.addAttribute("pageLogs", papercutDaoService.findAllPayboxPapercutTransactionLogs(pageable));
        uiModel.addAttribute("active", "admin"); 	
    	}
        return "user/history";
    }
	
    
    @GetMapping(value = "/{i}", produces = "text/html")
    public String viewTransactionLog(@PathVariable("id") Long id, Model uiModel) {
    	uiModel.addAttribute("payboxpapercuttransactionlog", papercutDaoService.findById(id).get());
    	uiModel.addAttribute("itemId", id);
    	uiModel.addAttribute("active", "logs"); 	
        return "user/show-transactionlog";
    }

    
} 

