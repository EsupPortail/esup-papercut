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

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.esupportail.papercut.security.ContextCasAuthenticationProvider;
import org.esupportail.papercut.security.ContextFilter;
import org.apereo.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AuthenticationProvider authenticationProvider;
    
    private final AuthenticationEntryPoint authenticationEntryPoint;
    
    private final SingleSignOutFilter singleSignOutFilter;
    
    private final LogoutFilter logoutFilter;

    @Autowired
    public SecurityConfig(ContextCasAuthenticationProvider casAuthenticationProvider, AuthenticationEntryPoint eP,
                          LogoutFilter lF, SingleSignOutFilter ssF) {
        this.authenticationProvider = casAuthenticationProvider;
        this.authenticationEntryPoint = eP;
        this.logoutFilter = lF;
        this.singleSignOutFilter = ssF;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CasAuthenticationFilter casAuthFilter) throws Exception {
      http
        .authorizeHttpRequests(authz -> authz
          .requestMatchers("/login", "/logout", "/webjars/**", "/resources/**", "/error").permitAll()
          .requestMatchers("/*/user", "/*/user/*").hasRole("USER")
          .requestMatchers("/*/api/csv-online").permitAll()
          .requestMatchers("/*/admin", "/*/admin/*").hasAnyRole("ADMIN", "MANAGER")
          .anyRequest().authenticated()
        )
        .exceptionHandling(exception -> exception
          .authenticationEntryPoint(authenticationEntryPoint)
        )
        .logout(logout -> logout
          .logoutSuccessUrl("/logout")
          .invalidateHttpSession(true)
        )
        .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
        .addFilterBefore(casAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(logoutFilter, CasAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable);

      return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
      return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties sP) throws Exception {
      logger.info("=== Initializing CasAuthenticationFilter ===");
      logger.info("Service URL: {}", sP.getService());

      CasAuthenticationFilter filter = new CasAuthenticationFilter();
      filter.setServiceProperties(sP);
      filter.setAuthenticationManager(authenticationManager());
      filter.setFilterProcessesUrl("/login");

      logger.info("CasAuthenticationFilter will process requests to: /login");

      filter.setAuthenticationFailureHandler((request, response, exception) -> {
        logger.error("CAS Authentication FAILED: {}", exception.getMessage(), exception);
        logger.info("Redirecting to / after auth failure");
        response.sendRedirect("/");
      });

      SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
      successHandler.setDefaultTargetUrl("/");
      successHandler.setAlwaysUseDefaultTargetUrl(false);

      filter.setAuthenticationSuccessHandler(successHandler);
      logger.info("=== CasAuthenticationFilter initialized ===");
      return filter;
    }

    /**
     * Configure l'enregistrement du ContextFilter pour qu'il s'exécute en premier.
     * Ordre 0 garantit qu'il s'exécute avant tous les autres filtres.
     */
    @Bean
    public FilterRegistrationBean<ContextFilter> contextFilterRegistration(ContextFilter filter) {
      FilterRegistrationBean<ContextFilter> registration = new FilterRegistrationBean<>(filter);
      registration.setOrder(0);
      registration.addUrlPatterns("/*");
      return registration;
    }

    /**
     * Désactive l'enregistrement automatique du CasAuthenticationFilter pour éviter qu'il soit appelé deux fois.
     * Le filtre est ajouté manuellement dans la chaîne de filtres de sécurité.
     */
    @Bean
    public FilterRegistrationBean<CasAuthenticationFilter> casAuthenticationFilterRegistration(CasAuthenticationFilter filter) {
      FilterRegistrationBean<CasAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
      registration.setEnabled(false);
      return registration;
    }

}
