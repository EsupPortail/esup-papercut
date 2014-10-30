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
package org.esupportail.papercut.webportlet;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.esupportail.papercut.domain.EsupPapercutSessionObject;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.esupportail.papercut.services.UserAgentInspector;
import org.hibernate.ejb.criteria.ValueHandlerFactory.DoubleValueHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.util.PortletUtils;


@Controller
@Scope("request")
@RequestMapping("VIEW")
public class EsupPapercutPortletController {

	static Logger log = Logger.getLogger(EsupPapercutPortletController.class);
	
	private final static String PREF_PAPERCUT_CONTEXT = "paperCutContext";
	
	private final static String PREF_PAPERCUT_USER_UID_ATTR = "papercutUserUidAttr";
	
	private final static String PREF_PAPERCUT_USER_MAIL_ATTR = "userEmail";
	
	private final static String PREF_VALIDATE_AFTER_REDIRECT = "validatePayboxJustWithRedirection";
	
	@Resource 
	Map<String, EsupPaperCutService> esupPaperCutServices;
	
    @Resource
    protected UserAgentInspector userAgentInspector;	
	   
    @RequestMapping
    public ModelAndView renderView(RenderRequest request, RenderResponse response) {	

    	ModelMap model = new ModelMap();  
    	
    	model.put("htmlHeader", request.getPreferences().getValue("htmlHeader", ""));
    	model.put("htmlFooter", request.getPreferences().getValue("htmlFooter", ""));
    	model.put("payboxMontantMin", request.getPreferences().getValue("payboxMontantMin", "0.5"));
    	model.put("payboxMontantMax", request.getPreferences().getValue("payboxMontantMax", "5.0"));
    	model.put("payboxMontantStep", request.getPreferences().getValue("payboxMontantStep", "0.5"));	
    	model.put("payboxMontantDefaut", request.getPreferences().getValue("payboxMontantDefaut", "2.0"));
    	
    	double papercutSheetCost = Double.parseDouble(request.getPreferences().getValue("papercutSheetCost", "-1"));
    	double papercutColorSheetCost = Double.parseDouble(request.getPreferences().getValue("papercutColorSheetCost", "-1"));
    	
    	String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);
        EsupPaperCutService esupPaperCutService = esupPaperCutServices.get(paperCutContext);
        
        String uid = getUid(request);
        String userMail = getUserMail(request);
        
        // check if the user can make a transaction
        int transactionNbMax = Integer.parseInt(request.getPreferences().getValue("transactionNbMax", "-1"));
        BigDecimal transactionMontantMax  = new BigDecimal(request.getPreferences().getValue("transactionMontantMax", "-1"));
        boolean canMakeTransaction = true;
	
        // constraints via transactionNbMax
    	if(transactionNbMax>-1) {
    		long nbTransactionsNotArchived = PayboxPapercutTransactionLog.countFindPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEqualsAndArchived(uid, paperCutContext, false);   			
    		if(transactionNbMax<=nbTransactionsNotArchived) {
    			canMakeTransaction = false;
    		}
    	}		
        
    	BigDecimal payboxMontantMin = new BigDecimal(request.getPreferences().getValue("payboxMontantMin", "0.5"));
    	BigDecimal payboxMontantMax  = new BigDecimal(request.getPreferences().getValue("payboxMontantMax", "5.0"));
    	BigDecimal payboxMontantStep  = new BigDecimal(request.getPreferences().getValue("payboxMontantStep", "0.5"));	
    	BigDecimal payboxMontantDefaut  = new BigDecimal(request.getPreferences().getValue("payboxMontantDefaut", "2.0"));
        // constraints on the slider via transactionMontantMax
        if(canMakeTransaction && transactionMontantMax.intValue() > -1) {  	
			List<PayboxPapercutTransactionLog> transactionsNotArchived = PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEqualsAndArchived(uid, paperCutContext, false).getResultList();
			BigDecimal montantTotalTransactionsNotArchived = new BigDecimal("0");
			for(PayboxPapercutTransactionLog txLog: transactionsNotArchived) {
				montantTotalTransactionsNotArchived = montantTotalTransactionsNotArchived.add(new BigDecimal(txLog.getMontant()));
			}
			transactionMontantMax = transactionMontantMax.multiply(new BigDecimal("100")).subtract(montantTotalTransactionsNotArchived);
        	if(transactionMontantMax.doubleValue() < payboxMontantMax.doubleValue()*100) {
        		payboxMontantMax = transactionMontantMax.divide(payboxMontantStep).multiply(payboxMontantStep);
        		payboxMontantMax = payboxMontantMax.divide(new BigDecimal("100"));
        		if(payboxMontantDefaut.compareTo(payboxMontantMax) == 1) {
        			payboxMontantDefaut = payboxMontantMax;
        		}
        		if(payboxMontantMax.compareTo(payboxMontantMin) == -1) {
        			canMakeTransaction = false;
        		}
            	model.put("payboxMontantMax", payboxMontantMax.doubleValue());
            	model.put("payboxMontantDefaut", payboxMontantDefaut.doubleValue());
        	}
        }
	       
