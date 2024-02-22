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
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.esupportail.papercut.security.ContextHelper;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Aspect
@Component
public class PapercutDaoServiceAspect {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private EntityManager em;
	
	  // Attention, ne fonctionne pas sur les native query ...
	  // De même cf doc hibernate "Filters apply to entity queries, but not to direct fetching."
	  @Before("execution(* org.springframework.data.jpa.repository.*.*(..)) || execution(* org.esupportail.papercut.dao.*.*(..))")
	  public void aroundExecution() {
	    org.hibernate.Filter filter = em.unwrap(Session.class).enableFilter("contextFilter");
	    filter.setParameter("papercutContext", ContextHelper.getCurrentContext());
	    filter.validate();
	  }

	@After("execution(* org.springframework.data.jpa.repository.*.*(..)) || execution(* org.esupportail.papercut.dao.*.*(..))")
	public void disableFilter() {
		em.unwrap(Session.class).disableFilter("contextFilter");
	}
	
}
