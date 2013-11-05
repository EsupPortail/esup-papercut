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
