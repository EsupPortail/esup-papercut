package org.esupportail.papercut.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayBoxCallbackController {

	private final Logger log = Logger.getLogger(getClass());
	
	@Resource 
	Map<String, EsupPaperCutService> esupPaperCutServices;

	
    /** 
     * Manage callback response from paybox - url like :  
     * 
     * /payboxcallback?montant=200&reference=bonamvin@univrouen@200-2013-08-23-17-08-18-394&auto=XXXXXX&erreur=00000&idtrans=3608021&signature=CPqq18Un24NL0llB3E3G9kbKI4ztlkoL%2BSRTnMMrWlPBTVNTsn%2B%2FxA0YMSQOGGnU0wm45HYh%2F2RHoZGG3THzj7xKSY6upNJcnKrfFmzfTgA5FTFA3dyM27RgKmLcCeH48FRNoZPjVsKk0G2npvaP%2FY5pkSvn%2BQUl34DkmJkTejs%3D
     *
     * @param uiModel
     * @return empty page
     */
	@RequestMapping("/payboxcallback")
    @ResponseBody
    public ResponseEntity<String> index(@RequestParam String montant, @RequestParam String reference, @RequestParam String auto, 
    		@RequestParam String erreur, @RequestParam String idtrans, @RequestParam String signature, 
    		HttpServletRequest request) {
		
		String paperCutContext = reference.split("@")[1];
		String ip = request.getRemoteAddr();
		String queryString = request.getQueryString();
		
		if(esupPaperCutServices.get(paperCutContext).payboxCallback(montant, reference, auto, erreur, idtrans, signature, queryString, ip, null)) {
    		HttpHeaders headers = new HttpHeaders();
    		headers.add("Content-Type", "text/html; charset=utf-8");
    		return new ResponseEntity<String>("", headers, HttpStatus.OK);
		} else {
    		HttpHeaders headers = new HttpHeaders();
    		headers.add("Content-Type", "text/html; charset=utf-8");
    		return new ResponseEntity<String>("", headers, HttpStatus.FORBIDDEN);
		}
    }
}
