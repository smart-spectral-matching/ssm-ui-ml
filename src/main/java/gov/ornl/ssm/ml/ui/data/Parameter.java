package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of a Parameter from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Parameter {

	private String property;

	private String quantity;

	private ValueArray valuearray;

	public Parameter() {
		property = null;
		quantity = null;
		valuearray = null;
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

	public ValueArray getValuearray() {
		return valuearray;
	}

	public void setValuearray(ValueArray valuearray) {
		this.valuearray = valuearray;
	}
}
