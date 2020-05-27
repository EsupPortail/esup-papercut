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

import java.util.HashMap;
import java.util.Map;

public class EsupPapercutContext {

	String papercutContext = "univ-ville";
	
    String numCommandePrefix = "EsupPaperCut-";
	
	String papercutUserUidAttr = "uid";
	
	String userEmail = "email";
	
	String validatePayboxJustWithRedirection = "false";
	
	String requeteNbTransactions = "useOriginal";
	
	String requeteMontantTransactions = "useOriginal";
	
	String requeteCumulTransactions = "useOriginal";
	
	String requeteCumulMontants = "useOriginal";
	
	Map<String, String> esupPapercutCasAttributeRuleAdmin = new HashMap<String, String>();
	
	Map<String, String> esupPapercutCasAttributeRuleManager = new HashMap<String, String>();
	
	Map<String, String> esupPapercutCasAttributeRuleUser = new HashMap<String, String>();

	String title = "Gestion des crédits d'impression";
	
	String htmlFooter = "<a href=\"https://www.esup-portail.org/wiki/display/EsupPapercut\">esup-papercut</a>";
	
	String htmlUserHeader = "";
	
	String htmlUserFooter = "";
	
	Integer montantMin = 50;
	
	String cgvText = "";
	
	Integer montantMax = 500;
	
	Integer montantStep = 50;	
	
	Double papercutSheetCost = -1.0;
	
	Double papercutColorSheetCost = -1.0;
	
	Integer transactionNbMax = -1;
	
	Integer transactionMontantMax = -1;
	
	PapercutConfig papercut;
	
	PayboxConfig paybox;
	
	IzlyPayConfig izlypay;

	AnonymizationConfig anonymization;
	
	public String getPapercutContext() {
		return papercutContext;
	}

	public void setPapercutContext(String papercutContext) {
		this.papercutContext = papercutContext;
	}
	
	public String getNumCommandePrefix() {
		return numCommandePrefix;
	}

	public void setNumCommandePrefix(String numCommandePrefix) {
		this.numCommandePrefix = numCommandePrefix;
	}

	public String getPapercutUserUidAttr() {
		return papercutUserUidAttr;
	}

	public void setPapercutUserUidAttr(String papercutUserUidAttr) {
		this.papercutUserUidAttr = papercutUserUidAttr;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getValidatePayboxJustWithRedirection() {
		return validatePayboxJustWithRedirection;
	}

	public void setValidatePayboxJustWithRedirection(String validatePayboxJustWithRedirection) {
		this.validatePayboxJustWithRedirection = validatePayboxJustWithRedirection;
	}

	public String getRequeteNbTransactions() {
		return requeteNbTransactions;
	}

	public void setRequeteNbTransactions(String requeteNbTransactions) {
		this.requeteNbTransactions = requeteNbTransactions;
	}

	public String getRequeteMontantTransactions() {
		return requeteMontantTransactions;
	}

	public void setRequeteMontantTransactions(String requeteMontantTransactions) {
		this.requeteMontantTransactions = requeteMontantTransactions;
	}

	public String getRequeteCumulTransactions() {
		return requeteCumulTransactions;
	}

	public void setRequeteCumulTransactions(String requeteCumulTransactions) {
		this.requeteCumulTransactions = requeteCumulTransactions;
	}

	public String getRequeteCumulMontants() {
		return requeteCumulMontants;
	}

	public void setRequeteCumulMontants(String requeteCumulMontants) {
		this.requeteCumulMontants = requeteCumulMontants;
	}

	public Map<String, String> getEsupPapercutCasAttributeRuleAdmin() {
		return esupPapercutCasAttributeRuleAdmin;
	}

	public void setEsupPapercutCasAttributeRuleAdmin(Map<String, String> esupPapercutCasAttributeRuleAdmin) {
		this.esupPapercutCasAttributeRuleAdmin = esupPapercutCasAttributeRuleAdmin;
	}

	public Map<String, String> getEsupPapercutCasAttributeRuleManager() {
		return esupPapercutCasAttributeRuleManager;
	}

	public void setEsupPapercutCasAttributeRuleManager(Map<String, String> esupPapercutCasAttributeRuleManager) {
		this.esupPapercutCasAttributeRuleManager = esupPapercutCasAttributeRuleManager;
	}

	public Map<String, String> getEsupPapercutCasAttributeRuleUser() {
		return esupPapercutCasAttributeRuleUser;
	}

	public void setEsupPapercutCasAttributeRuleUser(Map<String, String> esupPapercutCasAttributeRuleUser) {
		this.esupPapercutCasAttributeRuleUser = esupPapercutCasAttributeRuleUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtmlFooter() {
		return htmlFooter;
	}

	public void setHtmlFooter(String htmlFooter) {
		this.htmlFooter = htmlFooter;
	}

	public String getHtmlUserHeader() {
		return htmlUserHeader;
	}

	public void setHtmlUserHeader(String htmlUserHeader) {
		this.htmlUserHeader = htmlUserHeader;
	}

	public String getHtmlUserFooter() {
		return htmlUserFooter;
	}

	public void setHtmlUserFooter(String htmlUserFooter) {
		this.htmlUserFooter = htmlUserFooter;
	}

	public String getCgvText() {
		return cgvText;
	}

	public void setCgvText(String cgvText) {
		this.cgvText = cgvText;
	}

	public Integer getMontantMin() {
		return montantMin;
	}

	public void setMontantMin(Integer montantMin) {
		this.montantMin = montantMin;
	}

	public Integer getMontantMax() {
		return montantMax;
	}

	public void setMontantMax(Integer montantMax) {
		this.montantMax = montantMax;
	}

	public Integer getMontantStep() {
		return montantStep;
	}

	public void setMontantStep(Integer montantStep) {
		this.montantStep = montantStep;
	}

	public Double getPapercutSheetCost() {
		return papercutSheetCost;
	}

	public Double getPapercutColorSheetCost() {
		return papercutColorSheetCost;
	}

	public void setPapercutColorSheetCost(Double papercutColorSheetCost) {
		this.papercutColorSheetCost = papercutColorSheetCost;
	}

	public void setPapercutSheetCost(Double papercutSheetCost) {
		this.papercutSheetCost = papercutSheetCost;
	}

	public Integer getTransactionNbMax() {
		return transactionNbMax;
	}

	public void setTransactionNbMax(Integer transactionNbMax) {
		this.transactionNbMax = transactionNbMax;
	}

	public Integer getTransactionMontantMax() {
		return transactionMontantMax;
	}

	public void setTransactionMontantMax(Integer transactionMontantMax) {
		this.transactionMontantMax = transactionMontantMax;
	}

	public PapercutConfig getPapercut() {
		return papercut;
	}

	public void setPapercut(PapercutConfig papercut) {
		this.papercut = papercut;
	}

	public PayboxConfig getPaybox() {
		return paybox;
	}

	public void setPaybox(PayboxConfig paybox) {
		this.paybox = paybox;
	}

	public IzlyPayConfig getIzlypay() {
		return izlypay;
	}

	public void setIzlypay(IzlyPayConfig izlypay) {
		this.izlypay = izlypay;
	}

	public AnonymizationConfig getAnonymization() {
		return anonymization;
	}

	public void setAnonymization(AnonymizationConfig anonymization) {
		this.anonymization = anonymization;
	}
}

