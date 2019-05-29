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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.esupportail.papercut.config.EsupPapercutContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

@Service
public class HashService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final static String ALGO_HSAH = "SHA512";

	public String getHash() {
		return ALGO_HSAH;
	}

	// TOSO optimisation : cache avec map
	public SecretKeySpec getSecretKey(String hmacKey) {
		return new SecretKeySpec(DatatypeConverter.parseHexBinary(hmacKey), "HmacSHA512" );		
	}

	public String getHMac(EsupPapercutContext context, String input) {
		try {
			Mac mac = Mac.getInstance("HmacSHA512");
			mac.init(getSecretKey(context.getPaybox().getHmacKey()));
			final byte[] macData = mac.doFinal(input.getBytes());
			char[] hex = new Hex().encode(macData);
			String hmac = new String(hex).toUpperCase();
			log.debug(input);
			log.debug(hmac);
			return hmac;
		} catch (Exception e) {
			log.error("Error during encoding data ...");
			throw new RuntimeException(e);
		}
	}


}
