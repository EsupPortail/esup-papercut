package org.esupportail.papercut.domain;

public class EsupPapercutSessionObject {

	boolean isAdmin;
	
    String paperCutContext;
   
    String requeteNbTransactions;
    
	String requeteMontantTransactions;
	
	String requeteCumulTransactions;
	
	String requeteCumulMontants;

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPaperCutContext() {
		return paperCutContext;
	}

	public void setPaperCutContext(String paperCutContext) {
		this.paperCutContext = paperCutContext;
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
	
}
