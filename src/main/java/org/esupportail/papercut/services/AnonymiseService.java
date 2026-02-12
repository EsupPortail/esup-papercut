/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
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
package org.esupportail.papercut.services;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.dao.AllContextPapercutDaoService;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class AnonymiseService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	EsupPapercutConfig config;

	@Autowired
	AllContextPapercutDaoService allContextPapercutDaoService;

	@Scheduled(fixedDelay=3600000)
	public void anonymiseOldTransactions() {	
		for(String context : config.getContexts().keySet()) {
			if(config.getContext(context).getAnonymiseEnabled()) {
				log.debug(String.format("anonymiseOldTransactions called for context %s - transactions logs older than %s days will be anonymised", context, config.getContext(context).getAnonymiseOldDays()));
				List<PayPapercutTransactionLog> transactionLogs = allContextPapercutDaoService.findOldPayPapercutTransactionLogsNotAnonymised(config.getContext(context).getAnonymiseOldDays());
				for (PayPapercutTransactionLog transactionLog : transactionLogs) {
					anonymise(transactionLog);
				}
				if (transactionLogs.size() > 0) {
					log.info(transactionLogs.size() + " old transactionLogs anonymised");
				}
			} else {
				log.debug(String.format("Anonymisation of context %s is not Enabled", context));
			}
		}
	}

	private void anonymise(PayPapercutTransactionLog transactionLog) {
		transactionLog.setUid("anonymous");
		transactionLog.setReference("anonymous");
	}

}

