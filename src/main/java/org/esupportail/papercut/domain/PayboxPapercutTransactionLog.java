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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals", "findPayboxPapercutTransactionLogsByPaperCutContextEquals", "findPayboxPapercutTransactionLogsByIdtransEquals" })
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
    
    public String getMontantDevise() {
    	Double mnt = new Double(montant)/100.0;
    	return mnt.toString();
    }
}
