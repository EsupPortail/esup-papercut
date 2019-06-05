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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@FilterDef(name = "contextFilter", parameters = {@ParamDef(name = "paperCutContext", type = "string")})
@Filter(name = "contextFilter", condition = "paper_cut_context = :paperCutContext")
public class PayboxPapercutTransactionLog implements ContextSupport {

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "dd/MM/yyyy-HH:mm:ss")
    private Date transactionDate;

    private String uid;

    private String paperCutContext;

    private String papercutWsCallStatus;

    private String reference;

    private String montant;

    private String papercutNewSolde;

    private String auto;

    private String erreur;

    private String idtrans;

    private String signature;

    private String papercutOldSolde;
    
    private Boolean archived = false;   
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPaperCutContext() {
		return paperCutContext;
	}

	public void setPaperCutContext(String paperCutContext) {
		this.paperCutContext = paperCutContext;
	}

	public String getPapercutWsCallStatus() {
		return papercutWsCallStatus;
	}

	public void setPapercutWsCallStatus(String papercutWsCallStatus) {
		this.papercutWsCallStatus = papercutWsCallStatus;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public String getPapercutNewSolde() {
		return papercutNewSolde;
	}

	public void setPapercutNewSolde(String papercutNewSolde) {
		this.papercutNewSolde = papercutNewSolde;
	}

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public String getErreur() {
		return erreur;
	}

	public void setErreur(String erreur) {
		this.erreur = erreur;
	}

	public String getIdtrans() {
		return idtrans;
	}

	public void setIdtrans(String idtrans) {
		this.idtrans = idtrans;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPapercutOldSolde() {
		return papercutOldSolde;
	}

	public void setPapercutOldSolde(String papercutOldSolde) {
		this.papercutOldSolde = papercutOldSolde;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public String getMontantDevise() {
    	Double mnt = new Double(montant)/100.0;
    	return mnt.toString();
    }

}
