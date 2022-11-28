package gov.ornl.ssm.ml.ui;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Security configuration for OIDC sign in.
 * 
 * @author Robert Smith
 *
 */
@Configuration
public class OIDCLoginSecurityConfig {
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    
		OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
		handler.setPostLogoutRedirectUri("http://localhost:8080/machine-learning/");
		
		http.authorizeRequests(authorizeRequests -> authorizeRequests
	    	.anyRequest().authenticated())
	    	.oauth2Login(e -> e.permitAll())
	    	.logout(e -> e.logoutSuccessHandler(handler));
	    return http.build();
	}

}
