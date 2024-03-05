package gov.ornl.ssm.ml.ui.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import gov.ornl.ssm.ml.ui.UIConfiguration;
import gov.ornl.ssm.ml.ui.components.LabelFeaturesDialog;
import gov.ornl.ssm.ml.ui.data.Filter;
import gov.ornl.ssm.ml.ui.data.Model;

/**
 * View to define and train new filters.
 * 
 * @author Robert Smith
 *
 */
@Route("training")
public class TrainingView extends VerticalLayout {

	/**
	 * The configuration from the .properties file
	 */
	@Autowired
	private UIConfiguration config;

	/**
	 * Map of human readable classifier names to SSM expected classifier types
	 */
	private HashMap<String, String> classifierNamesToTypes;

	/**
	 * List of all models from CURIES.
	 */
	private List<Model> models;
	
    /**
     * Oauth authentication service.
    */
    @Autowired(required = false)
    private OAuth2AuthorizedClientService authorizedClientService;

	/**
	 * The default constructor
	 */
	public TrainingView() {

		// Nothing to do but initialize data members until config is autowired during
		// PostConstruct
		classifierNamesToTypes = new HashMap<String, String>();
		classifierNamesToTypes.put("Nearest Neighbors", "KNeighborsClassifier");
		classifierNamesToTypes.put("Support Vector Machine", "SVC");
		classifierNamesToTypes.put("Gaussian Process", "GaussianProcessClassifier");
		classifierNamesToTypes.put("Decision Tree", "DecisionTreeClassifier");
		classifierNamesToTypes.put("Random Forest", "RandomForestClassifier");
		classifierNamesToTypes.put("Multi-Layer Perceptron", "MLPClassifier");
		classifierNamesToTypes.put("AdaBoost", "AdaBoostClassifier");
		classifierNamesToTypes.put("Gaussian Naive Bayes", "GaussianNB");
		classifierNamesToTypes.put("Quadratic Discriminant Analysis", "QuadraticDiscriminantAnalysis");
	}

