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
package org.esupportail.papercut.config;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

public class PayboxConfig {

    String retourVariables = "montant:M;reference:R;auto:A;erreur:E;idtrans:S;signature:K";

    String hmacKey;

    String site;

    String rang;

    String identifiant;

    // 978 = Euro                                                                                                                                                                                              
    String devise = "978";

    List<String> payboxActionUrls;

    List<String> payboxServersIP;

    PublicKey payboxPublicKey;

    String reponseServerUrl;

    String forwardServerUrl;

    String payboxActionUrlOK = null;
    
    String legend;

	public String getRetourVariables() {
		return retourVariables;
	}

	public void setRetourVariables(String retourVariables) {
		this.retourVariables = retourVariables;
	}
	

	public String getHmacKey() {
		return hmacKey;
	}

	public void setHmacKey(String hmacKey) {
		this.hmacKey = hmacKey;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getRang() {
		return rang;
	}

	public void setRang(String rang) {
		this.rang = rang;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public List<String> getPayboxActionUrls() {
		return payboxActionUrls;
	}

	public void setPayboxActionUrls(List<String> payboxActionUrls) {
		this.payboxActionUrls = payboxActionUrls;
	}

	public List<String> getPayboxServersIP() {
		return payboxServersIP;
	}

	public void setPayboxServersIP(List<String> payboxServersIP) {
		this.payboxServersIP = payboxServersIP;
	}

	public PublicKey getPayboxPublicKey() {
		return payboxPublicKey;
	}

	public void setPayboxPublicKey(PublicKey payboxPublicKey) {
		this.payboxPublicKey = payboxPublicKey;
	}

	public String getReponseServerUrl() {
		return reponseServerUrl;
	}

	public void setReponseServerUrl(String reponseServerUrl) {
		this.reponseServerUrl = reponseServerUrl;
	}

	public String getForwardServerUrl() {
		if(forwardServerUrl == null) {
			return reponseServerUrl;
		}
		return forwardServerUrl;
	}

	public void setForwardServerUrl(String forwardServerUrl) {
		this.forwardServerUrl = forwardServerUrl;
	}

	public String getPayboxActionUrlOK() {
		return payboxActionUrlOK;
	}

	public void setPayboxActionUrlOK(String payboxActionUrlOK) {
		this.payboxActionUrlOK = payboxActionUrlOK;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
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

}
