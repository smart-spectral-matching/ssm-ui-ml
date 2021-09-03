package gov.ornl.ssm.ml.ui.data;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.textfield.NumberField;

import gov.ornl.ssm.ml.ui.components.LabelFeaturesDialog;

/**
 * A feature defined by the location of the maximal value in a range.
 * 
 * @author Robert Smith
 *
 */
public class PeakLocationRangeFeature implements Feature {

	/**
	 * The dialog that will display this feature
	 */
	private LabelFeaturesDialog dialog;
	
	/**
	 * The filter whose feature this represents
	 */
	private Filter filter;
	
	/**
	 * List of models this feature will be used to extract data from.
	 */
	private List<Model> models;

	/**
	 * The default constructor.
	 * 
	 * @param filter
	 */
	public PeakLocationRangeFeature(Filter filter, List<Model> models, LabelFeaturesDialog dialog) {
		this.filter = filter;
		this.models = models;
		this.dialog = dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.ornl.ssm.ml.ui.data.Feature#draw(com.vaadin.flow.component.HasComponents,
	 * int)
	 */
	@Override
	public void draw(HasComponents layout, int index) {

		// A field for the range start
		NumberField startField = new NumberField();
		startField.setLabel("Range Start");
		startField.setValue(0d);

		// A field for the range end
		NumberField endField = new NumberField();
		endField.setLabel("Range End");
		endField.setValue(1d);
		
		//Initialize the input fields if the filter already has a definition
		for(String feature : filter.getFeatures().get(index)) {
			if(feature.startsWith("SSM:XY:axis:PEAK-LOC-RANGE-")) {
				String[] tokens = feature.split("-");
				
				startField.setValue(Double.valueOf(tokens[3]));
				endField.setValue(Double.valueOf(tokens[4]));
				break;
			}
		}
		
		Feature finalThis = this;

		// A listener for both the controls
		ValueChangeListener<ComponentValueChangeEvent<NumberField, Double>> listener = new ValueChangeListener<ComponentValueChangeEvent<NumberField, Double>>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.vaadin.flow.component.HasValue.ValueChangeListener#valueChanged(com.
			 * vaadin.flow.component.HasValue.ValueChangeEvent)
			 */
			@Override
			public void valueChanged(ComponentValueChangeEvent<NumberField, Double> arg0) {

				// Set the range based on the controls
				filter.getFeatures().set(index,
						Arrays.asList("scidata", "dataseries",
								"SSM:XY:axis:PEAK-LOC-RANGE-" + startField.getValue() + "-" + endField.getValue(),
								"parameter", "numericValueArray", "numberArray"));
				
				//Update all models' validity
				for(Model model : models) {
					
					//Get the x axis
					List<Double> axis = model.getScidata().getDataseries().get(0).getxAxis().getParameter().getNumericValueArray().getNumberArray();
					
					//If the defined range is entirely outside the axis's range, this model is invalid.
					if(axis.get(0) > endField.getValue() || axis.get(axis.size() - 1) < startField.getValue()) {
						model.getInvalidFeatures().add(finalThis);
					} else {
						model.getInvalidFeatures().remove(finalThis);
					}
				}
				
				dialog.refreshGrid();
			}

		};

		startField.addValueChangeListener(listener);
		endField.addValueChangeListener(listener);

		// Layout
		layout.add(startField);
		layout.add(endField);
	}
	
	/**
	 * Get a human readable name for this type of feature.
	 * 
	 * @return
	 */
	public static String getName() {
		return "Highest Peak Location in Range";
	}
}
