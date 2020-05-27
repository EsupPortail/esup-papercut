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

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PayPapercutTransactionLogRepository;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AnonymizeService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    EsupPapercutConfig esupPapercutConfig;

    @Resource
    PayPapercutTransactionLogRepository payPapercutTransactionLogRepository;

    @Resource
    AnonymizeOneService anonymizeOneService;

    @Scheduled(fixedDelay=3600000)
    public void anonymizeOldTransactions() {
        Collection<EsupPapercutContext> availableContexts = esupPapercutConfig.getContexts().values();
        for (EsupPapercutContext availableContext : availableContexts) {
            if (availableContext.getAnonymization().isEnabled()) {
                log.debug("anonymizeOldTransactions called for context : " + availableContext);
                List<PayPapercutTransactionLog> transactionLogs = payPapercutTransactionLogRepository.findAllByTransactionDateBeforeAndUidIsNotLike(
                        Date.from(Instant.now().minus(Duration.ofDays(availableContext.getAnonymization().getOldDaysTransactionsLogs()))), "anonymized");
                for (PayPapercutTransactionLog transactionLog : transactionLogs) {
                    anonymizeOneService.anonymize(transactionLog.getId());
                }
                if (transactionLogs.size() > 0) {
                    log.info(transactionLogs.size() + " old transactionLogs anonymized for context : " + availableContext);
                }
            }
        }
    }

}

