package gov.ornl.ssm.ml.ui.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

/**
 * The base view, with links to each of the app's capabilities.
 * 
 * @author Robert Smith
 *
 */
@Route("")
public class MainScreen extends VerticalLayout {

	/**
	 * The default constructor
	 */
	public MainScreen() {

		//Links to each capability
		RouterLink trainingLink = new RouterLink("Train a New Filter", TrainingView.class);
		RouterLink classifyLink = new RouterLink("Classify a Sample", ClassificationView.class);
		Anchor logout = new Anchor("logout", "Logout");
		
		//Layout
		add(trainingLink);
		add(classifyLink);
		add(logout);

	}
	
	public String runPython() {
		
		String line = "";
		
		// Get the currently existing docker container names
		
		try {
			Process containerNameProcess = containerNameProcess = Runtime.getRuntime()
					.exec("docker ps --format \"{{.Names}}\"");

		try {
			containerNameProcess.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read all the names, finding the largest id
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(containerNameProcess.getInputStream()));

		// The current docker container
		line = reader.readLine();

		// Iterate through all docker containers, finding the next unused name for
		// qclimax
		while (line != null) {

			// Get the next line
			line += reader.readLine();
		}

		reader.close();
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return line;
	}
}
