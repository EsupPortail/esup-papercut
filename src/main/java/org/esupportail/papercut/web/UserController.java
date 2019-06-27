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
package org.esupportail.papercut.web;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.esupportail.papercut.config.EsupPapercutConfig;
import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.dao.PapercutDaoService;
import org.esupportail.papercut.domain.IzlyPayForm;
import org.esupportail.papercut.domain.PayBoxForm;
import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.domain.PayPapercutTransactionLog.PayMode;
import org.esupportail.papercut.domain.UserPapercutInfos;
import org.esupportail.papercut.security.ContextHelper;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/{papercutContext}/user")
public class UserController {
	

    final static Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Resource 
	EsupPaperCutService esupPaperCutService;
	
	@Resource
	EsupPapercutConfig config;
	
	@Autowired
	PapercutDaoService papercutDaoService;
	
	@Autowired
	MessageSource messageSource;
	   
	@GetMapping(produces = "text/html")
    public String userView(HttpServletRequest request, Model uiModel) {	

		EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		if(context == null) {
			return "redirect:/";
		}
    	
    	double papercutSheetCost = context.getPapercutSheetCost();
    	double papercutColorSheetCost = context.getPapercutColorSheetCost();
        
        String uid = getUid();
        String userMail = getUserMail();
        
        // check if the user can make a transaction
        int transactionNbMax = context.getTransactionNbMax();
        int transactionMontantMax  = context.getTransactionMontantMax();
        boolean canMakeTransaction = true;
	
        // constraints via transactionNbMax
    	if(transactionNbMax>-1) {
    		long nbTransactionsNotArchived = papercutDaoService.countByUidAndArchived(uid, false);   			
    		if(transactionNbMax<=nbTransactionsNotArchived) {
    			canMakeTransaction = false;
    		}
    	}		
        
    	Integer montantMin = context.getMontantMin();
    	Integer montantMax  = context.getMontantMax();
    	Integer montantStep  = context.getMontantStep();	
    	
        // constraints via transactionMontantMax
        if(canMakeTransaction && transactionMontantMax > -1) {  	
			Page<PayPapercutTransactionLog> transactionsNotArchived = papercutDaoService.findPayPapercutTransactionLogsByUidAndArchived(uid, false, PageRequest.of(0, Integer.MAX_VALUE));
			Integer montantTotalTransactionsNotArchived = 0;
			for(PayPapercutTransactionLog txLog: transactionsNotArchived.getContent()) {
				montantTotalTransactionsNotArchived = montantTotalTransactionsNotArchived + txLog.getMontant();
			}
			transactionMontantMax = transactionMontantMax - montantTotalTransactionsNotArchived;
        	if(transactionMontantMax < montantMax) {
        		montantMax = transactionMontantMax;
        		if(montantMax.compareTo(montantMin) == -1) {
        			canMakeTransaction = false;
        		}
            	uiModel.addAttribute("payboxMontantMax", montantMax.doubleValue());
        	}
        }
	       
        // generation de l'ensemble des payboxForm :  payboxMontantMin -> payboxMontantMax par pas de payboxMontantStep
        String contextPath = request.getContextPath();
        
        if(esupPaperCutService.getPayModes(context).contains(PayMode.PAYBOX)) {
	        Map<Integer, PayBoxForm> payboxForms = new TreeMap<Integer, PayBoxForm>(); 
	        for(Integer montant=montantMin; montant.compareTo(montantMax)<=0; montant = montant + montantStep) {
	        	PayBoxForm payBoxForm = esupPaperCutService.getPayBoxForm(context, uid, userMail, montant, contextPath);
		        payBoxForm.setToolTip(getToolTipMessage(payBoxForm.getMontant(), montant, papercutSheetCost, papercutColorSheetCost));
		        payboxForms.put(montant, payBoxForm);
	        }    
	        uiModel.addAttribute("payboxForms", payboxForms);
        }
        
        if(esupPaperCutService.getPayModes(context).contains(PayMode.IZLYPAY)) {
	        Map<Integer, IzlyPayForm> izlypayForms = new TreeMap<Integer, IzlyPayForm>(); 
	        for(Integer montant=montantMin; montant.compareTo(montantMax)<=0; montant = montant + montantStep) {
	        	IzlyPayForm izlypayForm = new IzlyPayForm();
	        	izlypayForm.setMontant(new Double(new Double(montant)/100.0).toString());
		        izlypayForm.setToolTip(getToolTipMessage(izlypayForm.getMontant(), montant, papercutSheetCost, papercutColorSheetCost));
		        izlypayForms.put(montant, izlypayForm);
	        }        
	        uiModel.addAttribute("izlypayForms", izlypayForms);
        }
        if(esupPaperCutService.getPayModes(context).contains(PayMode.PAYBOX) || esupPaperCutService.getPayModes(context).contains(PayMode.IZLYPAY)) {
        	uiModel.addAttribute("tipMessage4MontantMin", getToolTipMessage(new Double(new Double(montantMin)/100.0).toString(), montantMin, papercutSheetCost, papercutColorSheetCost));
        }
        
        uiModel.addAttribute("canMakeTransaction", canMakeTransaction);

    	UserPapercutInfos userPapercutInfos = esupPaperCutService.getUserPapercutInfos(context, uid);
		uiModel.addAttribute("userPapercutInfos", userPapercutInfos);

    	uiModel.addAttribute("isAdmin", WebUtils.isAdmin());
    	uiModel.addAttribute("isManager", WebUtils.isManager());
    	uiModel.addAttribute("active", "home");
    	
    	uiModel.addAttribute("papercutContext", context);
    	
    	return "index";
    }
	
	
	private String getToolTipMessage(String montantAsString, Integer montant, double papercutSheetCost, double papercutColorSheetCost) {
    	int nbSheets = -1;
    	int nbColorSheets = -1;
    	if(papercutSheetCost > 0) {
        	nbSheets = (int)(montant/papercutSheetCost);
        }
        if(papercutColorSheetCost > 0) {
        	nbColorSheets = (int)(montant/papercutColorSheetCost);
        }
		if(nbSheets > 0 && nbColorSheets > 0) {
			return messageSource.getMessage("pay.credit-tooltip", new String[] {montantAsString, String.valueOf(nbSheets), String.valueOf(nbColorSheets)}, null);
		} else if(nbSheets > 0) {
			return messageSource.getMessage("pay.credit-tooltip-black", new String[] {montantAsString, String.valueOf(nbSheets)}, null);
		} if(nbColorSheets > 0) {
			return messageSource.getMessage("pay.credit-tooltip-color", new String[] {montantAsString, String.valueOf(nbColorSheets)}, null);
		}
		return null;
	}


