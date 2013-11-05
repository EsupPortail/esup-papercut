package org.esupportail.papercut.services;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.esupportail.papercut.domain.PayBoxForm;
import org.springframework.core.io.ClassPathResource;


public class PayBoxService {

	private final Logger log = Logger.getLogger(getClass());

	private final static String retourVariables = "montant:M;reference:R;auto:A;erreur:E;idtrans:S;signature:K";

	HashService hashService;

	private String site;
	
	private String rang;
	
	private String identifiant;
	
	// 978 = Euro
	private String devise;
	
	private List<String> payboxActionUrls;

	private List<String> payboxServersIP;
	
	private PublicKey payboxPublicKey;

	private String reponseServerUrl;
	

	public void setHashService(HashService hashService) {
		this.hashService = hashService;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setRang(String rang) {
		this.rang = rang;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}


	public void setPayboxActionUrls(List<String> payboxActionUrls) {
		this.payboxActionUrls = payboxActionUrls;
	}

	public void setPayboxServersIP(List<String> payboxServersIP) {
		this.payboxServersIP = payboxServersIP;
	}
	
	public void setDerPayboxPublicKeyFile(String derPayboxPublicKeyFile) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		org.springframework.core.io.Resource derPayboxPublicKeyRessource = new ClassPathResource(derPayboxPublicKeyFile);
		InputStream fis = derPayboxPublicKeyRessource.getInputStream();
        DataInputStream dis = new DataInputStream(fis);
        byte[] pubKeyBytes = new byte[fis.available()];
        dis.readFully(pubKeyBytes);
        fis.close();
        dis.close();
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		this.payboxPublicKey =  kf.generatePublic(x509EncodedKeySpec);
	}

	public void setReponseServerUrl(String reponseServerUrl) {
		this.reponseServerUrl = reponseServerUrl;
	}

	/**
	 * @param uid
	 * @param montant en €
	 * @return
	 */
	public PayBoxForm getPayBoxForm(String uid, String mail, double montant, String paperCutContext, String portletContextPath) {
		
		String montantAsCents = Integer.toString(new Double(montant * 100).intValue());
		
		PayBoxForm payBoxForm = new PayBoxForm();
		payBoxForm.setActionUrl(getPayBoxActionUrl());
		payBoxForm.setClientEmail(mail);
		payBoxForm.setCommande(getNumCommande(uid, montantAsCents, paperCutContext));
		payBoxForm.setDevise(devise);
		payBoxForm.setHash(hashService.getHash());
		payBoxForm.setIdentifiant(identifiant);
		payBoxForm.setRang(rang);
		payBoxForm.setRetourVariables(retourVariables);
		payBoxForm.setSite(site);
		payBoxForm.setTime(getCurrentTime());
		payBoxForm.setTotal(montantAsCents);
		
		String callbackUrl = reponseServerUrl + "/esup-papercut/servlet/payboxcallback";
		payBoxForm.setCallbackUrl(callbackUrl);
		
		String forwardUrl = reponseServerUrl + portletContextPath;
		payBoxForm.setForwardAnnuleUrl(forwardUrl);
		payBoxForm.setForwardEffectueUrl(forwardUrl);
		payBoxForm.setForwardRefuseUrl(forwardUrl);
		
		String hMac = hashService.getHMac(payBoxForm.getParamsAsString());
		payBoxForm.setHmac(hMac);
		
		return payBoxForm;
	}

	private String getNumCommande(String uid, String montantAsCents, String paperCutContext) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-S");
		return uid + "@" + paperCutContext + "@" + montantAsCents + "-" + df.format(new Date());
	}

	protected String getPayBoxActionUrl() {
		for(String payboxActionUrl : payboxActionUrls) {
			try {
				// on teste la connection, pour voir si le serveur est disponible
				URL url = new URL(payboxActionUrl);
				URLConnection connection = url.openConnection();
				connection.connect();
				connection.getInputStream().read();
				return payboxActionUrl;
			} catch (Exception e) {
				log.warn("Pb with " + payboxActionUrl, e);
			} 
		}			
		throw new RuntimeException("No paybox action url is available at the moment !");
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

	public boolean isPayboxServer(String ip) {

		if(ip == null || !payboxServersIP.contains(ip)) {
			log.info(ip + " is not a paybox server");
			return false;
		} else {
			log.info(ip + " is a paybox server");
			return true;
		}
	}
		
	public boolean checkPayboxSignature(String queryString, String signature) {
		
		String sData = queryString.substring(0, queryString.lastIndexOf("&"));
		
		try {
			Signature sig = Signature.getInstance("SHA1WithRSA");
			byte[] sigBytes = Base64.decodeBase64(signature.getBytes());
			sig.initVerify(payboxPublicKey);
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

}
