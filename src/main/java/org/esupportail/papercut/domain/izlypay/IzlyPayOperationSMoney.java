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
