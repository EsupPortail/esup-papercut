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

import java.util.Map;

import javax.annotation.Resource;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.log4j.Logger;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayboxPapercutTransactionLog;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

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
	
    @RequestMapping
    public ModelAndView renderView(RenderRequest request, RenderResponse response) {	

    	ModelMap model = new ModelMap();  
    	
    	String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);
        EsupPaperCutService esupPaperCutService = esupPaperCutServices.get(paperCutContext);
        
        String uid = getUid(request);
        String userMail = getUserMail(request);
    	
    	UserPapercutInfos userPapercutInfos = esupPaperCutService.getUserPapercutInfos(uid);   		
		model.put("userPapercutInfos", userPapercutInfos);
		
		String portletContextPath = response.createRenderURL().toString();

    	PayBoxForm payBoxForm = esupPaperCutService.getPayBoxForm(uid, userMail, 2, paperCutContext, portletContextPath);
    	model.put("payBoxForm", payBoxForm);
    	
    	model.put("isAdmin", isAdmin(request));
    	
    	return new ModelAndView("index", model);
    }


	@ResourceMapping("payboxFormMontant")
	public ModelAndView payboxFormMontant(@RequestParam double montant, ResourceRequest request, ResourceResponse response) {
    	
    	ModelMap model = new ModelMap();  
    	
    	String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);
        EsupPaperCutService esupPaperCutService = esupPaperCutServices.get(paperCutContext);
        
        String uid = getUid(request);
        String userMail = getUserMail(request);
		
        String portletContextPath = response.createRenderURL().toString();
		
    	PayBoxForm payBoxForm = esupPaperCutService.getPayBoxForm(uid, userMail, montant, paperCutContext, portletContextPath);
    	model.put("payBoxForm", payBoxForm);

	    return new ModelAndView("paybox-form-montant", model);
	 }


    
    
    @RequestMapping(params = "action=admin")
    public ModelAndView adminList(@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size, 
    		@RequestParam(value = "sortFieldName", required = false) String sortFieldName, 
    		@RequestParam(value = "sortOrder", required = false) String sortOrder,
    		RenderRequest request) {
    	
    	boolean isAdmin = isAdmin(request);
    	
    	ModelMap model = new ModelMap();  
    	model.put("isAdmin", isAdmin);
    	
    	if(isAdmin) {
	    	
	    	String paperCutContext = request.getPreferences().getValue(PREF_PAPERCUT_CONTEXT, null);

	    	if(sortFieldName == null) {
	    		sortFieldName = "transactionDate";
	    		sortOrder = "desc";
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
        
    	}
        
        return new ModelAndView("history", model);
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
    	
        return new ModelAndView("history", model);
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
    public ModelAndView renderViewAfterPaybox(@RequestParam String montant, @RequestParam String reference, @RequestParam String auto, 
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

    	sb.append(String.format("%s=%s", "montant", montant));
    	sb.append("&");

    	sb.append(String.format("%s=%s", "reference", reference));
    	sb.append("&");

    	sb.append(String.format("%s=%s", "auto", auto));
    	sb.append("&");

    	sb.append(String.format("%s=%s", "erreur", erreur));
    	sb.append("&");	

    	sb.append(String.format("%s=%s", "idtrans", idtrans));
    	sb.append("&");	

    	sb.append(String.format("%s=%s", "signature", signature));

    	return sb.toString();
    }
}

