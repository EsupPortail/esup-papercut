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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.springframework.transaction.annotation.Transactional;

public class EsupPaperCutService {

	private final Logger log = Logger.getLogger(getClass());
	
	PayBoxService payBoxService;
	
	PapercutService papercutService;

	public void setPayBoxService(PayBoxService payBoxService) {
		this.payBoxService = payBoxService;
	}

	public void setPapercutService(PapercutService papercutService) {
		this.papercutService = papercutService;
	}

	public UserPapercutInfos getUserPapercutInfos(String uid) {
		return papercutService.getUserPapercutInfos(uid);
	}

	public PayBoxForm getPayBoxForm(String uid, String mail, double montant, String paperCutContext, String portletContextPath) {
		if(payBoxService != null)
			return payBoxService.getPayBoxForm(uid, mail, montant, paperCutContext, portletContextPath);
		else return null;
	}

	
	public boolean payboxCallback(String montant, String reference, String auto, String erreur, String idtrans, String signature, String queryString, String ip, String currentUserUid) {
		
		List<PayboxPapercutTransactionLog> txLogs  = PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByIdtransEquals(idtrans, null, null).getResultList();		
		
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
		uid = uid.substring(payBoxService.getNumCommandePrefix().length(), uid.length());
		txLog.setUid(uid);
		String paperCutContext = reference.split("@")[1];
		txLog.setPaperCutContext(paperCutContext);

		// if paybox server OR connected user ok 
		if(payBoxService.isPayboxServer(ip) || uid.equals(currentUserUid)) {

			// check signature == message come from paybox
			if(payBoxService.checkPayboxSignature(queryString, signature)) {

				String papercutOldSolde =  papercutService.getUserPapercutInfos(uid).getBalance();
				txLog.setPapercutOldSolde(papercutOldSolde);

				// TODO Vérifier que le montant correspondait bien à la demande initiale ?
				if("00000".equals(erreur)) {
					try {
						log.info("Transaction : " + reference + " pour un montant de " + montant + " OK !");
						double montantEuros = new Double(montant) / 100.0;
						papercutService.creditUserBalance(uid, montantEuros, idtrans);
						txLog.setPapercutWsCallStatus("OK");
					} catch(Exception ex) {
						log.error("Exception during creditUserBalance on papercut ?", ex);
						txLog.setPapercutWsCallStatus("ERREUR");
					}
				} else {
					log.info("Erreur " + erreur + " lors de la transaction paybox : " + reference + " pour un montant de " + montant);
				}

				String papercutNewSolde =  papercutService.getUserPapercutInfos(uid).getBalance();
				txLog.setPapercutNewSolde(papercutNewSolde);

				if(newTxLog) {
					txLog.persist();	
				} else {
					txLog.merge();	
				}

				return true;
			} else {
				log.warn("signature checking of paybox failed, transaction " + txLog + " canceled.");
			}
		} else {
			log.warn("this ip " + ip + " is not trusted for the paybox transaction, " +
					"or this user " + uid + " does'nt correspond to this user " + currentUserUid + "  (validatePayboxJustWithRedirection mode), " +
					"transaction " + txLog + " canceled.");
		}
		

		// if request come from paybox, we save this log for this action which failed,
		// else we don't trust the params sent by the user ... and so we don't save the log
		if(payBoxService.isPayboxServer(ip)) {
			if(newTxLog) {
				txLog.persist();	
			} else {
				txLog.merge();	
			}
		}
		
    	return false;
	}
	
}
