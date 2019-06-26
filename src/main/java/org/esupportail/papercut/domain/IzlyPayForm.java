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

public class IzlyPayForm {

	private String montant;
	
	private Integer nbSheets;
	
	private Integer nbColorSheets;
	
	private String toolTip;

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public Integer getNbSheets() {
		return nbSheets;
	}

	public void setNbSheets(Integer nbSheets) {
		this.nbSheets = nbSheets;
	}

	public Integer getNbColorSheets() {
		return nbColorSheets;
	}

	public void setNbColorSheets(Integer nbColorSheets) {
		this.nbColorSheets = nbColorSheets;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	
}
