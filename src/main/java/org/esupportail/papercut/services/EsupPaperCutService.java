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
package org.esupportail.papercut.services;

import java.util.Date;
import java.util.List;

import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EsupPaperCutService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PapercutDaoService papercutDaoService;
	
	@Autowired
	PayBoxService payBoxService;
	
	@Autowired
	PapercutService papercutService;


	public void setPayBoxService(PayBoxService payBoxService) {
		this.payBoxService = payBoxService;
	}

	public void setPapercutService(PapercutService papercutService) {
		this.papercutService = papercutService;
	}

	public UserPapercutInfos getUserPapercutInfos(EsupPapercutContext context, String uid) {
		return papercutService.getUserPapercutInfos(context, uid);
	}

	public PayBoxForm getPayBoxForm(EsupPapercutContext context, String uid, String mail, double montant, String portletContextPath) {
		if(payBoxService != null)
			return payBoxService.getPayBoxForm(context, uid, mail, montant, portletContextPath);
		else return null;
	}

	
	public boolean payboxCallback(EsupPapercutContext context, String montant, String reference, String auto, String erreur, String idtrans, String signature, String queryString, String ip, String currentUserUid) {
		
		List<PayboxPapercutTransactionLog> txLogs  = papercutDaoService.findPayboxPapercutTransactionLogsByIdtrans(idtrans, PageRequest.of(0, Integer.MAX_VALUE));		
		
		boolean newTxLog = txLogs.size() == 0;
		PayboxPapercutTransactionLog txLog = txLogs.size()>0 ? txLogs.get(0) : null;
		
		if(txLog == null) {
			 txLog = new PayboxPapercutTransactionLog();
		} else {
			if("OK".equals(txLog.getPapercutWsCallStatus()) || !"00000".equals(txLog.getErreur())) {
				log.info("This transaction + " + idtrans + " is already OK");
				return true;
			}
		}
	
		txLog.setMontant(montant);
		txLog.setReference(reference);
		txLog.setAuto(auto);
		txLog.setErreur(erreur);
		txLog.setIdtrans(idtrans);
		txLog.setSignature(signature);
		txLog.setTransactionDate(new Date());
		String uid = reference.split("@")[0];
		uid = uid.substring(context.getPaybox().getNumCommandePrefix().length(), uid.length());
		txLog.setUid(uid);
		String paperCutContext = reference.split("@")[1];
		txLog.setPaperCutContext(paperCutContext);

		// if paybox server OR connected user ok 
		if(payBoxService.isPayboxServer(context, ip) || uid.equals(currentUserUid)) {

			// check signature == message come from paybox
			if(payBoxService.checkPayboxSignature(context, queryString, signature)) {

				String papercutOldSolde =  papercutService.getUserPapercutInfos(context, uid).getBalance();
				txLog.setPapercutOldSolde(papercutOldSolde);

				// TODO Vérifier que le montant correspondait bien à la demande initiale ?
				if("00000".equals(erreur)) {
					try {
						log.info("Transaction : " + reference + " pour un montant de " + montant + " OK !");
						double montantEuros = new Double(montant) / 100.0;
						papercutService.creditUserBalance(context, uid, montantEuros, idtrans);
						txLog.setPapercutWsCallStatus("OK");
						
						String papercutNewSolde =  papercutService.getUserPapercutInfos(context, uid).getBalance();
						txLog.setPapercutNewSolde(papercutNewSolde);

						if(newTxLog) {
							papercutDaoService.persist(txLog);	
						} else {
							papercutDaoService.merge(txLog);	
						}
						
					} catch(Exception ex) {
						log.error("Exception during creditUserBalance on papercut ?", ex);
						txLog.setPapercutWsCallStatus("ERREUR");
					}
				} else {
					log.info("'Erreur' " + erreur + "  (annulation) lors de la transaction paybox : " + reference + " pour un montant de " + montant);
				}
				
			} else {
				log.error("signature checking of paybox failed, transaction " + txLog + " canceled.");
			}

			return true;
			
		} else {
			log.warn("this ip " + ip + " is not trusted for the paybox transaction, " +
					"or this user " + uid + " does'nt correspond to this user " + currentUserUid + "  (validatePayboxJustWithRedirection mode), " +
					"transaction " + txLog + " canceled.");
		}

    	return false;
	}
	
}
