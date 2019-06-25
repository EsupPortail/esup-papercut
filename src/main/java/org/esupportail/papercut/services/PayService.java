package org.esupportail.papercut.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.esupportail.papercut.config.EsupPapercutContext;

public abstract class PayService {
	
	protected String getNumCommande(EsupPapercutContext context, String uid, Integer montant) {
		if(context.getPaybox() != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-S");
			return context.getNumCommandePrefix() + uid + "@" + context.getPapercutContext() + "@" + montant + "-" + df.format(new Date());
		}
		return null;
	}

}
