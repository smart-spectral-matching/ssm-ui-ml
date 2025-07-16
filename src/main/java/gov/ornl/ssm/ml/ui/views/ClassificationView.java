package gov.ornl.ssm.ml.ui.views;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.codec.binary.Hex;

import gov.ornl.ssm.ml.ui.UIConfiguration;

/**
 * View to perform predictions on new datapoints with pre-trained models.
 * 
 * @author Robert Smith
 *
 */
@Route("classify")
public class ClassificationView extends VerticalLayout {

	/**
	 * The configuration from the .properties file
	 */
	@Autowired
	private UIConfiguration config;
	
	/**
	 * The json formatted data from the user uploaded file
	 */
	private String userJson;

	/**
	 * Default constructor
	 */
	public ClassificationView() {
		// Nothing to do until the config is autowired during PostConstruct
	}

	/**
	 * Initialize the view
	 */
	@PostConstruct
	public void init() {

		userJson = null;
		
		//Download all model names from the database
		ArrayList<String> names = new ArrayList<String>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://" + config.getDatabaseHost() + ":" + config.getDatabasePort() + "/" + config.getDatabaseName(),
			        config.getDatabaseUser(), config.getDatabasePassword());
			
			ResultSet results = conn.createStatement().executeQuery("SELECT name FROM models");
			
			while(results.next()) {
				names.add(results.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			add(new Label(e.getMessage()));

			for (int i = 0; i < e.getStackTrace().length; i++) {
				add(new Label(e.getStackTrace()[i].toString()));
			}
		}
		
		// Selector for the machine learning model
		Select<String> classifierSelect = new Select<String>();
		classifierSelect.setLabel("Filter");
		classifierSelect.setItems(names);
		
		// Label to hold the user description of the filter
		Label descriptionText = new Label("");
		descriptionText.getElement().getStyle().set("white-space", "pre-wrap");
		
		// Layout for the graphs
		HorizontalLayout graphs = new HorizontalLayout();
		
		classifierSelect.addValueChangeListener(e -> {
			try {
				Connection conn = DriverManager.getConnection("jdbc:postgresql://" + config.getDatabaseHost() + ":5432/ssm",
				        "postgres", "postgres");
				ResultSet results = conn.createStatement().executeQuery("SELECT description, f1, recall, selectivity, false_discovery, false_omission, true_positive, true_negative, false_positive, false_negative FROM models WHERE name = '" + classifierSelect.getValue() + "'");
				
				if(results.next()) {
				
					descriptionText.setText(results.getString("description") + "\n\nF1 Score: " + results.getString("f1") + "\nRecall: " + results.getString("recall") + " False Discovery Rate: " + results.getString("false_discovery") + "\nSelectivity: " + results.getString("selectivity") + " False Omission Rate: " + results.getString("false_omission") + "\n\nTrue Positives: " + results.getString("true_positive") + " False Positives: " + results.getString("false_positive") + "\nTrue Negatives: " + results.getString("true_negative") + " False Negatives: " + results.getString("false_negative") );
				} else {
					descriptionText.setText("");
				}
				
				graphs.removeAll();
				
				//Get all graphs for the given model
				ResultSet imageResults = conn.createStatement().executeQuery("SELECT image FROM graphs WHERE model = '" + classifierSelect.getValue() + "'");
				
				// Add each graph to the view
				while(imageResults.next()) {
					
					byte[] imageBytes = imageResults.getBytes("image");
					
					StreamResource streamResource = new StreamResource("graph.png", () -> {
							return new ByteArrayInputStream(imageBytes);
							
					});
					
					

			        Image image = new Image(streamResource, "Graph");
			        graphs.add(image);
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// Upload control for the file whose data is to be predicted.
		MemoryBuffer buffer = new MemoryBuffer();
		Upload fileField = new Upload(buffer);
		fileField.setDropLabel(new Label("Upload your sample's data file here"));
		
		fileField.addFinishedListener(e -> {
			
			//Send the uploaded file to the parser service
			HttpEntity entity = MultipartEntityBuilder.create().addPart("upload_file", new InputStreamBody(buffer.getInputStream(), e.getFileName())).build();
			
			HttpPost request = new HttpPost(config.getFileConverterHost() + "/convert/json");
		    request.setEntity(entity);

		    HttpClient client = HttpClientBuilder.create().build();
		    try {
		    	
		    	//Save the parser service's response
				HttpResponse response = client.execute(request);
				userJson = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		});

		// Button to perform the prediction
		Button runButton = new Button("Predict");
		
		// Label to hold the prediction output
		Label outputLabel = new Label();
		
		runButton.addClickListener(e -> {
			// String to store the Python output
			String output = "";

			try {
				// Launch the Python script
				ProcessBuilder builder = new ProcessBuilder("python3", "ssm.py", "predict", classifierSelect.getValue(), userJson);
				Process pythonProcess = builder.start();
				
				while(pythonProcess.isAlive()) {
					
				}
				
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
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Display the output text
			outputLabel.setText(output);
		});
		
		

		add(classifierSelect);
		add(new Label("Filter Description:"));
		add(descriptionText);
		add(graphs);
		add(fileField);
		add(runButton);
		add(outputLabel);
	}
}