        // generation de l'ensemble des payboxForm :  payboxMontantMin -> payboxMontantMax par pas de payboxMontantStep
        String portletContextPath = ((RenderResponse)response).createRenderURL().toString();
        Map<Integer, PayBoxForm> payboxForms = new HashMap<Integer, PayBoxForm>(); 
        for(BigDecimal montant=payboxMontantMin; montant.compareTo(payboxMontantMax)<=0; montant = montant.add(payboxMontantStep)) {
        	PayBoxForm payBoxForm = esupPaperCutService.getPayBoxForm(uid, userMail, montant.doubleValue(), paperCutContext, portletContextPath);
        	if(papercutSheetCost > 0) {
        		int nbSheets = (int)(montant.doubleValue()/papercutSheetCost);
        		payBoxForm.setNbSheets(nbSheets);
        	}
        	if(papercutColorSheetCost > 0) {
        		int nbColorSheets = (int)(montant.doubleValue()/papercutColorSheetCost);
        		payBoxForm.setNbColorSheets(nbColorSheets);
        	}
        	
        	payboxForms.put(montant.multiply(new BigDecimal(100)).intValue(), payBoxForm);
        }
        Map<Integer, PayBoxForm> sortedMap = new TreeMap<Integer, PayBoxForm>(payboxForms);
        
        model.put("payboxForms", sortedMap);
        model.put("payboxMontantDefautCents", payboxMontantDefaut.multiply(new BigDecimal(100)).intValue());
        
        model.put("canMakeTransaction", canMakeTransaction);
    	
    	UserPapercutInfos userPapercutInfos = esupPaperCutService.getUserPapercutInfos(uid);   		
		model.put("userPapercutInfos", userPapercutInfos);
    	
		boolean isAdmin = isAdmin(request);
    	model.put("isAdmin", isAdmin);
    	
