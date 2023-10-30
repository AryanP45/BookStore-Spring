
package com.dbms.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher.Builder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.dbms.bookstore.model.CustomUserDetail;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration

public class SecurityConfig {
	
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector){
		return new MvcRequestMatcher.Builder(introspector);
	}

	@Bean
	 public SecurityFilterChain filterChain(HttpSecurity http,MvcRequestMatcher.Builder mvc) throws Exception {
		
		http
            .authorizeHttpRequests((authz) -> {
				try {
					authz
							.requestMatchers(mvc.pattern("/")).permitAll()
							.requestMatchers(mvc.pattern( "/shop/**")).permitAll()
							.requestMatchers(mvc.pattern("/register/**")).permitAll()
							.requestMatchers(mvc.pattern("/images/**")).permitAll()
							.requestMatchers(mvc.pattern("/productImages/**")).permitAll()
							.requestMatchers(mvc.pattern("/admin")).hasRole("ADMIN")
					    .anyRequest().authenticated()
					    .and()
					    .formLogin()
					    .loginPage("/login")
					    .permitAll()
					    .failureUrl("/login?error=true")
					    .defaultSuccessUrl("/")
					    .usernameParameter("email")
					    .passwordParameter("password")
					    .and()
					    .logout()
					    .logoutRequestMatcher(mvc.pattern("/logout"))
					    .logoutSuccessUrl("/login")
					    .invalidateHttpSession(true)
					    .deleteCookies("JSESSIONID")
					    .and()
					    .exceptionHandling()
					    .and()
					    .csrf().disable()
					    ;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("error in auth");
					e.printStackTrace();
				}
			}
                
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            ;
		return http.build();
    }
//	  @Bean
//	    public WebSecurityCustomizer webSecurityCustomizer() {
//	        return (web) -> web.ignoring().requestMatchers("/shop/**","/register");
//	    }
	
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder(); 
//	}
	
//	public void configure (AuthenticationManagerBuilder auth) throws Exception{
//		auth.userDetailsService(CustomUserDetailService);
//	}
	 
	  
}