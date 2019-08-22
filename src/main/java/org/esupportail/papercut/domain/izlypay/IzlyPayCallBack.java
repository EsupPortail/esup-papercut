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
