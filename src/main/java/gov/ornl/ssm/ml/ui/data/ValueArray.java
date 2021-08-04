package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of the valuearray from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class ValueArray {

	private List<Double> numberarray;

	public ValueArray() {
		setNumberarray(new ArrayList<Double>());
	}

	public List<Double> getNumberarray() {
		return numberarray;
	}

	public void setNumberarray(List<Double> numberarray) {
		this.numberarray = numberarray;
	}

}
