package org.esupportail.papercut.domain;

import java.util.Vector;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class UserPapercutInfos {

	
	public static Vector<String> propertyNames = new Vector<String>();		
	static {	
		propertyNames.add("full-name"); 
		propertyNames.add("balance"); 
		propertyNames.add("email"); 
		propertyNames.add("department"); 
		propertyNames.add("office"); 
		propertyNames.add("card-number"); 
		propertyNames.add("print-stats.job-count"); 
		propertyNames.add("print-stats.page-count"); 
		propertyNames.add("disabled-print"); 
		propertyNames.add("restricted"); 
		propertyNames.add("notes"); 		
	}

	public UserPapercutInfos(String uid, Vector<String> propertyValues) {
		this.uid = uid;
		this.fullName = propertyValues.get(0);
		this.balance = propertyValues.get(1);
		this.email = propertyValues.get(2);
		this.department = propertyValues.get(3);
		this.office = propertyValues.get(4);
		this.cardNumber = propertyValues.get(5);
		this.printJobs = propertyValues.get(6);
		this.printPages = propertyValues.get(7);
		this.printDisabled = "true".equals(propertyValues.get(8));
		this.restricted =  "true".equals(propertyValues.get(9));
		this.notes = propertyValues.get(10);
	}

	private String uid;
	
	private String fullName;
	
	private String balance;
	
	private String email;
	
	private String department;
	
	private String office;
	
	private String cardNumber;
	
	private String printJobs;
	
	private String printPages;
	
	private boolean printDisabled;
	
	private boolean restricted;
	
	private String notes;

	
}
