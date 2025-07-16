package gov.ornl.ssm.ml.ui.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import gov.ornl.ssm.ml.ui.UIConfiguration;

/**
 * View to perform matching of a file against existing spectra in the database
 * 
 * @author Robert Smith
 *
 */
@Route("match")
public class MatchingView extends VerticalLayout {
	
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
	public MatchingView() {
		// Nothing to do until the config is autowired during PostConstruct
	}
	
	/**
	 * Initialize the view
	 */
	@PostConstruct
	public void init() {
	
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
		Button runButton = new Button("Get Matching Spectra");
		
		// Label to show the output
		VerticalLayout outputLayout = new VerticalLayout();
		Pre outputLabel = new Pre();
		
		runButton.addClickListener(e -> {

			// String to store the Python output
			String output = "";
			
			outputLayout.removeAll();

			try {
				// Launch the Python script
				ProcessBuilder builder = new ProcessBuilder("python3", "ssm.py", "match", userJson, config.getFusekiHost());
				Process pythonProcess = builder.start();
				
				while(pythonProcess.isAlive()) {
					
				}
				
				// Read the output
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(pythonProcess.getInputStream()));

				// The current line of output
				String line = reader.readLine();
				String prevLine = "";

				while (line != null) {
					
					// If this line is the link, we have enough data to display the named link
					if (line.startsWith("http")) {
						outputLayout.add(new Anchor(line, prevLine));
						// FIXME Need to figure out the link to the one in the GUI
						outputLayout.add(new Anchor(prevLine, "View on Web"));
					} else if (!prevLine.startsWith("http")) {
						
						// If the previous line wasn't a link and this line wasn't a link, then the previous line was
						// a header, so add it.
						outputLayout.add(new Label(prevLine));
					}

					// Get the next line
					output += line + "\n";
					prevLine = line;
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
		
		// Layout
		add(fileField);
		add(runButton);
		add(outputLayout);
	}
}
