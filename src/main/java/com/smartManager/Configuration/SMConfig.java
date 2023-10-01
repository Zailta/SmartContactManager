package com.smartManager.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
		httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN");

		}).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers(mvcMatcherBuilder.pattern("/user/**")).hasRole("USER");

		}).authorizeHttpRequests(authorize -> {
			authorize.requestMatchers(mvcMatcherBuilder.pattern("/**")).permitAll().anyRequest().authenticated();
		})

				.authenticationProvider(getDaoAuthenticationProvider()).formLogin(formlogin->{
					formlogin.loginPage("/signin");
				});

		return httpSecurity.build();
	}

}
