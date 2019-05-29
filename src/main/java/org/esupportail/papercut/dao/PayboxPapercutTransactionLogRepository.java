package org.esupportail.papercut.dao;

import java.util.List;

import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayboxPapercutTransactionLogRepository extends JpaRepository<PayboxPapercutTransactionLog, Long>{

	Long countByIdtrans(String idTrans);
	
	Long countByArchivedAndPaperCutContext(Boolean archived, String paperCutContext);

	Long countByPaperCutContext(String paperCutContext);

	Long countByUidAndPaperCutContext(String uid, String paperCutContext);

	Long countByUidAndPaperCutContextAndArchived(String uid, String paperCutContext, Boolean archived);
	
	List<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByIdtrans(String idTrans, Pageable pageable);

	List<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByArchivedAndPaperCutContext(Boolean archived, String paperCutContext, Pageable pageable);
	
	List<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByPaperCutContext(String paperCutContext, Pageable pageable);

	List<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByUidAndPaperCutContext(String uid, String paperCutContext, Pageable pageable);

	List<PayboxPapercutTransactionLog> findPayboxPapercutTransactionLogsByUidAndPaperCutContextAndArchived(String uid, String paperCutContext, Boolean archived, Pageable pageable);

}