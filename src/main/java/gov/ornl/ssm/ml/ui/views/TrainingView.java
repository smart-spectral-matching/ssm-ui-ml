package gov.ornl.ssm.ml.ui.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import gov.ornl.ssm.ml.ui.UIConfiguration;
import gov.ornl.ssm.ml.ui.components.LabelFeaturesDialog;
import gov.ornl.ssm.ml.ui.data.Axis;
import gov.ornl.ssm.ml.ui.data.Dataseries;
import gov.ornl.ssm.ml.ui.data.Facet;
import gov.ornl.ssm.ml.ui.data.Filter;
import gov.ornl.ssm.ml.ui.data.Model;
import gov.ornl.ssm.ml.ui.data.Parameter;
import gov.ornl.ssm.ml.ui.data.Scidata;
import gov.ornl.ssm.ml.ui.data.System;
import gov.ornl.ssm.ml.ui.data.ValueArray;

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

		try {
		ObjectMapper mapper = new ObjectMapper();

		// The filter to be defined
		Filter filter = new Filter();

		// Json representation of the models
		String jsonModels = "";

		// List of all models from the CURIES database
		models = new ArrayList<Model>();

		try {

			// Get the model digest from the backend
			URL url = new URL(config.getFusekiHost()
					+ "/api/datasets/curies/models");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			//Read the results into the string
			String line = reader.readLine();

			while (line != null) {
				jsonModels += line;
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			
			//Strip out the pagination data, leaving only the raw models
			String modelsString = mapper.writeValueAsString(mapper.readTree(jsonModels).get("data"));
			
			//Convert the model data back to JSON, then read the JSON into classes
			models = mapper.readValue(modelsString,
					mapper.getTypeFactory().constructCollectionType(List.class, Model.class));
			
			//FIXME Remove this section when abbreviated formatting is done.
			for(Model model : models) {
				
				model.setScidata(new Scidata());
				model.getScidata().setSystem(new System());
				
				for(int j = 0; j < 3; j++) {
					int i = ThreadLocalRandom.current().nextInt(0, 5);
					Facet facet = new Facet();
					if(i == 0) {
						facet.setAtoms("Pb");
					} else if (i == 1) {
						facet.setAtoms("UO2");
					} else if (i == 2) {
						facet.setAtoms("U");
					} else if (i == 3) {
						facet.setAtoms("H20");
					} else {
						facet.setAtoms("SiO");
					}
					model.getScidata().getSystem().getFacets().add(facet);
				}
				
				model.getScidata().setDataseries(new Dataseries());
				model.getScidata().getDataseries().setxAxis(new Axis());
				model.getScidata().getDataseries().getxAxis().setParameter(new Parameter());
				model.getScidata().getDataseries().getxAxis().getParameter().setValuearray(new ValueArray());
				model.getScidata().getDataseries().getxAxis().getParameter().getValuearray().getNumberarray().add(0d);
				model.getScidata().getDataseries().getxAxis().getParameter().getValuearray().getNumberarray().add(1d);
				model.getScidata().getDataseries().getxAxis().getParameter().getValuearray().getNumberarray().add(2d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//A grid to display all models
		Grid<Model> grid = new Grid<Model>(Model.class);
		grid.setItems(models);
		grid.setColumns("title", "created", "modified");

		//Button to open the label and feature dialog
		Button labelFeaturesButton = new Button("Set Label and Features");

		labelFeaturesButton.addClickListener(e -> {
			new LabelFeaturesDialog(models, filter).open();
		});

		//Selection box for the type of machine learning 
		Select<String> typeSelect = new Select<String>();
		typeSelect.setLabel("Classifier/Regressor Type");
		typeSelect.setItems(classifierNamesToTypes.keySet());

		//Field to define the model's name
		TextField nameField = new TextField();
		nameField.setLabel("Save as ");

		//Field to define the model's description
		TextField descriptionField = new TextField();
		descriptionField.setLabel("Description");

		//Button to begin the training
		Button runButton = new Button("Train New Filter");

		//Label to show the output
		Label outputLabel = new Label();

		runButton.addClickListener(e -> {

			//Create the comma separated list of model uuids
			String modelList = "";

			for (Model m : models) {
				modelList += m.getUuid() + ",";
			}

			modelList = modelList.substring(0, modelList.length() - 1);

			//Get the JSON representation of the filter
			String filterString = "{\"Filters\":[{\"Features\":[[\"path1\",\"path2\",\"path3\"],[\"path1\",\"path2\",\"path3\"]],\"Label\":[\"path1\",\"path2\",\"path3\"]}]}";
			try {
				filterString = "{\"Filters\":[" + mapper.writeValueAsString(filter) + "]}";
			} catch (JsonProcessingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} 
			
			//Get the classifier type
			String classifier = classifierNamesToTypes.get(typeSelect.getValue());

			//Get the model name
			String name = nameField.getValue().replace(' ', '_');

			//String to store the Python output
			String output = "";

			// Get the currently existing docker container names

			try {
				
				//Launch the Python script
				Process containerNameProcess = Runtime.getRuntime().exec("python3 ssm.py train "
						+ modelList + " " + filterString + " " + classifier + " " + nameField.getValue());

				try {
					containerNameProcess.waitFor();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}

				// Read the output
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(containerNameProcess.getInputStream()));

				// The current line of output
				String line = reader.readLine();

				while (line != null) {

					// Get the next line
					output += line;
					line = reader.readLine();
				}

				reader.close();

				//Now read standard error as well
				reader = new BufferedReader(new InputStreamReader(containerNameProcess.getErrorStream()));

				line = reader.readLine();
				
				while (line != null) {

					// Get the next line
					output += line;
					line = reader.readLine();
				}

				reader.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}

			//Display the output text
			outputLabel.setText(output);
		});

		//Layout
		add(grid);
		add(typeSelect);
		add(nameField);
		add(descriptionField);
		add(labelFeaturesButton);
		add(runButton);
		add(outputLabel);
		
		}
		catch (NullPointerException e) {
			add(new Label(e.getMessage()));
			
			
			for(int i = 0; i < e.getStackTrace().length; i++) {
			add(new Label(e.getStackTrace()[i].toString()));
			}
		}
	}

}
