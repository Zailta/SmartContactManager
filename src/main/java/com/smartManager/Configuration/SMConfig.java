package com.smartManager.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SMConfig {
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new SMUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider getDaoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.getUserDetailsService());
		authenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class).build();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
		httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN");

		}).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers(mvcMatcherBuilder.pattern("/user/**")).hasRole("USER");

		}).authorizeHttpRequests(authorize -> {
			authorize.requestMatchers(mvcMatcherBuilder.pattern("/token")).permitAll().anyRequest().authenticated();
		}).sessionManagement(sessionManagement ->{
			sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		})
		.authenticationProvider(getDaoAuthenticationProvider()).formLogin(formlogin->{
					formlogin.loginPage("/signin").loginProcessingUrl("/signin").defaultSuccessUrl("/user/view-contacts/0");
				});

		return httpSecurity.build();
	}

}
