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
package org.esupportail.papercut.domain.izlypay;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IzlyPayOperationSMoney {

	Integer id;
	
	String amount;
	
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	Date date;
	
	String message;
	
	String clientOrderId;
	
	String status;

	String callbackParameters;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCallbackParameters() {
		return callbackParameters;
	}

	public void setCallbackParameters(String callbackParameters) {
		this.callbackParameters = callbackParameters;
	}

	public Integer getAmountAsInteger() {
		// Amount est de la forme 200.00 pour 200 centimes
		Double amountDble = Double.valueOf(getAmount());
		return Integer.valueOf(amountDble.intValue());
	}
	
}
