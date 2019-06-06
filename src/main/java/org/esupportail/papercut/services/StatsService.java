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

import java.util.LinkedHashMap;
import java.util.List;

import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.security.ContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
	
	@Autowired
	private PapercutDaoService papercutDaoService;

	@SuppressWarnings("serial")
	public  LinkedHashMap<String,Object> getStatsPapercut(EsupPapercutContext context) {
    	
		ContextHelper.getCurrentContext();
		
		//on remplit le futur json
		LinkedHashMap<String, Object> results = new LinkedHashMap<String, Object>() {
	        {
	            put("nombre", mapField(countNumberTranscationsBydate(context.getRequeteNbTransactions()),3));
	            put("montants", mapField(countMontantTranscationsBydate(context.getRequeteMontantTransactions()),3));
	            put("cumulTransac", mapField(countCumulNombreTranscationsBydate(context.getRequeteCumulTransactions()),3));
	            put("cumulMontant", mapField(countCumulMontantTranscationsBydate(context.getRequeteCumulMontants()),3));
	        }
	    };	
		return results;
    }
	
    public LinkedHashMap<String, Object> mapField(List<Object> listes, int level){
    	
    	LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
    	
    	LinkedHashMap<String, Object> secondMap = new LinkedHashMap<String, Object>();
    	
    	String test = null;
    	int i = 1;
    	
   		for (Object result : listes) {
   			Object[] r = (Object[]) result;	
   			String annee = String.valueOf(Math.round(Double.parseDouble(r[0].toString())));
   			String mois = String.valueOf(Math.round(Double.parseDouble(r[1].toString())));
   			if (level == 2){
	   		    map.put(annee,r[1]);
   			}else{
   				if(test== null || test.equals(annee)){
   					secondMap.put(mois,r[2]);
   				}else{
   					map.put(test, secondMap);
   					secondMap = new LinkedHashMap<String, Object>();
   					secondMap.put(mois,r[2]);
   				}
   				test = annee;
   				if(i ==  listes.size()){
   					map.put(test, secondMap);
   				}
   				i++;
   			}
   		}
        return map;
    }

    protected List<Object>  countNumberTranscationsBydate(String requeteNbTransactions) {
        return papercutDaoService.countNumberTranscationsBydate(requeteNbTransactions);
    }
    protected List<Object>  countMontantTranscationsBydate(String requeteMontantTransactions) {
        return papercutDaoService.countMontantTranscationsBydate(requeteMontantTransactions);
    }
    protected List<Object>  countCumulNombreTranscationsBydate(String requeteCumulTransactions) {
        return papercutDaoService.countCumulNombreTranscationsBydate(requeteCumulTransactions);
    }
    protected List<Object>  countCumulMontantTranscationsBydate(String requeteCumulMontants) {
        return papercutDaoService.countCumulMontantTranscationsBydate(requeteCumulMontants);
    }
}
