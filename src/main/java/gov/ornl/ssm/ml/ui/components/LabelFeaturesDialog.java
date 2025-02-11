package gov.ornl.ssm.ml.ui.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;

import gov.ornl.ssm.ml.ui.UIConfiguration;
import gov.ornl.ssm.ml.ui.data.CoordinationChemistry;
import gov.ornl.ssm.ml.ui.data.CrystalSystem;
import gov.ornl.ssm.ml.ui.data.Feature;
import gov.ornl.ssm.ml.ui.data.Filter;
import gov.ornl.ssm.ml.ui.data.FunctionalGroup;
import gov.ornl.ssm.ml.ui.data.Model;
import gov.ornl.ssm.ml.ui.data.PeakFixedDistanceFeature;
import gov.ornl.ssm.ml.ui.data.PeakLocationRangeFeature;
import gov.ornl.ssm.ml.ui.data.PeakRatioRangeFeature;
import gov.ornl.ssm.ml.ui.data.PeaksRatioTwoRangeFeature;
import gov.ornl.ssm.ml.ui.data.StructureType;
import gov.ornl.ssm.ml.ui.data.System;

/**
 * Dialog for defining the Label and Features for a Filter.
 * 
 * @author Robert Smith
 *
 */
public class LabelFeaturesDialog extends Dialog {

	/**
	 * List of all unique crystals named in any Face in any Model
	 */
	private HashSet<String> crystals = null;

	/**
	 * List of all uranium chemistry coordinations named in any System in any Model
	 */
	private HashSet<String> coordinations = null;

	/**
	 * Layout where feature editors will be drawn.
	 */
	private VerticalLayout featureLayout;

	/**
	 * List of existing Feature objects.
	 */
	private List<Feature> features;

	/**
	 * The filter being edited.
	 */
	private Filter filter;

	/**
	 * The grid showing the models being used as training data.
	 */
	private Grid<Model> grid;

	/**
	 * The models representing the data the filter will be trained on.
	 */
	private List<Model> models;

	/**
	 * List of all unique molecules named in any System in any Model
	 */
	private HashSet<String> molecules = null;

	/**
	 * Layout where the label editor will be drawn.
	 */
	private VerticalLayout labelLayout;

	/**
	 * List of all unique structures named in any System in any Model
	 */
	private HashSet<String> structures = null;

