package gov.ornl.ssm.ml.ui.views;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;

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

		ArrayList<String> names = new ArrayList<String>();
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://" + config.getDatabaseHost() + ":5432/ssm",
			        "postgres", "postgres");
			
			ResultSet results = conn.createStatement().executeQuery("SELECT name FROM models");
			
			while(results.next()) {
				names.add(results.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Selector for the machine learning model
		Select<String> classifierSelect = new Select<String>();
		classifierSelect.setLabel("Filter");
		classifierSelect.setItems(names);
		
		Text descriptionText = new Text("");
		
		classifierSelect.addValueChangeListener(e -> {
			try {
				Connection conn = DriverManager.getConnection("jdbc:postgresql://" + config.getDatabaseHost() + ":5432/ssm",
				        "postgres", "postgres");
				ResultSet results = conn.createStatement().executeQuery("SELECT name FROM models WHERE name = " + classifierSelect.getValue());
				
				if(results.next()) {
				
				descriptionText.setText(results.getString("description"));
				} else {
					descriptionText.setText("");
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// Upload control for the file whose data is to be predicted.
		Upload fileField = new Upload();
		fileField.setDropLabel(new Label("Upload your sample's data file here"));

		// Button to perform the prediction
		Button runButton = new Button("Predict");

		add(classifierSelect);
		add(new Label("Filter Description:"));
		add(descriptionText);
		add(fileField);
		add(runButton);
	}
}
