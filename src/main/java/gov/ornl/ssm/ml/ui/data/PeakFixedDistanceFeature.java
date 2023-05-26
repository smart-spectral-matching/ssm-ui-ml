package gov.ornl.ssm.ml.ui.data;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.textfield.NumberField;

import gov.ornl.ssm.ml.ui.components.LabelFeaturesDialog;

/**
 * A feature defined by the presence/absence of a number of peaks with fixed
 * distance between them inside a given range.
 * 
 * @author Robert Smith
 * 
 */
public class PeakFixedDistanceFeature implements Feature {

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
	public PeakFixedDistanceFeature(Filter filter, List<Model> models, LabelFeaturesDialog dialog) {
		this.filter = filter;
		this.models = models;
		this.dialog = dialog;
	}

	@Override
	public void draw(HasComponents layout, int index) {

		// A field for the range start
		NumberField startField = new NumberField();
		startField.setLabel("Range Start");
		startField.setValue(0d);

		// A field for the range end
		NumberField endField = new NumberField();
		endField.setLabel("Range End");
		endField.setValue(10d);

		// A field for the peak distance
		NumberField distanceField = new NumberField();
		distanceField.setLabel("Fixed Distance Between Peaks");
		distanceField.setValue(2d);

		// A field for the tolerance
		NumberField toleranceField = new NumberField();
		toleranceField.setLabel("Tolerance for Acceptable Range Around Fixed Distance");
		toleranceField.setValue(1d);

		// A field for how many peaks to find
		NumberField peaksField = new NumberField();
		peaksField.setLabel("Number of Peaks to Find");
		peaksField.setValue(2d);

		// A field for the minimum peak width
		NumberField widthField = new NumberField();
		widthField.setLabel("Minimum Peak Width Expressed As Number of Values");
		widthField.setValue(2d);

		// Initialize the input fields if the filter already has a definition
		for (Object feature : filter.getFeatures().get(index)) {
			if (feature instanceof String && ((String) feature).startsWith("SSM:XY:axis:PEAK-DISTANCE")) {
				String[] tokens = ((String) feature).split("-");

				startField.setValue(Double.valueOf(tokens[1]));
				endField.setValue(Double.valueOf(tokens[2]));
				distanceField.setValue(Double.valueOf(tokens[3]));
				toleranceField.setValue(Double.valueOf(tokens[4]));
				peaksField.setValue(Double.valueOf(tokens[5]));
				widthField.setValue(Double.valueOf(tokens[6]));
				break;
			}
		}

		//Final reference to this
		final PeakFixedDistanceFeature finalThis = this;
		
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
								"SSM:XY:axis:PEAK-DISTANCE-" + startField.getValue() + "-" + endField.getValue() + "-"
										+ distanceField.getValue() + "-" + toleranceField.getValue() + "-" + peaksField.getValue() + "-" + widthField.getValue(),
								"parameter", "numericValueArray", 0, "numberArray"));

				// Update all models' validity
				for (Model model : models) {
					List<Double> axis = model.getScidata().getDataseries().get(0).getxAxis().getParameter()
							.getNumericValueArray().getNumberArray();

					// If the feature range is at least partially covered by the data, it is valid
					if (axis.get(0) > endField.getValue() || axis.get(axis.size() - 1) < startField.getValue()) {
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
		distanceField.addValueChangeListener(listener);
		toleranceField.addValueChangeListener(listener);
		peaksField.addValueChangeListener(listener);
		widthField.addValueChangeListener(listener);

		// Layout
		layout.add(startField);
		layout.add(endField);
		layout.add(distanceField);
		layout.add(toleranceField);
		layout.add(peaksField);
		layout.add(widthField);
	}

	/**
	 * Get a human readable name for this type of feature.
	 * 
	 * @return
	 */
	public static String getName() {
		return "Number of Peaks with Fixed Distance in Range";
	}
}