	@PostMapping(value = "izlypay", produces = "text/html")
	public ModelAndView izlypayForm(@RequestParam Integer montant) {	

		EsupPapercutContext context = config.getContext(ContextHelper.getCurrentContext());
		if(context == null) {
			new ModelAndView("redirect:/");
		}
		
		String uid = getUid();
		String userMail = getUserMail();
		
		String izlyPayUrl = esupPaperCutService.getIzlyPayUrl(context, uid, userMail, montant, ContextHelper.getCurrentContext());

		return new ModelAndView("redirect:" + izlyPayUrl);
	}
	

    
    @GetMapping(value = "/logs", produces = "text/html")
    public String myhistoryList(@PageableDefault(size = 10, direction = Direction.DESC, sort = "transactionDate") Pageable pageable,
    		Model uiModel) {
    	
        String uid = getUid();
  
        uiModel.addAttribute("pageLogs", papercutDaoService.findPayPapercutTransactionLogsByUid(uid, pageable));
        uiModel.addAttribute("active", "history");
    	
        uiModel.addAttribute("active", "logs"); 	
        return "history";
    }
	
    
    @GetMapping(value = "/logs", produces = "text/html", params = {"id"})
    public String viewTransactionLog(@RequestParam Long id, Model uiModel) {
    	uiModel.addAttribute("plog", papercutDaoService.findById(id));
    	uiModel.addAttribute("itemId", id);
    	uiModel.addAttribute("active", "logs"); 	
        return "show-transactionlog";
    }

    private String getUid() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

    
	private String getUserMail() {
		//String mailAttr = request.getPreferences().getValue(PREF_PAPERCUT_USER_MAIL_ATTR, null);
		//Map<String,String> userinfo = (Map<String,String>)request.getAttribute(PortletRequest.USER_INFO);
		//String mail = userinfo.get(mailAttr);
		// TODO
		return "toto@univ-ville.fr";
	}

		
    /**
     * When user is redirected on esup-papercut after the paybox process, 
     * and if validatePayboxJustWithRedirection is true,
     * we validate the transaction regarding informations on queryString 
     * (there is a paybox signature that secures this, 
     * so we can do that even if we can consider it "less" secure than the direct call of paybox) 
     */
    @RequestMapping(params="signature")
    public String renderViewAfterPaybox(@PathVariable String papercutContext, @RequestParam(required=false) Integer montant, @RequestParam String reference, @RequestParam(required=false) String auto, 
    		@RequestParam String erreur, @RequestParam String idtrans, @RequestParam String signature) {	
    	
    	EsupPapercutContext context = config.getContext(papercutContext);
    	
    	if("true".equals(context.getValidatePayboxJustWithRedirection())) {
	    	String uid = getUid();
	    	String queryString = getQueryString(montant, reference, auto, erreur, idtrans, signature);

	    	log.debug(queryString);
	    	
	    	esupPaperCutService.payboxCallback(context, montant, reference, auto, erreur, idtrans, signature, queryString, null, uid);
    	}
    	
    	return "redirect:/user";
    }


    private static String getQueryString(Integer montant, String reference,
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
    
} 

