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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@FilterDef(name = "contextFilter", parameters = {@ParamDef(name = "papercutContext", type = "string")})
@Filter(name = "contextFilter", condition = "papercut_context = :papercutContext")
public class PayPapercutTransactionLog implements ContextSupport {

    public static enum PayMode {
		IZLYPAY, PAYBOX
	}
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "dd/MM/yyyy-HH:mm:ss")
    private Date transactionDate;
    
    @Enumerated(EnumType.STRING)
    private PayMode payMode;

    private String uid;

    private String papercutContext;

    private String reference;

    private Integer montant;

    private String papercutNewSolde;

    private String idtrans;

    private String papercutOldSolde;
    
    private Boolean archived = false;   
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPapercutContext() {
		return papercutContext;
	}

	public void setPapercutContext(String papercutContext) {
		this.papercutContext = papercutContext;
	}

	public PayMode getPayMode() {
		return payMode;
	}

	public void setPayMode(PayMode payMode) {
		this.payMode = payMode;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getMontant() {
		return montant;
	}

	public void setMontant(Integer montant) {
		this.montant = montant;
	}

	public String getPapercutNewSolde() {
		return papercutNewSolde;
	}

	public void setPapercutNewSolde(String papercutNewSolde) {
		this.papercutNewSolde = papercutNewSolde;
	}

	public String getIdtrans() {
		return idtrans;
	}

	public void setIdtrans(String idtrans) {
		this.idtrans = idtrans;
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
