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

	private List<Double> numberArray;
	
	private String unitref;

	public ValueArray() {
		setNumberArray(new ArrayList<Double>());
		unitref = "";
	}

	public List<Double> getNumberArray() {
		return numberArray;
	}

	public void setNumberArray(List<Double> numberarray) {
		this.numberArray = numberarray;
	}

	public String getUnitref() {
		return unitref;
	}

	public void setUnitref(String unitref) {
		this.unitref = unitref;
	}

}
