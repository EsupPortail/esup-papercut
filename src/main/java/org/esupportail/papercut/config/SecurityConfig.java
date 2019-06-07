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
package org.esupportail.papercut.config;

import java.util.Arrays;

import org.esupportail.papercut.security.ContextCasAuthenticationProvider;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationProvider authenticationProvider;
    
    private AuthenticationEntryPoint authenticationEntryPoint;
    
    private SingleSignOutFilter singleSignOutFilter;
    
    private LogoutFilter logoutFilter;

    @Autowired
    public SecurityConfig(ContextCasAuthenticationProvider casAuthenticationProvider, AuthenticationEntryPoint eP,
                          LogoutFilter lF
                          , SingleSignOutFilter ssF) {
        this.authenticationProvider = casAuthenticationProvider;
        this.authenticationEntryPoint = eP;
        this.logoutFilter = lF;
        this.singleSignOutFilter = ssF;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
		.anonymous().disable()
        .authorizeRequests()
        .regexMatchers("/login")
        .authenticated()
        .and()
        .authorizeRequests()
        .regexMatchers("/[^/]*/user(/.*|/?)")
        .hasRole("USER")
        .and()
        .authorizeRequests()
        .regexMatchers("/[^/]*/admin(/.*|/?)")
        .hasAnyRole("ADMIN", "MANAGER")
        .and()
        .authorizeRequests()
        .regexMatchers("/")
        .permitAll()
        .and()
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .logout().logoutSuccessUrl("/logout")
        .and()
        .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
        .addFilterBefore(logoutFilter, LogoutFilter.class)
        .csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
      return new ProviderManager(Arrays.asList(authenticationProvider));
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties sP) throws Exception {
      CasAuthenticationFilter filter = new CasAuthenticationFilter();
      filter.setServiceProperties(sP);
      filter.setAuthenticationManager(authenticationManager());
      return filter;
    }

}
