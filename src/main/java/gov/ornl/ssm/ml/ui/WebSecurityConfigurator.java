package gov.ornl.ssm.ml.ui;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.requestCache().requestCache(new HttpSessionRequestCache())
			.and().authorizeRequests()
			.anyRequest().permitAll()
				.antMatchers("**/login").permitAll()
				
				.requestMatchers(request -> {
					final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
	            	if(request.getServletPath().equals("/favicon.ico")) return true;
	            	if(request.getServletPath().startsWith("/VAADIN/")) return true;
	            	if(request.getServletPath().startsWith("/vaadinServlet")) return true;
	            	return parameterValue != null
	        				&& Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));	

				}).permitAll()
				
				.anyRequest().authenticated()
				
		        .and().formLogin()
	            .loginPage("/login")
	            .loginProcessingUrl("/login")
	            //.successHandler(myAuthenticationSuccessHandler())
	            
	            
	            
	            // Add a custom authentication failure handler
	            .and().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {

					@Override
					public void commence(HttpServletRequest arg0, HttpServletResponse arg1,
							AuthenticationException arg2) throws IOException, ServletException {
						
						
						//Redirect the user to the main page
						String url = arg0.getServerName();
						
						String login = "/login";
						
						if(arg0.getServerPort() == 80) {
							url = "http://" + url + arg0.getContextPath() + login;
						} else if(arg0.getServerPort() == 443) {
							url = "https://" + url + arg0.getContextPath() + login;
						} else {
							url = "http://" + url + ":" + arg0.getServerPort() + arg0.getContextPath() + login;
						}
						
						arg1.sendRedirect(arg1.encodeRedirectURL(url));
						
					}
	            	
	            })
	            
	            // Logging out also redirects to main page
	            .and().logout().logoutSuccessUrl("/");

	}

	  @Override
	  public void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth
	      .ldapAuthentication()
	        .userDnPatterns("uid={0},ou=Users")
	        //.groupSearchBase("ou=groups")
	        .contextSource()
	          .url("ldaps://ldapu.ornl.gov/dc=ornl,dc=gov")
	        //  .and()
	        //.passwordCompare()
	          //.passwordEncoder(new BCryptPasswordEncoder())
	          //.passwordAttribute("password");
	          ;
	    
	  }
	
	   @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	   @Override
	   public AuthenticationManager authenticationManagerBean() throws Exception {
	       return super.authenticationManagerBean();
	   }
	  
//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
}