	/**
	 * Default constructor.
	 * 
	 * @param models
	 *            The models that the filter will be trained against.
	 * @param filter
	 *            The filter definition to be edited
	 */
	public LabelFeaturesDialog(List<Model> models, Filter filter, String name, String description, UIConfiguration config) {

		// Initialize data members
		features = new ArrayList<Feature>();
		this.filter = filter;
		this.models = models;
		labelLayout = new VerticalLayout();
		featureLayout = new VerticalLayout();

		// A grid to display model labels
		grid = new Grid<Model>(Model.class);
		grid.setItems(models);
		grid.setColumns("title", "label");

		// TODO Maybe display this condition as a color for the row instead of a column
		// Add a column for whether this model needs to be thrown out for not having a
		// feature
		grid.addColumn(new ValueProvider<Model, Boolean>() {

			@Override
			public Boolean apply(Model source) {
				return source.getInvalidFeatures().size() == 0;
			}

		}).setHeader("valid");

		// Selector for which type of label to use
		Select<String> labelSelect = new Select<String>();
		labelSelect.setItems("Crystal System Type", "Functional Group Prescence",
				"Structure Type", "Uranium Coordination Chemistry");
		labelSelect.setLabel("Select Label Type");

		// Draw the editor for the kind of label the user selects
		labelSelect.addValueChangeListener(e -> {

			if ("Crystal System Type".equals(e.getValue())) {
				drawCrystalSystemEditor();
			} else if ("Functional Group Prescence".equals(e.getValue())) {
				drawFunctinoalGroupPrescenceEditor();
			} else if ("Structure Type".equals(e.getValue())) {
				drawStructureTypeEditor();
			} else if ("Uranium Coordination Chemistry".equals(e.getValue())) {
				drawUraniumCoordinationChemistryEditor();
			}
		});

		// List of all existing features
		ListBox<Feature> featureBox = new ListBox<Feature>();

		featureBox.setRenderer(new ComponentRenderer<Label, Feature>(item -> {
			String display = "Feature " + features.indexOf(item) + " ";
			if (item instanceof PeakLocationRangeFeature) {
				display += PeakLocationRangeFeature.getName();
			} else if (item instanceof PeakRatioRangeFeature) {
				display += PeakRatioRangeFeature.getName();
			}
			return new Label(display);
		}));

		// Selector for what kind of feature to add
		Select<String> featureSelect = new Select<String>();
		featureSelect.setItems(PeakLocationRangeFeature.getName(),
				PeakRatioRangeFeature.getName(), PeakFixedDistanceFeature.getName(),
				PeaksRatioTwoRangeFeature.getName());
		featureSelect.setLabel("Select New Feature Type");

		// Button to add a new feature
		Button addFeatureButton = new Button("Add Feature");

		// When a new feature is added, add a Feature object of the appropriate type and
		// edit the Filter definition
		addFeatureButton.addClickListener(e -> {

			if (PeakLocationRangeFeature.getName().equals(featureSelect.getValue())) {
				features.add(new PeakLocationRangeFeature(filter, models, this));
				featureBox.setItems(features);
				filter.getFeatures().add(Arrays.asList("scidata", "dataseries", "SSM:XY:axis:PEAK-LOC-RANGE-0-1",
						"parameter", "numericValueArray", 0, "numberArray"));
			} else if (PeakRatioRangeFeature.getName().equals(featureSelect.getValue())) {
				features.add(new PeakRatioRangeFeature(filter, models, this));
				featureBox.setItems(features);
				filter.getFeatures().add(Arrays.asList("scidata", "dataseries", "SSM:XY:axis:PEAK-RATIO-RANGE-0-1",
						"parameter", "numericValueArray", 0, "numberArray"));
			} else if (PeaksRatioTwoRangeFeature.getName().equals(featureSelect.getValue())) {
				features.add(new PeaksRatioTwoRangeFeature(filter, models, this));
				featureBox.setItems(features);
				filter.getFeatures()
						.add(Arrays.asList("scidata", "dataseries", "SSM:XY:axis:PEAKS-RATIO-TWO-RANGES-0-1-2-3",
								"parameter", "numericValueArray", 0, "numberArray"));
			} else if (PeakFixedDistanceFeature.getName().equals(featureSelect.getValue())) {
				features.add(new PeakFixedDistanceFeature(filter, models, this));
				featureBox.setItems(features);
				filter.getFeatures().add(Arrays.asList("scidata", "dataseries",
						"SSM:XY:axis:PEAKS-DISTANCE-0-10-2-1-2-2", "parameter", "numericValueArray", 0, "numberArray"));
			}

		});

		// Button to remove a feature
		Button removeFeatureButton = new Button("Remove Feature");

		// Disable the button if there are no features to remove
		if (features.isEmpty()) {
			removeFeatureButton.setEnabled(false);
		}

		featureBox.addValueChangeListener(e -> {
			if (featureBox.getValue() != null) {

				// If a feature is selected, draw its editor
				featureLayout.removeAll();
				featureBox.getValue().draw(featureLayout, features.indexOf(featureBox.getValue()));
				removeFeatureButton.setEnabled(true);
			} else {

				// If the feature is deselected, remove its editor and disable the remove
				// button.
				featureLayout.removeAll();
				removeFeatureButton.setEnabled(false);
			}
		});

		// If a feature is selected, remove it and the corresponding line from the
		// filter
		removeFeatureButton.addClickListener(e -> {
			if (featureBox.getValue() != null) {
				filter.getFeatures().remove(features.indexOf(featureBox.getValue()));
				features.remove(featureBox.getValue());
				featureLayout.removeAll();
				featureBox.setItems(features);
			}
		});
		
		Label outputLabel = new Label();
		
		Button autoButton = new Button("Auto Train");
		autoButton.addClickListener(e -> {
			
			// String to store the Python output
			String output = "";

			try {
				
				// Create the comma separated list of model uuids
				String modelList = "";

				for (Model m : models) {
					modelList += m.getUuid() + ",";
				}

				modelList = modelList.substring(0, modelList.length() - 1);


				// Launch the Python script
				Process pythonProcess = Runtime.getRuntime().exec(new String[] {"python3", "ssm.py", "auto", modelList, name,
						config.getFusekiHost(), description});
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
		add(labelSelect);
		add(labelLayout);
		add(featureBox);
		add(featureSelect);
		add(addFeatureButton);
		add(removeFeatureButton);
		add(featureLayout);
		add(autoButton);
		add(outputLabel);

		// If a label already exists, draw its editor
		if (filter.getLabel().size() > 0) {
			for (String label : filter.getLabel()) {

				// "PRESENT" means seearching for a molecule
				if (label.contains(":PRESENT")) {
					drawFunctinoalGroupPrescenceEditor();
					break;
				}
			}
		}

		// If the filter already has any filters, add them to the UI
		if (filter.getFeatures().size() > 0) {
			for (List<Object> featureList : filter.getFeatures()) {
				for (Object featureObj : featureList) {

					if (featureObj instanceof String) {

						String feature = (String) featureObj;

						// Add the correct type of feature for the SSM command in the filter definition.
						if (feature.contains(":PEAK-LOC-RANGE-")) {
							features.add(new PeakLocationRangeFeature(filter, models, this));
							break;
						} else if (feature.contains(":PEAK-RATIO-RANGE-")) {
							features.add(new PeakRatioRangeFeature(filter, models, this));
							break;
						} else if (feature.contains(":PEAKS-RATIO-TWO-RANGES")) {
							features.add(new PeaksRatioTwoRangeFeature(filter, models, this));
							break;
						}

					}
				}
			}
		}

		// Update the feature box with the pre-existing features
		featureBox.setItems(features);
	}

	/**
	 * Draw an editor for a Crystal System type filter
	 */
	private void drawCrystalSystemEditor() {

		// If the lists hvensn't been created, search for all unique molecules
		if (crystals == null) {
			crystals = new HashSet<String>();

			for (Model model : models) {
				CrystalSystem crystalsystem = model.getScidata().getSystem().getCrystalSystem();
				if(crystalsystem != null) {
					crystals.add(crystalsystem.getCrystalSystem());
				}
			}
		}

		// A selector for which molecule to build the label on
		Select<String> moleculeSelect = new Select<String>();
		moleculeSelect.setLabel("Crystal System type to detect");
		moleculeSelect.setItems(crystals);

		// If the filter is already initialized, show t
		if (filter.getLabel().size() > 0) {
			for (String label : filter.getLabel()) {
				if (label.contains("SSM:PRESENT:crystal system:")) {
					moleculeSelect.setValue(label.substring(27));
				}
			}
		}

		moleculeSelect.addValueChangeListener(e -> {

			// Set the molecule to the filter definition
			filter.setLabel(Arrays.asList("scidata", "system", "facets", "SSM:PRESENT:crystal system:" + e.getValue()));

			// Search each mode for the selected molecule, applying labels for
			// prescence/abscence in the GUI
			for (Model model : models) {
				boolean found = false;
				String value = "";

                CrystalSystem crystalsystem = model.getScidata().getSystem().getCrystalSystem();
                    if(crystalsystem != null) {
						found = true;
						value = crystalsystem.getCrystalSystem();
						break;
					}

				if (found) {
					model.setLabel(value);
				} else {
					model.setLabel("None");
				}

				// Update the GUI
				grid.setItems(models);
			}
		});

		// Layout
		labelLayout.removeAll();
		labelLayout.add(moleculeSelect);

	}

	/**
	 * Draw an editor for a Molecule Prescence type filter
	 */
	private void drawFunctinoalGroupPrescenceEditor() {

		// If the lists hvensn't been created, search for all unique molecules
		if (molecules == null) {
			molecules = new HashSet<String>();

			for (Model model : models) {
				List<FunctionalGroup> functionalGroups = model.getScidata().getSystem().getFunctionalGroups();
				for (FunctionalGroup group : functionalGroups) {
					if (group.getAtoms() != null) {
						molecules.add(group.getAtoms());
					}
				}
			}
		}

		// A selector for which molecule to build the label on
		Select<String> moleculeSelect = new Select<String>();
		moleculeSelect.setLabel("Functional group to detect");
		moleculeSelect.setItems(molecules);

		// If the filter is already initialized, show t
		if (filter.getLabel().size() > 0) {
			for (String label : filter.getLabel()) {
				if (label.contains("SSM:PRESENT:atoms:")) {
					moleculeSelect.setValue(label.substring(18));
				}
			}
		}

		moleculeSelect.addValueChangeListener(e -> {

			// Set the molecule to the filter definition
			filter.setLabel(Arrays.asList("scidata", "system", "facets", "SSM:PRESENT:atoms:" + e.getValue()));

			// Search each mode for the selected molecule, applying labels for
			// prescence/abscence in the GUI
			for (Model model : models) {
				boolean found = false;

				List<FunctionalGroup> functionalGroups = model.getScidata().getSystem().getFunctionalGroups();
				for (FunctionalGroup group : functionalGroups) {
					if (e.getValue().equals(group.getAtoms())) {
						found = true;
						break;
					}
				}

				if (found) {
					model.setLabel(e.getValue() + " present");
				} else {
					model.setLabel(e.getValue() + " absent");
				}

				// Update the GUI
				grid.setItems(models);

			}
		});

		// Layout
		labelLayout.removeAll();
		labelLayout.add(moleculeSelect);

	}

	/**
	 * Draw an editor for a Structure type filter
	 */
	private void drawStructureTypeEditor() {

		// If the lists hvensn't been created, search for all unique molecules
		if (structures == null) {
			structures = new HashSet<String>();

			for (Model model : models) {
				StructureType structureType = model.getScidata().getSystem().getStructureType();
				if (structureType.getStructureType() != null) {
				    structures.add(structureType.getStructureType());
				}
			}
		}

		// A selector for which molecule to build the label on
		Select<String> moleculeSelect = new Select<String>();
		moleculeSelect.setLabel("Structure type to detect");
		moleculeSelect.setItems(structures);

		// If the filter is already initialized, show t
		if (filter.getLabel().size() > 0) {
			for (String label : filter.getLabel()) {
				if (label.contains("SSM:PRESENT:structure type:")) {
					moleculeSelect.setValue(label.substring(27));
				}
			}
		}

		moleculeSelect.addValueChangeListener(e -> {

			// Set the molecule to the filter definition
			filter.setLabel(Arrays.asList("scidata", "system", "facets", "SSM:PRESENT:structure type:" + e.getValue()));

			// Search each mode for the selected molecule, applying labels for
			// prescence/abscence in the GUI
			for (Model model : models) {
				boolean found = false;
				String value = "";

				StructureType structureType = model.getScidata().getSystem().getStructureType();
				if (structureType.getStructureType() != null) {
					found = true;
					value = structureType.getStructureType();
					break;
				}

				if (found) {
					model.setLabel(value);
				} else {
					model.setLabel("None");
				}

				// Update the GUI
				grid.setItems(models);

			}
		});

		// Layout
		labelLayout.removeAll();
		labelLayout.add(moleculeSelect);

	}

	/**
	 * Draw an editor for a Uranium Coordination Chemistry type filter
	 */
	private void drawUraniumCoordinationChemistryEditor() {

		// If the lists hvensn't been created, search for all unique molecules
		if (coordinations == null) {
			coordinations = new HashSet<String>();

			for (Model model : models) {
				List<CoordinationChemistry> coordinationList =  model.getScidata().getSystem().getCoordinationChemistry();
				for(CoordinationChemistry coordination : coordinationList) {
					if (coordination.getCoordination() != null) {
						coordinations.add(coordination.getCoordination());
					}
				}
			}
		}

		// A selector for which molecule to build the label on
		Select<String> moleculeSelect = new Select<String>();
		moleculeSelect.setLabel("Uranium coordination chemistry to detect");
		moleculeSelect.setItems(coordinations);

		// If the filter is already initialized, show t
		if (filter.getLabel().size() > 0) {
			for (String label : filter.getLabel()) {
				if (label.contains("SSM:PRESENT:uranium coordination chemistry:")) {
					moleculeSelect.setValue(label.substring(43));
				}
			}
		}

		moleculeSelect.addValueChangeListener(e -> {

			// Set the molecule to the filter definition
			filter.setLabel(Arrays.asList("scidata", "system", "facets",
					"SSM:PRESENT:uranium coordination chemistry:" + e.getValue()));

			// Search each mode for the selected molecule, applying labels for
			// prescence/abscence in the GUI
			for (Model model : models) {
				String value = "None";

				// TODO: can have multple coordinations; need to draw on screen
				CoordinationChemistry coordination =  model.getScidata().getSystem().getCoordinationChemistry().get(0);
				if (coordination.getCoordination() != null) {
					value = coordination.getCoordination();
				}

				model.setLabel(value);

				// Update the GUI
				grid.setItems(models);

			}
		});

		// Layout
		labelLayout.removeAll();
		labelLayout.add(moleculeSelect);

	}

	public void refreshGrid() {
		grid.setItems(models);
	}
}
