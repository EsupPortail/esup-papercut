package org.esupportail.papercut.services;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.papercutapi.ServerCommandProxy;
import org.springframework.beans.factory.InitializingBean;

public class PapercutService implements InitializingBean {

	private final Logger log = Logger.getLogger(getClass());

	String authToken;
	
	String server;
	
	int port;
	
    ServerCommandProxy serverProxy;    

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void afterPropertiesSet() {
		serverProxy = new ServerCommandProxy(server, port, authToken);
	}
    
    public UserPapercutInfos getUserPapercutInfos(String uid) {
    	Vector<String> propertyValues = serverProxy.getUserProperties(uid, UserPapercutInfos.propertyNames);
    	UserPapercutInfos userPapercutInfos = new UserPapercutInfos(uid, propertyValues);
    	log.debug("userPapercutInfos de " + uid + " = " + userPapercutInfos);
    	return userPapercutInfos;
    }
    
    public void creditUserBalance(String uid, double amount, String idtrans) {
    	String logMessage = "Ajout de " + amount + " à " + uid + " via l'appli Esup-Papercut - transaction paybox : " + idtrans;
    	log.info(logMessage);
    	serverProxy.adjustUserAccountBalance(uid, amount, logMessage);
    }

	
}
