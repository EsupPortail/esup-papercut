package org.esupportail.papercut.domain.izlypay;

public class IzlyPayCallBack {

	private IzlyPayOperationSMoney operationSMoney;
	
	private IzlyPayBeneficiary beneficiary;
	
	private IzlyPayUser user;
	
	private IzlyPayError error;

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

	public IzlyPayUser getUser() {
		return user;
	}

	public void setUser(IzlyPayUser user) {
		this.user = user;
	}

	public IzlyPayError getError() {
		return error;
	}

	public void setError(IzlyPayError error) {
		this.error = error;
	}
	
}