	/**
	 * Create all controls
	 */
	@PostConstruct
	public void init() {

		ObjectMapper mapper = new ObjectMapper();

		// The filter to be defined
		Filter filter = new Filter();

		// Json representation of the models
		String jsonModels = "";
		
        // Get the current user
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
		
        // The user information
        OidcUser user = (OidcUser) authentication.getPrincipal();
        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient("keycloak", user.getName());

		// List of all models from the CURIES database
		models = new ArrayList<Model>();
		try {
			try {

				// Get the model digest from the backend
				URL url = new URL(config.getFusekiHost() + "/api/collections/" + config.getCollectionName() + "/models?pageSize=220");
				
				// How many consumed/total models are in the database
				int seen = 0;
				int total = 1;
	            
				// Continue fetching until all models have been returned
				while(seen < total) {
					
					// If the token is expired, force a spring-boot token refresh by refreshing the entire page.
		            if(client.getAccessToken().getExpiresAt().isBefore(Instant.now())) {
		            	SecurityContextHolder.getContext().setAuthentication(null);
		            	UI.getCurrent().navigate("training");
		            }
					
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestProperty("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
					conn.setRequestMethod("GET");
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					
					jsonModels = "";
					
					// Read the results into the string
					String line = reader.readLine();
	
					while (line != null) {
						jsonModels += line;
						line = reader.readLine();
					}
					
	
					// Get the total number of models to download
					JsonNode digest = mapper.readTree(jsonModels);
					total = digest.get("total").asInt();
					
					// Get the first result page's url
					String next = digest.get("next").asText();
					url = new URL(next);
					
					// Strip out the pagination data, leaving only the raw models
					String modelsString = mapper.writeValueAsString(digest.get("data"));
	
					// Convert the model data back to JSON, then read the JSON into classes
					List<Model> tempModels = mapper.readValue(modelsString,
							mapper.getTypeFactory().constructCollectionType(List.class, Model.class));
					
					//Read each abbreviated model
					for(Model temp : tempModels) {
						
						//Count the new model
						seen += 1;
						
						String modelString = "";
						
						//Change the urls to point to the fuseki host
						String urlString = temp.getUrl();
						if(urlString.contains(".gov")) {
							urlString = config.getFusekiHost() + urlString.substring(urlString.indexOf(".gov") + 4);
						}
						if(urlString.endsWith("/")) {
							urlString = urlString.substring(0, urlString.lastIndexOf("/"));
						}
						
						// Get the model digest from the backend
						URL modelUrl = new URL(urlString);
						HttpURLConnection modelConn = (HttpURLConnection) modelUrl.openConnection();
						modelConn.setRequestMethod("GET");
						modelConn.setRequestProperty("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
						reader = new BufferedReader(new InputStreamReader(modelConn.getInputStream()));
	
						// Read the results into the string
						line = reader.readLine();
	
						while (line != null) {
							modelString += line;
							line = reader.readLine();
						}
						
						models.add(mapper.readValue(modelString, Model.class));
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
				add(new Label(e.getMessage()));

				for (int i = 0; i < e.getStackTrace().length; i++) {
					add(new Label(e.getStackTrace()[i].toString()));
				}

			}
			
			// A grid to display all models
			Grid<Model> grid = new Grid<Model>(Model.class);
			grid.setItems(models);
			grid.setColumns("title", "created", "modified");

			// Button to open the label and feature dialog
			Button labelFeaturesButton = new Button("Set Label and Features");

			labelFeaturesButton.addClickListener(e -> {
				new LabelFeaturesDialog(models, filter, "Default name", "Default description", config).open();
			});

			// Selection box for the type of machine learning
			Select<String> typeSelect = new Select<String>();
			typeSelect.setLabel("Classifier/Regressor Type");
			typeSelect.setItems(classifierNamesToTypes.keySet());

			// Field to define the model's name
			TextField nameField = new TextField();
			nameField.setLabel("Save as ");

			// Field to define the model's description
			TextField descriptionField = new TextField();
			descriptionField.setLabel("Description");

			// Button to begin the training
			Button runButton = new Button("Train New Filter");

			// Label to show the output
			Label outputLabel = new Label();

			runButton.addClickListener(e -> {

				// Create the comma separated list of model uuids
				String modelList = "";

				for (Model m : models) {
					modelList += m.getUuid() + ",";
				}

				modelList = modelList.substring(0, modelList.length() - 1);

				// Get the JSON representation of the filter
				String filterString = "";
				try {
					filterString = "{\"Filters\":[" + mapper.writeValueAsString(filter) + "]}";
				} catch (JsonProcessingException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				// Get the classifier type
				String classifier = classifierNamesToTypes.get(typeSelect.getValue());

				// Get the model name
				String name = nameField.getValue().replace(' ', '_');

				// String to store the Python output
				String output = "";

				try {
					


					// Launch the Python script
					Process pythonProcess = Runtime.getRuntime().exec(new String[] {"python3", "ssm.py", "train", modelList, filterString, classifier, nameField.getValue(),
							config.getFusekiHost(), descriptionField.getValue(), client.getAccessToken().getTokenValue()});
					pythonProcess.waitFor();
					
					// Read the output
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(pythonProcess.getInputStream()));

					// The current line of output
					String line = reader.readLine();

					while (line != null) {

						// Get the next line
						output += line;
						line = reader.readLine();
					}

					reader.close();

					// Now read standard error as well
					reader = new BufferedReader(new InputStreamReader(pythonProcess.getErrorStream()));

					line = reader.readLine();

					while (line != null) {

						// Get the next line
						output += line;
						line = reader.readLine();
					}

					reader.close();

				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
					add(new Label(e1.getMessage()));

					for (int i = 0; i < e1.getStackTrace().length; i++) {
						add(new Label(e1.getStackTrace()[i].toString()));
					}
				}

				// Display the output text
				outputLabel.setText(output);
			});

			// Layout
			add(grid);
			add(typeSelect);
			add(nameField);
			add(descriptionField);
			add(labelFeaturesButton);
			add(runButton);
			add(outputLabel);

		} catch (NullPointerException e) {
			e.printStackTrace();
			add(new Label(e.getMessage()));

			for (int i = 0; i < e.getStackTrace().length; i++) {
				add(new Label(e.getStackTrace()[i].toString()));
			}
		}
	}
}
