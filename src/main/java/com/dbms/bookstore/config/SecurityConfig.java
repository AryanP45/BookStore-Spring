package com.dbms.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.dbms.bookstore.services.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
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

		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable()).authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers(mvc.pattern("/"), mvc.pattern("/shop/**"), mvc.pattern("/register/**"),
						mvc.pattern("/images/**"), mvc.pattern("/productImages/**")).permitAll()

						.requestMatchers(mvc.pattern("/cart/**"), mvc.pattern("/checkout")).hasRole("USER")
						.requestMatchers(mvc.pattern("/admin")).hasRole("ADMIN").anyRequest().authenticated().and()

						.formLogin(login -> login.loginPage("/login").permitAll().failureUrl("/login?error=true")
								.usernameParameter("email").passwordParameter("password"))

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

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}

}