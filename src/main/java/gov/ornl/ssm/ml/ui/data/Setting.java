package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of the Setting in the json.
 * 
 * @author Robert Smith
 *
 */
public class Setting {

	private Value hasValue;
	
	private String property;
	
	private String quantity;
	
	public Setting() {
		setHasValue(null);
		setProperty(null);
		setQuantity(null);
	}

	public Value getHasValue() {
		return hasValue;
	}

	public void setHasValue(Value hasValue) {
		this.hasValue = hasValue;
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
	
	
}
