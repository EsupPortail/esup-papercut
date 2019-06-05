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

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PapercutDaoService {
	
	@PersistenceContext
	public EntityManager entityManager;
	
	@Autowired
	private PayboxPapercutTransactionLogRepository txRepository;
	
    @Transactional
    public void persist(PayboxPapercutTransactionLog txLog) {
    	txRepository.save(txLog);
    }
    
    @Transactional
    public void remove(PayboxPapercutTransactionLog txLog) {       
        if (entityManager.contains(txLog)) {
            entityManager.remove(txLog);
        } else {
            PayboxPapercutTransactionLog attached = txRepository.findById(txLog.getId()).get();
            entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {       
        entityManager.flush();
    }
    
    @Transactional
    public void clear() {        
        entityManager.clear();
    }
    
    @Transactional
    public PayboxPapercutTransactionLog merge(PayboxPapercutTransactionLog txLog) {        
        PayboxPapercutTransactionLog merged = entityManager.merge(txLog);
        entityManager.flush();
        return merged;
    }
    
	public Long countByUidAndArchived(String uid, boolean archived) {
		return txRepository.countByUidAndArchived(uid, archived);
	}

	public Page<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByIdtrans(String idTrans, Pageable pageable) {
		return txRepository.findPayboxPapercutTransactionLogsByIdtrans(idTrans, pageable);
	}

	public Page<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByUidAndArchived(String uid, boolean archived, Pageable pageable) {
		return txRepository.findPayboxPapercutTransactionLogsByUidAndArchived(uid, archived, pageable);
	}

	public Page<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByUid(String uid, Pageable pageable) {
		return txRepository.findPayboxPapercutTransactionLogsByUid(uid, pageable);
	}

	public Optional<PayboxPapercutTransactionLog> findById(Long id) {
		return txRepository.findById(id);
	}

	public Page<PayboxPapercutTransactionLog> findAllPayboxPapercutTransactionLogs(Pageable pageable) {
		return txRepository.findAll(pageable);
	}
    
	/*
	 * 
	 * Requêtes pour PostgreSQL : corespondent aux préférences "requetes***" positionnées à la valeur "useOriginal"
	Pour adapter les requêtes à un autre SGBD, changer la valeur "useOriginal" par la requête directement
	*
	*/
    public List<Object>  countNumberTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, count(*) as count FROM paybox_papercut_transaction_log GROUP BY year, month ORDER BY year,month";
    	}

		Query q = entityManager.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public List<Object>  countMontantTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(CAST(montant AS decimal)) as totalMois FROM paybox_papercut_transaction_log GROUP BY year, month ORDER BY year,month";
    	}

		Query q = entityManager.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public List<Object>  countCumulNombreTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month, sum(count(* )) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM paybox_papercut_transaction_log Where date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from paybox_papercut_transaction_log) GROUP BY year, month ORDER BY year,month";
    	}

		Query q = entityManager.createNativeQuery(requete);

        return q.getResultList();
    }
    
    public List<Object>  countCumulMontantTranscationsBydate(String requete) {
    	if(requete.equals("useOriginal")){
    		requete = "SELECT date_part('year',transaction_date) as year, date_part('month',transaction_date) as month ,sum( sum(CAST(montant AS decimal))) OVER (PARTITION BY date_part('year',transaction_date) ORDER BY  date_part('year',transaction_date),date_part('month',transaction_date)) as cumul FROM paybox_papercut_transaction_log Where date_part('year',transaction_date) in (select distinct date_part('year',transaction_date) from paybox_papercut_transaction_log) GROUP BY year, month ORDER BY year,month";
    	} 	

		Query q = entityManager.createNativeQuery(requete);

        return q.getResultList();
    }

    
}
