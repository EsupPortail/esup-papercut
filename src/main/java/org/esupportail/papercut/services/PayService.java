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
