package org.esupportail.papercut.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class EsupPapercutSessionObject {

	boolean isAdmin;
	
    String paperCutContext;
   
    String requeteNbTransactions;
    
	String requeteMontantTransactions;
	
	String requeteCumulTransactions;
	
	String requeteCumulMontants;
	
}
