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

import java.net.URL;
import java.net.URLConnection;
import java.security.Signature;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.domain.PayBoxForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PayBoxService extends PayService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	HashService hashService;
	
	@Resource
	EsupPapercutConfig config;
	
	/**
	 * @param uid
	 * @param montant en €
	 * @return
	 */
	public PayBoxForm getPayBoxForm(EsupPapercutContext context, String uid, String mail, Integer montant, String contextPath) {
		
		if(context.getPaybox() == null) {
			return null;
		}
		
		PayBoxForm payBoxForm = new PayBoxForm();
		payBoxForm.setActionUrl(getPayBoxActionUrl(context));
		payBoxForm.setClientEmail(mail);
		payBoxForm.setCommande(getNumCommande(context, uid, montant));
		payBoxForm.setDevise(context.getPaybox().getDevise());
		payBoxForm.setHash(hashService.getHash());
		payBoxForm.setIdentifiant(context.getPaybox().getIdentifiant());
		payBoxForm.setRang(context.getPaybox().getRang());
		payBoxForm.setRetourVariables(context.getPaybox().getRetourVariables());
		payBoxForm.setSite(context.getPaybox().getSite());
		payBoxForm.setTime(getCurrentTime());
		payBoxForm.setTotal(montant.toString());
		String callbackUrl = String.format("%s/%s/payboxcallback", context.getPaybox().getReponseServerUrl(), context.getPapercutContext());
		payBoxForm.setCallbackUrl(callbackUrl);

		String forwardUrl = String.format("%s/%s", context.getPaybox().getForwardServerUrl(), context.getPapercutContext());
		payBoxForm.setForwardAnnuleUrl(forwardUrl);
		payBoxForm.setForwardEffectueUrl(forwardUrl);
		payBoxForm.setForwardRefuseUrl(forwardUrl);
		
		String hMac = hashService.getHMac(context, payBoxForm.getParamsAsString());
		payBoxForm.setHmac(hMac);

		return payBoxForm;
	}

	protected String getPayBoxActionUrl(EsupPapercutContext context) {
		if(context.getPaybox() != null && context.getPaybox().getPayboxActionUrlOK() == null) {
			updatePayBoxActionUrl(context);
		} 
		return context.getPaybox().getPayboxActionUrlOK();
	}
	
	/**
	 * @return current UTC Time - ISO 8601 format
	 */
	protected String getCurrentTime() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		return nowAsISO;
	}
		
	public boolean checkPayboxSignature(EsupPapercutContext context, String queryString, String signature) {
		
		String sData = queryString.substring(0, queryString.lastIndexOf("&"));
		
		try {
			Signature sig = Signature.getInstance("SHA1WithRSA");
			byte[] sigBytes = Base64.getDecoder().decode(signature.getBytes());
			sig.initVerify(context.getPaybox().getPayboxPublicKey());
			sig.update(sData.getBytes());
	        boolean signatureOk = sig.verify(sigBytes);
	        if(!signatureOk) {
	        	log.error("Erreur lors de la vérification de la signature, les données ne correspondent pas.");
	        	log.error(sData);
	        	log.error(signature);
	        }
	        return signatureOk;
		} catch (Exception e) {
			log.warn("Pb when checking SSL signature of Paybox", e);
			return false;
		}
	}
	
	@Scheduled(fixedDelay=3600000)
	public void flushPayboxActionUrlOK() {
		for(EsupPapercutContext context : config.getContexts().values()) {
			updatePayBoxActionUrl(context);
		}
	}
	
	public void updatePayBoxActionUrl(EsupPapercutContext context) {
		if(context.getPaybox() != null) {
			for(String payboxActionUrl : context.getPaybox().getPayboxActionUrls()) {
				try {
					// on teste la connection, pour voir si le serveur est disponible
					URL url = new URL(payboxActionUrl);
					URLConnection connection = url.openConnection();
					connection.connect();
					connection.getInputStream().read();
					if(!payboxActionUrl.equals(context.getPaybox().getPayboxActionUrlOK())) {
						context.getPaybox().setPayboxActionUrlOK(payboxActionUrl);
						log.info(String.format("Update Paybox Action Url for %s : %s", context.getPapercutContext(), context.getPaybox().getPayboxActionUrlOK()));
					}
					break;
				} catch (Exception e) {
					log.warn("Pb with " + payboxActionUrl, e);
				} 
			}
			if(context.getPaybox().getPayboxActionUrlOK() == null) {
				log.error(String.format("No paybox action url is available at the moment for %s", context.getPapercutContext()));
			}
		}
	}

}
