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
