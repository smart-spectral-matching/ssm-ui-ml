package gov.ornl.ssm.ml.ui.data;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.textfield.NumberField;

import gov.ornl.ssm.ml.ui.components.LabelFeaturesDialog;

/**
 * A feature defined by the ratio between the maximal and average values in a
 * range.
 * 
 * @author Robert Smith
 *
 */
public class PeakRatioRangeFeature implements Feature {
	
	/**
	 * The dialog where this feature will be displayed
	 */
	private LabelFeaturesDialog dialog;

	/**
	 * The filter whose feature is being edited.
	 */
	private Filter filter;
	
	/**
	 * The models which this feature will be used to extract data from.
	 */
	private List<Model> models;

	/**
	 * The default cosntructor.
	 * 
	 * @param filter
	 */
	public PeakRatioRangeFeature(Filter filter, List<Model> models, LabelFeaturesDialog dialog) {
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

		// A field to define the range start
		NumberField startField = new NumberField();
		startField.setLabel("Range Start");
		startField.setValue(0d);

		// A field to define the range end
		NumberField endField = new NumberField();
		endField.setLabel("Range End");
		endField.setValue(1d);
		
		//If the filter already has a definition for the filter, initialize the fields
		for(Object feature : filter.getFeatures().get(index)) {
			if(feature instanceof String && ((String) feature).startsWith("SSM:XY:axis:PEAK-RATIO-RANGE-")) {
				String[] tokens = ((String) feature).split("-");
				
				startField.setValue(Double.valueOf(tokens[3]));
				endField.setValue(Double.valueOf(tokens[4]));
				break;
			}
		}

		Feature finalThis = this;
		
		// A single listener for both controls
		ValueChangeListener<ComponentValueChangeEvent<NumberField, Double>> listener = new ValueChangeListener<ComponentValueChangeEvent<NumberField, Double>>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.vaadin.flow.component.HasValue.ValueChangeListener#valueChanged(com.
			 * vaadin.flow.component.HasValue.ValueChangeEvent)
			 */
			@Override
			public void valueChanged(ComponentValueChangeEvent<NumberField, Double> arg0) {

				// Set the filter path according to the range values
				filter.getFeatures().set(index,
						Arrays.asList("scidata", "dataseries",
								"SSM:XY:axis:PEAK-RATIO-RANGE-" + startField.getValue() + "-" + endField.getValue(),
								"parameter", "numericValueArray", 0, "numberArray"));
				
				//Update all models' validity
				for(Model model : models) {
					List<Double> axis = model.getScidata().getDataseries().get(0).getxAxis().getParameter().getNumericValueArray().get(0).getNumberArray();
					
					//If the feature range is at least partially covered by the data, it is valid
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
		return "Ratio of Highest Peak to Average Value in Range";
	}
}
