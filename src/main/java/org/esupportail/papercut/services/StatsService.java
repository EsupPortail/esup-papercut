package org.esupportail.papercut.services;

import java.util.LinkedHashMap;
import java.util.List;

import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

	@SuppressWarnings("serial")
	public  LinkedHashMap<String,Object> getStatsPapercut() {
    	
		//on remplit le futur json
		LinkedHashMap<String, Object> results = new LinkedHashMap<String, Object>() {
	        {
	            put("nombre", mapField(PayboxPapercutTransactionLog.countNumberTranscationsBydate(),3));
	            put("montants", mapField(PayboxPapercutTransactionLog.countMontantTranscationsBydate(),3));
	            put("cumulTransac", mapField(PayboxPapercutTransactionLog.countCumulNombreTranscationsBydate(),3));
	            put("cumulMontant", mapField(PayboxPapercutTransactionLog.countCumulMontantTranscationsBydate(),3));
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
   			if (level == 2){
	   		    map.put(r[0].toString(),r[1]);
   			}else{
   				if(test== null || test.equals(r[0].toString())){
   					secondMap.put(r[1].toString(),r[2]);
   				}else{
   					map.put(test, secondMap);
   					secondMap = new LinkedHashMap<String, Object>();
   					secondMap.put(r[1].toString(),r[2]);
   				}
   				test = r[0].toString();
   				if(i ==  listes.size()){
   					map.put(test, secondMap);
   				}
   				i++;
   			}
   		}
        return map;
    }

    public List<Object>  countNumberTranscationsBydate() {
        return PayboxPapercutTransactionLog.countNumberTranscationsBydate();
    }
    public List<Object>  countMontantTranscationsBydate(String requete) {
        return PayboxPapercutTransactionLog.countMontantTranscationsBydate();
    }
    public List<Object>  countCumulNombreTranscationsBydate(String requete) {
        return PayboxPapercutTransactionLog.countCumulNombreTranscationsBydate();
    }
    public List<Object>  countCumulMontantTranscationsBydate() {
        return PayboxPapercutTransactionLog.countCumulMontantTranscationsBydate();
    }
}
