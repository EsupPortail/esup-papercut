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
package org.esupportail.papercut.domain;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

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
		this.balance = fixRoundPapercutError(propertyValues.get(1));
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
	
	public static Vector<String> getPropertyNames() {
		return propertyNames;
	}

	public static void setPropertyNames(Vector<String> propertyNames) {
		UserPapercutInfos.propertyNames = propertyNames;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPrintJobs() {
		return printJobs;
	}

	public void setPrintJobs(String printJobs) {
		this.printJobs = printJobs;
	}

	public String getPrintPages() {
		return printPages;
	}

	public void setPrintPages(String printPages) {
		this.printPages = printPages;
	}

	public boolean isPrintDisabled() {
		return printDisabled;
	}

	public void setPrintDisabled(boolean printDisabled) {
		this.printDisabled = printDisabled;
	}

	public boolean isRestricted() {
		return restricted;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Papercut WebService gives balance values like 1.9465000000000003 
	 * Error of epsilon machine is suspected in Papercut !
	 * -> to workaround, we simply here round the balance ... 
	 * 
	 * Note that 5 decimals is the max in papercut today, but maybe one day it will be more so here we return with 10 max 
	 * 
	 * @param balance
	 * @return
	 */
	private static String fixRoundPapercutError(String balance) {
		DecimalFormat df = new DecimalFormat("#.##########");
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(Double.valueOf(balance)); 
	}
	
}
