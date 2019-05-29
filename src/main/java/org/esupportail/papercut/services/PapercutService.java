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
package org.esupportail.papercut.services;

import java.util.Vector;

import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.papercutapi.ServerCommandProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PapercutService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	// TODO : optimsiation avec map en cache
	ServerCommandProxy getServerCommandProxy(EsupPapercutContext context) {
		return new ServerCommandProxy(context.getPapercut().getServer(), context.getPapercut().getScheme(), context.getPapercut().getPort(), context.getPapercut().getAuthToken());
	}
    
    public UserPapercutInfos getUserPapercutInfos(EsupPapercutContext context, String uid) {
    	ServerCommandProxy serverProxy = getServerCommandProxy(context);
    	if(!serverProxy.isUserExists(uid)) {
    		serverProxy.addNewUser(uid);
    	}
    	Vector<String> propertyValues = serverProxy.getUserProperties(uid, UserPapercutInfos.propertyNames);
    	UserPapercutInfos userPapercutInfos = new UserPapercutInfos(uid, propertyValues);
    	log.debug("userPapercutInfos de " + uid + " = " + userPapercutInfos);
    	return userPapercutInfos;
    }
    
    public void creditUserBalance(EsupPapercutContext context, String uid, double amount, String idtrans) {
    	ServerCommandProxy serverProxy = getServerCommandProxy(context);
    	String logMessage = "Ajout de " + amount + " à " + uid + " via l'appli Esup-Papercut - transaction paybox : " + idtrans;
    	log.info(logMessage);
    	serverProxy.adjustUserAccountBalance(uid, amount, logMessage, context.getPapercut().getAccountName());
    }

	
}
