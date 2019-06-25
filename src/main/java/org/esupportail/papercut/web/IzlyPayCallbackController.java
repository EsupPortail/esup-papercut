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
import javax.servlet.http.HttpServletRequest;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.domain.izlypay.IzlyPayCallBack;
import org.esupportail.papercut.domain.izlypay.IzlyPayError;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{papercutContext}/izlypaycallback")
public class IzlyPayCallbackController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource 
	EsupPaperCutService esupPaperCutService;

	@Resource
	EsupPapercutConfig config;
	
    /** 
     * Manage callback response from izlypay :  
     * 
     * POST on /izlypaycallback
     *
     * @param uiModel
     * @return empty page
     */
	@PostMapping
    public ResponseEntity<String> izlypaycallback(@PathVariable String papercutContext, @RequestBody IzlyPayCallBack izlyPayCallBack, HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		if(esupPaperCutService.izlypayCallback(config.getContext(papercutContext), izlyPayCallBack)) {
    		return new ResponseEntity<String>("", headers, HttpStatus.OK);
		} else {
    		IzlyPayError izlyPayError = new IzlyPayError();
    		izlyPayError.setCode(500);
    		izlyPayError.setMessage("Erreur lors du traitement du calbback IzlyPay sur esup-papercut");
    		return new ResponseEntity<>("", headers, HttpStatus.OK);
		}
    }
}
