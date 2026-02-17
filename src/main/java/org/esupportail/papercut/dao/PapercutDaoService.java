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
package org.esupportail.papercut.dao;

import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.domain.PayPapercutTransactionLog.PayMode;
import org.esupportail.papercut.domain.PayPapercutTransactionLogSearch;
import org.esupportail.papercut.security.ContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Service
public class PapercutDaoService {
	
	@PersistenceContext
	public EntityManager entityManager;
	
	@Autowired
	private PayPapercutTransactionLogRepository txRepository;
	
    @Transactional
    public void persist(PayPapercutTransactionLog txLog) {
    	txRepository.save(txLog);
    }
    
    @Transactional
    public void remove(PayPapercutTransactionLog txLog) {       
        if (entityManager.contains(txLog)) {
            entityManager.remove(txLog);
        } else {
            PayPapercutTransactionLog attached = txRepository.findById(txLog.getId()).get();
            entityManager.remove(attached);
        }
    }

    
	public Long countByUidAndArchived(String uid, boolean archived) {
		return txRepository.countByUidAndArchived(uid, archived);
	}

	public Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByIdtransAndPayMode(String idTrans, PayMode payMode, Pageable pageable) {
		return txRepository.findPayPapercutTransactionLogsByIdtransAndPayMode(idTrans, payMode, pageable);
	}

	public Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByUidAndArchived(String uid, boolean archived, Pageable pageable) {
		return txRepository.findPayPapercutTransactionLogsByUidAndArchived(uid, archived, pageable);
	}

	public Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByUid(String uid, Pageable pageable) {
		return txRepository.findPayPapercutTransactionLogsByUid(uid, pageable);
	}

	
	/**
	 * CF doc hibernate "Filters apply to entity queries, but not to direct fetching."
	 * un simple txRepository.findById(id) n'est pas affecté par @PapercutDaoServiceAspect
	 * on passe donc par une query ici
	 */
	public PayPapercutTransactionLog findById(Long id) {
		TypedQuery<PayPapercutTransactionLog> q = entityManager.createQuery("SELECT l from PayPapercutTransactionLog AS l WHERE l.id = :id", PayPapercutTransactionLog.class);
		q.setParameter("id", id);
		return q.getSingleResult();
	}

	public Page<PayPapercutTransactionLog> findAllPayPapercutTransactionLogs(Pageable pageable) {
		return txRepository.findAll(pageable);
	}

	public Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByExample(PayPapercutTransactionLogSearch payPapercutTransactionLogSearch, Pageable pageable) {
		// Création d'un exemple de recherche à partir de l'objet de recherche
		PayPapercutTransactionLog example = new PayPapercutTransactionLog(payPapercutTransactionLogSearch);

		// Utilisation de ExampleMatcher pour définir les critères de recherche
		ExampleMatcher matcher = ExampleMatcher.matching()
			.withIgnoreNullValues() // Ignorer les valeurs null
			.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Recherche par contenu pour les String
			.withIgnoreCase(); // Ignorer la casse pour les String

		Example<PayPapercutTransactionLog> exampleQuery = Example.of(example, matcher);
		return txRepository.findAll(exampleQuery, pageable);
	}

	
	/*
	 * Requêtes pour PostgreSQL : corespondent aux préférences "requetes***" positionnées à la valeur "useOriginal"
	 * 	Pour adapter les requêtes à un autre SGBD, changer la valeur "useOriginal" par la requête directement
	*/
    public List countNumberTranscationsBydate(String sqlQuery) {
    	
    	String papercutContext = ContextHelper.getCurrentContext();
    	
    	if("useOriginal".equals(sqlQuery)){
    		sqlQuery = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, count(*) as count FROM pay_papercut_transaction_log WHERE papercut_context=:papercutContext GROUP BY year, month ORDER BY year,month";
    	}

		Query q = entityManager.createNativeQuery(sqlQuery);
		
		q.setParameter("papercutContext", papercutContext);

        return q.getResultList();
    }
    
    public List countMontantTranscationsBydate(String sqlQuery) {
    	
    	String papercutContext = ContextHelper.getCurrentContext();
    	
    	if("useOriginal".equals(sqlQuery)){
    		sqlQuery = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(CAST(montant AS decimal)) as totalMois FROM pay_papercut_transaction_log WHERE papercut_context=:papercutContext GROUP BY year, month ORDER BY year,month";
    	}

		Query q = entityManager.createNativeQuery(sqlQuery);
		
		q.setParameter("papercutContext", papercutContext);

        return q.getResultList();
    }
    
    public List countCumulNombreTranscationsBydate(String sqlQuery) {
    	
    	String papercutContext = ContextHelper.getCurrentContext();
    	
    	if("useOriginal".equals(sqlQuery)){
    		sqlQuery = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(count(* )) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM pay_papercut_transaction_log WHERE date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from pay_papercut_transaction_log) and papercut_context=:papercutContext GROUP BY year, month ORDER BY year,month";
    	}

		Query q = entityManager.createNativeQuery(sqlQuery);
		
		q.setParameter("papercutContext", papercutContext);

        return q.getResultList();
    }
    
    public List countCumulMontantTranscationsBydate(String sqlQuery) {
    	
    	String papercutContext = ContextHelper.getCurrentContext();
    	
    	if("useOriginal".equals(sqlQuery)){
    		sqlQuery = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month ,sum( sum(CAST(montant AS decimal))) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM pay_papercut_transaction_log WHERE date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from pay_papercut_transaction_log) and papercut_context=:papercutContext GROUP BY year, month ORDER BY year,month";
    	} 	

		Query q = entityManager.createNativeQuery(sqlQuery);

		q.setParameter("papercutContext", papercutContext);
		
        return q.getResultList();
    }
    
}
