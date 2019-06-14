package org.esupportail.papercut.domain.izlypay;

public class IzlyPaymentResponse {
	
	String paymentAuthorizationUrl;
	
	IzlyPayOperationSMoney operationSMoney;
	
	IzlyPayBeneficiary beneficiary;

	public String getPaymentAuthorizationUrl() {
		return paymentAuthorizationUrl;
	}

	public void setPaymentAuthorizationUrl(String paymentAuthorizationUrl) {
		this.paymentAuthorizationUrl = paymentAuthorizationUrl;
	}

	public IzlyPayOperationSMoney getOperationSMoney() {
		return operationSMoney;
	}

	public void setOperationSMoney(IzlyPayOperationSMoney operationSMoney) {
		this.operationSMoney = operationSMoney;
	}

	public IzlyPayBeneficiary getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(IzlyPayBeneficiary beneficiary) {
		this.beneficiary = beneficiary;
	}
	
}
