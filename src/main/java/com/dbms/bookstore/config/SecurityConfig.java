
package com.dbms.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher.Builder;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.dbms.bookstore.services.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers(mvc.pattern("/")).permitAll().requestMatchers(mvc.pattern("/shop/**")).permitAll()
						.requestMatchers(mvc.pattern("/register/**")).permitAll()
						.requestMatchers(mvc.pattern("/images/**")).permitAll()
						.requestMatchers(mvc.pattern("/productImages/**")).permitAll()
//						Just for Testing
//						.requestMatchers(mvc.pattern("/cart/**")).permitAll()
//						.requestMatchers(mvc.pattern("/checkout")).permitAll()
						.requestMatchers(mvc.pattern("/admin")).hasRole("ADMIN").anyRequest().authenticated().and()
						.formLogin(login -> login.loginPage("/login").permitAll().failureUrl("/login?error=true")
								.defaultSuccessUrl("/shop").usernameParameter("email").passwordParameter("password"))
						.logout(logout -> logout.logoutRequestMatcher(mvc.pattern("/logout")).logoutSuccessUrl("/login")
								.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
						.exceptionHandling(withDefaults());
			} catch (Exception e) {
				System.out.println("error in auth");
				e.printStackTrace();
					}
				}).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}


    @Bean
    AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}
	
 
}