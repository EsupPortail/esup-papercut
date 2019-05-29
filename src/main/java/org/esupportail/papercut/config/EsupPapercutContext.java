package org.esupportail.papercut.config;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.core.io.ClassPathResource;

public class EsupPapercutContext {

	String paperCutContext = "univ-ville";
	
	String papercutUserUidAttr = "uid";
	
	String userEmail = "email";
	
	String validatePayboxJustWithRedirection = "false";
	
	String requeteNbTransactions = "useOriginal";
	
	String requeteMontantTransactions = "useOriginal";
	
	String requeteCumulTransactions = "useOriginal";
	
	String requeteCumulMontants = "useOriginal";
	
	String esupPapercutAdminRoleName = "ROLE_ADMIN";
	
	String esupPapercutManagerRoleName = "ROLE_MANAGER";

	String htmlHeader = "";
	
	String htmlFooter = "";
	
	String payboxMontantMin = "0.5";
	
	String cgvText = "";
	
	String payboxMontantMax = "5.0";
	
	String payboxMontantStep = "0.5";	
	
	String papercutSheetCost = "-1";
	
	String papercutColorSheetCost = "-1";
	
	String transactionNbMax = "-1";
	
	String transactionMontantMax = "-1";
	
	PapercutConfig papercut;
	
	PayboxConfig paybox;
	
	public String getPaperCutContext() {
		return paperCutContext;
	}

	public void setPaperCutContext(String paperCutContext) {
		this.paperCutContext = paperCutContext;
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

	public String getEsupPapercutAdminRoleName() {
		return esupPapercutAdminRoleName;
	}

	public void setEsupPapercutAdminRoleName(String esupPapercutAdminRoleName) {
		this.esupPapercutAdminRoleName = esupPapercutAdminRoleName;
	}

	public String getEsupPapercutManagerRoleName() {
		return esupPapercutManagerRoleName;
	}

	public void setEsupPapercutManagerRoleName(String esupPapercutManagerRoleName) {
		this.esupPapercutManagerRoleName = esupPapercutManagerRoleName;
	}

	public String getHtmlHeader() {
		return htmlHeader;
	}

	public void setHtmlHeader(String htmlHeader) {
		this.htmlHeader = htmlHeader;
	}

	public String getHtmlFooter() {
		return htmlFooter;
	}

	public void setHtmlFooter(String htmlFooter) {
		this.htmlFooter = htmlFooter;
	}

	public String getPayboxMontantMin() {
		return payboxMontantMin;
	}

	public void setPayboxMontantMin(String payboxMontantMin) {
		this.payboxMontantMin = payboxMontantMin;
	}

	public String getCgvText() {
		return cgvText;
	}

	public void setCgvText(String cgvText) {
		this.cgvText = cgvText;
	}

	public String getPayboxMontantMax() {
		return payboxMontantMax;
	}

	public void setPayboxMontantMax(String payboxMontantMax) {
		this.payboxMontantMax = payboxMontantMax;
	}

	public String getPayboxMontantStep() {
		return payboxMontantStep;
	}

	public void setPayboxMontantStep(String payboxMontantStep) {
		this.payboxMontantStep = payboxMontantStep;
	}

	public String getPapercutSheetCost() {
		return papercutSheetCost;
	}

	public void setPapercutSheetCost(String papercutSheetCost) {
		this.papercutSheetCost = papercutSheetCost;
	}

	public String getPapercutColorSheetCost() {
		return papercutColorSheetCost;
	}

	public void setPapercutColorSheetCost(String papercutColorSheetCost) {
		this.papercutColorSheetCost = papercutColorSheetCost;
	}

	public String getTransactionNbMax() {
		return transactionNbMax;
	}

	public void setTransactionNbMax(String transactionNbMax) {
		this.transactionNbMax = transactionNbMax;
	}

	public String getTransactionMontantMax() {
		return transactionMontantMax;
	}

	public void setTransactionMontantMax(String transactionMontantMax) {
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
	
}

