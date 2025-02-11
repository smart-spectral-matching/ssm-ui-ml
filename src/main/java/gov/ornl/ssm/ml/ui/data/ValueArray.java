package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the valuearray from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class ValueArray {

    @JsonProperty("numberArray")
	private List<Double> numberArray;

	public ValueArray() {
		setNumberArray(new ArrayList<Double>());
	}

	public List<Double> getNumberArray() {
		return numberArray;
	}

	public void setNumberArray(List<Double> numberarray) {
		this.numberArray = numberarray;
	}

}
