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
import org.esupportail.papercut.services.EsupPaperCutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/{papercutContext}/payboxcallback")
public class PayBoxCallbackController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource 
	EsupPaperCutService esupPaperCutService;

	@Resource
	EsupPapercutConfig config;
	
    /** 
     * Manage callback response from paybox - url like :  
     * 
     * /payboxcallback?montant=200&reference=bonamvin@univrouen@200-2013-08-23-17-08-18-394&auto=XXXXXX&erreur=00000&idtrans=3608021&signature=CPqq18Un24NL0llB3E3G9kbKI4ztlkoL%2BSRTnMMrWlPBTVNTsn%2B%2FxA0YMSQOGGnU0wm45HYh%2F2RHoZGG3THzj7xKSY6upNJcnKrfFmzfTgA5FTFA3dyM27RgKmLcCeH48FRNoZPjVsKk0G2npvaP%2FY5pkSvn%2BQUl34DkmJkTejs%3D
     *
     * @param uiModel
     * @return empty page
     */
	@GetMapping
    public ResponseEntity<String> payboxcallback(@PathVariable String papercutContext, @RequestParam String montant, @RequestParam String reference, @RequestParam(required=false) String auto, 
    		@RequestParam String erreur, @RequestParam String idtrans, @RequestParam String signature, 
    		HttpServletRequest request) {
		
		String ip = request.getRemoteAddr();
		String queryString = request.getQueryString();
		
		if(esupPaperCutService.payboxCallback(config.getContext(papercutContext), montant, reference, auto, erreur, idtrans, signature, queryString, ip, null)) {
    		HttpHeaders headers = new HttpHeaders();
    		headers.add("Content-Type", "text/html; charset=utf-8");
    		return new ResponseEntity<String>("", headers, HttpStatus.OK);
		} else {
    		HttpHeaders headers = new HttpHeaders();
    		headers.add("Content-Type", "text/html; charset=utf-8");
    		return new ResponseEntity<String>("", headers, HttpStatus.FORBIDDEN);
		}
    }
}
