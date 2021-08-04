package gov.ornl.ssm.ml.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * View for loging into the application.
 * 
 * @author Robert Smith
 *
 */
@Route("login")
public class LoginScreen extends VerticalLayout {

	@Autowired
	public LoginScreen(AuthenticationManager authenticationManager) {

		LoginOverlay login = new LoginOverlay();
		login.setAction("login");
		login.setOpened(true);
		login.setTitle("Smart Spectra Matching");

		login.addLoginListener(e -> {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));
		});

	}
}
