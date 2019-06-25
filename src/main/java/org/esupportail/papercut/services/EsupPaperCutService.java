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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.domain.PayPapercutTransactionLog.PayMode;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.domain.izlypay.IzlyPayCallBack;
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
	IzlyPayService izlyPayService;
	
	@Autowired
	PapercutService papercutService;


	public void setPayBoxService(PayBoxService payBoxService) {
		this.payBoxService = payBoxService;
	}

	public void setPapercutService(PapercutService papercutService) {
		this.papercutService = papercutService;
	}
	
	public List<PayMode> getPayModes(EsupPapercutContext context) {
		List<PayMode> payModes = new ArrayList<PayMode>();
		if(context.getPaybox() != null) {
			payModes.add(PayMode.PAYBOX);
		}
		if(context.getIzlypay() != null) {
			payModes.add(PayMode.IZLYPAY);
		}
		return payModes;
	}
	
	public UserPapercutInfos getUserPapercutInfos(EsupPapercutContext context, String uid) {
		return papercutService.getUserPapercutInfos(context, uid);
	}

	public PayBoxForm getPayBoxForm(EsupPapercutContext context, String uid, String mail, Integer montant, String portletContextPath) {
		return payBoxService.getPayBoxForm(context, uid, mail, montant, portletContextPath);
	}

	
	public String getIzlyPayUrl(EsupPapercutContext context, String uid, String mail, Integer montant, String contextPath) {
		return izlyPayService.getIzlyPayUrl(context, uid, mail, montant, contextPath);
	}

	
	public boolean payboxCallback(EsupPapercutContext context, Integer montant, String reference, String auto, String erreur, String idtrans, String signature, String queryString, String ip, String currentUserUid) {
		
		List<PayPapercutTransactionLog> txLogs  = papercutDaoService.findPayPapercutTransactionLogsByIdtransAndPayMode(idtrans, PayMode.PAYBOX, PageRequest.of(0, Integer.MAX_VALUE)).getContent();		
		
		if(txLogs.size() > 0) {
			log.info("This transaction + " + idtrans + " is already OK");
			return true;
		}
		
		PayPapercutTransactionLog txLog = new PayPapercutTransactionLog();
	
		txLog.setPayMode(PayMode.PAYBOX);
		txLog.setMontant(montant);
		txLog.setIdtrans(idtrans);
		txLog.setTransactionDate(new Date());
		txLog.setReference(reference);
		String uid = reference.split("@")[0];
		uid = uid.substring(context.getNumCommandePrefix().length(), uid.length());
		txLog.setUid(uid);
		String papercutContext = reference.split("@")[1];
		txLog.setPapercutContext(papercutContext);

		// if paybox server OR connected user ok 
		if(payBoxService.isPayboxServer(context, ip) || uid.equals(currentUserUid)) {

			// check signature == message come from paybox
			if(payBoxService.checkPayboxSignature(context, queryString, signature)) {

				String papercutOldSolde =  papercutService.getUserPapercutInfos(context, uid).getBalance();
				txLog.setPapercutOldSolde(papercutOldSolde);

				if("00000".equals(erreur)) {
					try {
						log.info("Transaction : " + reference + " pour un montant de " + montant + " OK !");
						double montantEuros = new Double(montant) / 100.0;
						papercutService.creditUserBalance(context, uid, montantEuros, idtrans);
						
						String papercutNewSolde =  papercutService.getUserPapercutInfos(context, uid).getBalance();
						txLog.setPapercutNewSolde(papercutNewSolde);

						papercutDaoService.persist(txLog);	
						
					} catch(Exception ex) {
						log.error("Exception during creditUserBalance on papercut ?", ex);
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

	public boolean izlypayCallback(EsupPapercutContext context, IzlyPayCallBack izlyPayCallBack) {

		String reference = izlyPayCallBack.getOperationSMoney().getClientOrderId();
		String idtrans = reference;
		Integer montant = izlyPayCallBack.getOperationSMoney().getAmountAsInteger();

		List<PayPapercutTransactionLog> txLogs  = papercutDaoService.findPayPapercutTransactionLogsByIdtransAndPayMode(idtrans, PayMode.IZLYPAY, PageRequest.of(0, Integer.MAX_VALUE)).getContent();		

		if(txLogs.size() > 0) {
			log.info("This transaction + " + idtrans + " is already OK");
			return true;
		}

		PayPapercutTransactionLog txLog = new PayPapercutTransactionLog();

		txLog.setPayMode(PayMode.IZLYPAY);
		txLog.setMontant(montant);
		txLog.setReference(reference);
		txLog.setIdtrans(idtrans);
		txLog.setTransactionDate(new Date());
		String uid = reference.split("@")[0];
		uid = uid.substring(context.getNumCommandePrefix().length(), uid.length());
		txLog.setUid(uid);
		String papercutContext = reference.split("@")[1];
		txLog.setPapercutContext(papercutContext);

		String papercutOldSolde =  papercutService.getUserPapercutInfos(context, uid).getBalance();
		txLog.setPapercutOldSolde(papercutOldSolde);

		try {
			log.info("Transaction : " + reference + " pour un montant de " + montant + " OK !");
			double montantEuros = new Double(montant) / 100.0;
			papercutService.creditUserBalance(context, uid, montantEuros, idtrans);

			String papercutNewSolde =  papercutService.getUserPapercutInfos(context, uid).getBalance();
			txLog.setPapercutNewSolde(papercutNewSolde);

			papercutDaoService.persist(txLog);	


		} catch(Exception ex) {
			log.error("Exception during creditUserBalance on papercut ?", ex);
		}
		return true;

	}
	
}
