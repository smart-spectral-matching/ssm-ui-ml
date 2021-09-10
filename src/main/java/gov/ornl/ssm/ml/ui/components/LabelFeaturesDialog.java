package gov.ornl.ssm.ml.ui.components;

import java.util.ArrayList;
import java.util.Arrays;
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

import gov.ornl.ssm.ml.ui.data.Facet;
import gov.ornl.ssm.ml.ui.data.Feature;
import gov.ornl.ssm.ml.ui.data.Filter;
import gov.ornl.ssm.ml.ui.data.Model;
import gov.ornl.ssm.ml.ui.data.PeakLocationRangeFeature;
import gov.ornl.ssm.ml.ui.data.PeakRatioRangeFeature;
import gov.ornl.ssm.ml.ui.data.PeaksRatioTwoRangeFeature;

/**
 * Dialog for defining the Label and Features for a Filter.
 * 
 * @author Robert Smith
 *
 */
public class LabelFeaturesDialog extends Dialog {

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
	 * List of all unique molecules named in any Facet in any Model
	 */
	private HashSet<String> molecules = null;

	/**
	 * Layout where the label editor will be drawn.
	 */
	private VerticalLayout labelLayout;

	/**
	 * Default constructor.
	 * 
	 * @param models
	 *            The models that the filter will be trained against.
	 * @param filter
	 *            The filter definition to be edited
	 */
	public LabelFeaturesDialog(List<Model> models, Filter filter) {

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
		
		//TODO Maybe display this condition as a color for the row instead of a column
		//Add a column for whether this model needs to be thrown out for not having a feature 
		grid.addColumn(new ValueProvider<Model, Boolean>(){

			@Override
			public Boolean apply(Model source) {
				return source.getInvalidFeatures().size() == 0;
			}
			
		}).setHeader("valid");

		// Selector for which type of label to use
		Select<String> labelSelect = new Select<String>("Functional Group Prescence");
		labelSelect.setLabel("Select Label Type");

		// Draw the editor for the kind of label the user selects
		labelSelect.addValueChangeListener(e -> {

			if ("Functional Group Prescence".equals(e.getValue())) {
				drawFunctinoalGroupPrescenceEditor();
			}
		});

		// List of all existing features
		ListBox<Feature> featureBox = new ListBox<Feature>();
		
		//Display the feature number in the list
		featureBox.setRenderer(new ComponentRenderer<Label,Feature>() {
			
			@Override
			public Label createComponent(Feature item) {
				
				String display = "Feature " + features.indexOf(item) + " ";
				if(item instanceof PeakLocationRangeFeature) {
					display += PeakLocationRangeFeature.getName();
				} else if (item instanceof PeakRatioRangeFeature) {
					display += PeakRatioRangeFeature.getName();
				}
				return new Label(display);
			}
		});

		// Selector for what kind of feature to add
		Select<String> featureSelect = new Select<String>(PeakLocationRangeFeature.getName(), PeakRatioRangeFeature.getName(), PeaksRatioTwoRangeFeature.getName());
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
						"valuearray", "numberarray"));
			} else if (PeakRatioRangeFeature.getName().equals(featureSelect.getValue())) {
				features.add(new PeakRatioRangeFeature(filter, models, this));
				featureBox.setItems(features);
				filter.getFeatures().add(Arrays.asList("scidata", "dataseries", "SSM:XY:axis:PEAK-RATIO-RANGE-0-1",
						"valuearray", "numberarray"));
			} else if (PeaksRatioTwoRangeFeature.getName().equals(featureSelect.getValue())) {
				features.add(new PeaksRatioTwoRangeFeature(filter, models, this));
				featureBox.setItems(features);
				filter.getFeatures().add(Arrays.asList("scidata", "dataseries", "SSM:XY:axis:PEAKS-RATIO-TWO-RANGES-0-1-2-3",
						"valuearray", "numberarray"));				
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

		// Layout
		add(grid);
		add(labelSelect);
		add(labelLayout);
		add(featureBox);
		add(featureSelect);
		add(addFeatureButton);
		add(removeFeatureButton);
		add(featureLayout);
		
		//If a label already exists, draw its editor
		if(filter.getLabel().size() > 0) {
			for(String label : filter.getLabel()) {
				
				//"PRESENT" means seearching for a molecule
				if(label.contains(":PRESENT")) {
					drawFunctinoalGroupPrescenceEditor();
					break;
				}
			}
		}
		
		//If the filter already has any filters, add them to the UI
		if(filter.getFeatures().size() > 0) {
			for(List<String> featureList : filter.getFeatures()) {
				for(String feature : featureList) {
					
					//Add the correct type of feature for the SSM command in the filter definition.
					if(feature.contains(":PEAK-LOC-RANGE-")) {
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
		
		//Update the feature box with the pre-existing features
		featureBox.setItems(features);
	}

	/**
	 * Draw an editor for a Molecule Prescence type filter
	 */
	private void drawFunctinoalGroupPrescenceEditor() {

		// If the molecule list hasn't been created, search for all unique molecules
		if (molecules == null) {
			molecules = new HashSet<String>();

			for (Model model : models) {
				for (Facet facet : model.getScidata().getSystem().getFacets()) {
					if (facet.getAtoms() != null) {
						molecules.add(facet.getAtoms());
					}
				}
			}
		}

		// A selector for which molecule to build the label on
		Select<String> moleculeSelect = new Select<String>();
		moleculeSelect.setLabel("Functional group to detect");
		moleculeSelect.setItems(molecules);
		
		//If the filter is already initialized, show t
		if(filter.getLabel().size() > 0) {
			for(String label : filter.getLabel()) {
				if(label.contains("SSM:PRESENT:atoms:")) {
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

				for (Facet facet : model.getScidata().getSystem().getFacets()) {
					if (e.getValue().equals(facet.getAtoms())) {
						found = true;
						break;
					}
				}

				if (found) {
					model.setLabel(e.getValue() + " present");
				} else {
					model.setLabel(e.getValue() + " absent");
				}
				
				//Update the GUI
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
