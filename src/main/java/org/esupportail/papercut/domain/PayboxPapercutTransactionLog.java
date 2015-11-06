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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals", "findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEqualsAndArchived", "findPayboxPapercutTransactionLogsByArchived", "findPayboxPapercutTransactionLogsByPaperCutContextEquals", "findPayboxPapercutTransactionLogsByIdtransEquals" })
public class PayboxPapercutTransactionLog {

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
    
    public String getMontantDevise() {
    	Double mnt = new Double(montant)/100.0;
    	return mnt.toString();
    }
    
	/*
	 * 
	 * Requêtes pour PostgreSQL : à adapter pour un autre SGBD,
	 *
	*/
    public static List<Object>  countNumberTranscationsBydate() {
    	String requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, count(*) as count FROM paybox_papercut_transaction_log GROUP BY year, month ORDER BY year,month";
    	EntityManager em = new PayboxPapercutTransactionLog().entityManager;
		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public static List<Object>  countMontantTranscationsBydate() {
    	String requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(CAST(montant AS decimal)) as totalMois FROM paybox_papercut_transaction_log GROUP BY year, month ORDER BY year,month";
    	EntityManager em = new PayboxPapercutTransactionLog().entityManager;
		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public static List<Object>  countCumulNombreTranscationsBydate() {
    	String requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(count(* )) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM paybox_papercut_transaction_log Where date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from paybox_papercut_transaction_log) GROUP BY year, month ORDER BY year,month";
    	EntityManager em = new PayboxPapercutTransactionLog().entityManager;
		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public static List<Object>  countCumulMontantTranscationsBydate() {
    	String requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month ,sum( sum(CAST(montant AS decimal))) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM paybox_papercut_transaction_log Where date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from paybox_papercut_transaction_log) GROUP BY year, month ORDER BY year,month";
    	EntityManager em = new PayboxPapercutTransactionLog().entityManager;
		Query q = em.createNativeQuery(requete);

        return q.getResultList();
    }

}
