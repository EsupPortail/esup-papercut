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
package org.esupportail.papercut.dao;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.esupportail.papercut.security.ContextHelper;
import org.esupportail.papercut.services.EsupPaperCutService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PapercutDaoServiceAspect {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	  @Before("execution(* org.esupportail.papercut.dao.PapercutDaoService.*(..)) && !execution(* org.esupportail.papercut.dao.PapercutDaoService.run(..)) && target(papercutDaoService)")
	  public void aroundExecution(JoinPoint pjp, PapercutDaoService papercutDaoService) throws Throwable {
	    org.hibernate.Filter filter = papercutDaoService.entityManager.unwrap(Session.class).enableFilter("contextFilter");
	    filter.setParameter("paperCutContext", ContextHelper.getCurrentContext());
	    filter.validate();
	  }
	
}
