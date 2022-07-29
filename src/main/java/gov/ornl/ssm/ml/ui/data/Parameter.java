package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;

/**
 * Object representation of a Parameter from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Parameter {

	private String property;

	private String quantity;

	private ArrayList<ValueArray> numericValueArray;

	public Parameter() {
		property = null;
		quantity = null;
		numericValueArray = new ArrayList<ValueArray>();
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public ArrayList<ValueArray> getNumericValueArray() {
		return numericValueArray;
	}

	public void setNumericValueArray(ArrayList<ValueArray> valuearray) {
		this.numericValueArray = valuearray;
	}
}