    	return new ModelAndView(getViewName(request, "index"), model);
    }

    @RequestMapping(params = "action=admin")
    public ModelAndView adminList(@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size, 
    		@RequestParam(value = "sortFieldName", required = false) String sortFieldName, 
    		@RequestParam(value = "sortOrder", required = false) String sortOrder,
    		RenderRequest request, RenderResponse response) {
    	
    	boolean isAdmin = isAdmin(request);
    	
    	ModelMap model = new ModelMap();  
    	model.put("isAdmin", isAdmin);
    	model.put("adminView", true);
    	
    	if(isAdmin) {
	    	
	    	String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);

	    	if(sortFieldName == null) {
	    		sortFieldName = "transactionDate";
	    		sortOrder = "desc";
	    	}
	    	
	    	if (size == null) {
	    		size = 10;
	    	}
	    	
	    	if (page != null || size != null) {
	            int sizeNo = size == null ? 10 : size.intValue();
	            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
	            model.put("payboxpapercuttransactionlogs", PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByPaperCutContextEquals(paperCutContext, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
	            float nrOfPages = (float) PayboxPapercutTransactionLog.countFindPayboxPapercutTransactionLogsByPaperCutContextEquals(paperCutContext) / sizeNo;
	            model.put("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
	        } else {
	            model.put("payboxpapercuttransactionlogs", PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByPaperCutContextEquals(paperCutContext, sortFieldName, sortOrder).getResultList());
	        }
	        addDateTimeFormatPatterns(model);
	        
	    	String sharedSessionId = response.getNamespace();
	    	EsupPapercutSessionObject objectShared = new EsupPapercutSessionObject();
	    	objectShared.setIsAdmin(isAdmin);
	    	objectShared.setPaperCutContext(paperCutContext);
	    	PortletUtils.setSessionAttribute(request, sharedSessionId, objectShared, PortletSession.APPLICATION_SCOPE);
	    	model.put("sharedSessionId", sharedSessionId);
	    	
        
    	}
        
        return new ModelAndView(getViewName(request,"history"), model);
    }
    
    @RequestMapping(params = "action=myhistory")
    public ModelAndView myhistoryList(@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size, 
    		@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
    		@RequestParam(value = "sortOrder", required = false) String sortOrder,
    		RenderRequest request) {
    	
        
        String uid = getUid(request);
        String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);
        
    	if(sortFieldName == null) {
    		sortFieldName = "transactionDate";
    		sortOrder = "desc";
    	}
    	
    	if (size == null) {
    		size = 10;
    	}
    	
    	ModelMap model = new ModelMap();  
    	if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            model.put("payboxpapercuttransactionlogs", PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals(uid, paperCutContext, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) PayboxPapercutTransactionLog.countFindPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals(uid, paperCutContext) / sizeNo;
            model.put("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.put("payboxpapercuttransactionlogs", PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByUidEqualsAndPaperCutContextEquals(uid, paperCutContext, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(model);
        
    	model.put("isAdmin", isAdmin(request));
    	
        return new ModelAndView(getViewName(request,"history"), model);
    }
	
    
    @RequestMapping(params = "action=show")
    public ModelAndView viewTransactionLog(@RequestParam(value = "id", required = true) Long id, RenderRequest request) {
    	ModelMap model = new ModelMap();  
        addDateTimeFormatPatterns(model);
        model.put("payboxpapercuttransactionlog", PayboxPapercutTransactionLog.findPayboxPapercutTransactionLog(id));
        model.put("itemId", id);
    	model.put("isAdmin", isAdmin(request));
        return new ModelAndView("show-transactionlog", model);
    }

    
    void addDateTimeFormatPatterns(ModelMap model) {
        model.put("payboxPapercutTransactionLog_transactiondate_date_format", "dd/MM/yyyy-HH:mm:ss");
    }

    private String getUid(PortletRequest request) {
		String uidAttr = request.getPreferences().getValue(PREF_PAPERCUT_USER_UID_ATTR, null);
		Map<String,String> userinfo = (Map<String,String>)request.getAttribute(PortletRequest.USER_INFO);
		String uid = userinfo.get(uidAttr);
		return uid;
	}

    
	private String getUserMail(PortletRequest request) {
		String mailAttr = request.getPreferences().getValue(PREF_PAPERCUT_USER_MAIL_ATTR, null);
		Map<String,String> userinfo = (Map<String,String>)request.getAttribute(PortletRequest.USER_INFO);
		String mail = userinfo.get(mailAttr);
		return mail;
	}
	
	private boolean isAdmin(PortletRequest request) {
		return request.isUserInRole("esupPapercutAdmin");
	}
	
    /**
     * When user is redirected on the portlet after the paybox process, 
     * and if validatePayboxJustWithRedirection in pref portlet is true,
     * we validate the transaction regarding informations on queryString 
     * (there is a paybox signature that secures this, 
     * so we can do that even if we can consider it "less" secure than the direct call of paybox) 
     */
    @RequestMapping(params="signature")
    public ModelAndView renderViewAfterPaybox(@RequestParam(required=false) String montant, @RequestParam String reference, @RequestParam(required=false) String auto, 
    		@RequestParam String erreur, @RequestParam String idtrans, @RequestParam String signature, RenderRequest request, RenderResponse response) {	
    	
    	if("true".equals(request.getPreferences().getValue(PREF_VALIDATE_AFTER_REDIRECT, "false"))) {
	    	String uid = getUid(request);
	    	String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);
	    	String queryString = getQueryString(montant, reference, auto, erreur, idtrans, signature);

	    	log.debug(queryString);
	    	
	    	esupPaperCutServices.get(paperCutContext).payboxCallback(montant, reference, auto, erreur, idtrans, signature, queryString, null, uid);
    	}
    	
    	return renderView(request, response);
    }


    private static String getQueryString(String montant, String reference,
    		String auto, String erreur, String idtrans, String signature) {
    	StringBuilder sb = new StringBuilder();

    	if(montant != null) {
	    	sb.append(String.format("%s=%s", "montant", montant));
	    	sb.append("&");
    	}

    	sb.append(String.format("%s=%s", "reference", reference));
    	sb.append("&");

    	if(auto != null) {
	    	sb.append(String.format("%s=%s", "auto", auto));
	    	sb.append("&");
    	}

    	sb.append(String.format("%s=%s", "erreur", erreur));
    	sb.append("&");	

    	sb.append(String.format("%s=%s", "idtrans", idtrans));
    	sb.append("&");	

    	sb.append(String.format("%s=%s", "signature", signature));

    	return sb.toString();
    }
    
    
    @Transactional
    @RequestMapping(params = "action=archiveAll")  // action phase
    public void archiveAll(ActionRequest request, ActionResponse response) {

    	if( isAdmin(request)) {
    		List<PayboxPapercutTransactionLog> txLogs = PayboxPapercutTransactionLog.findPayboxPapercutTransactionLogsByArchived(false).getResultList();
    		for(PayboxPapercutTransactionLog txLog: txLogs) {
    			txLog.setArchived(true);
			}
    	}
    	
        response.setRenderParameter("action", "admin");
    }
    
    @Transactional
    @RequestMapping(params = "action=archive")  // action phase
    public void archive(@RequestParam(value = "txLogId", required = true) Long txLogId, ActionRequest request, ActionResponse response) {

    	if( isAdmin(request)) {
    		PayboxPapercutTransactionLog txLog =  PayboxPapercutTransactionLog.findPayboxPapercutTransactionLog(txLogId);
    		txLog.setArchived(true);
    	}
    	
    	Map<String, String[]> parameters = new HashMap<String, String[]>();
    	parameters.put("action", new String[] {"show"});
    	parameters.put("id", new String[] {String.valueOf(txLogId)});
    	
        response.setRenderParameters(parameters);
    }
    
    public String getViewName(PortletRequest request, String viewName){
        if(userAgentInspector.isMobile(request)) {
                viewName = "m_".concat(viewName);
        }
        return viewName;
    }

    
}

