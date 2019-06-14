package org.esupportail.papercut.domain.izlypay;

public class IzlyPayment {

	Integer amount;
	
	String clientOrderId;
	
	String callbackParameters;
	
	String redirectUrlAfterPayment;
	
	String message;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public String getCallbackParameters() {
		return callbackParameters;
	}

	public void setCallbackParameters(String callbackParameters) {
		this.callbackParameters = callbackParameters;
	}

	public String getRedirectUrlAfterPayment() {
		return redirectUrlAfterPayment;
	}

	public void setRedirectUrlAfterPayment(String redirectUrlAfterPayment) {
		this.redirectUrlAfterPayment = redirectUrlAfterPayment;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
