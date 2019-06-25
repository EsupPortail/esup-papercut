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

import java.util.Collections;

import org.esupportail.papercut.config.EsupPapercutContext;
import org.esupportail.papercut.config.IzlyPayConfig;
import org.esupportail.papercut.domain.izlypay.IzlyPayment;
import org.esupportail.papercut.domain.izlypay.IzlyPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IzlyPayService extends PayService implements InitializingBean {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private RestTemplate restTemplate;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(mapper);
	    restTemplate.getMessageConverters().add(0, converter);
	}

	public String getIzlyPayUrl(EsupPapercutContext context, String uid, String mail, Integer montant, String contextPath) {

		if(context.getIzlypay() == null) {
			return null;
		}

		String clientOrderId = getNumCommande(context, uid, montant);

		IzlyPayment izlyPayment = new IzlyPayment();
		izlyPayment.setAmount(montant);
		izlyPayment.setCallbackParameters("");
		izlyPayment.setClientOrderId(clientOrderId);
		izlyPayment.setMessage(String.format("Paiement esup-papercut %s - %s", context.getIzlypay().getIdentifier(), context.getPapercutContext()));
		izlyPayment.setRedirectUrlAfterPayment(String.format("%s/%s", context.getIzlypay().getReponseServerUrl(), context.getPapercutContext()));

		HttpEntity<IzlyPayment> request = new HttpEntity<>(izlyPayment, getJsonHeaders(context.getIzlypay()));


		String webPaymentsUrl = String.format("%s/api/Izly/WebPayments", context.getIzlypay().getUrl());
		ResponseEntity<IzlyPaymentResponse> izlyPaymentResponseEntity = restTemplate.postForEntity(webPaymentsUrl, request, IzlyPaymentResponse.class);

		return izlyPaymentResponseEntity.getBody().getPaymentAuthorizationUrl();
	}

	private HttpHeaders getJsonHeaders(IzlyPayConfig izlyPayConfig) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/vnd.s-money.v1+json");
		headers.set("Authorization", "Basic " + izlyPayConfig.getKey());
		headers.set("AppIdentifier", izlyPayConfig.getIdentifier());
		return headers;
	}



}
