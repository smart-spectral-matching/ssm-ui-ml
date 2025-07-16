package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of a Parameter from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Parameter {

	private String property;

	private String quantity;

	private String units;

    @JsonProperty("datatype")
	private String dataType;

    @JsonProperty("numericValueArray")
	private List<ValueArray> numericValueArray;

	public Parameter() {
		dataType = null;
		property = null;
		quantity = null;
		units = null;
		numericValueArray = new ArrayList<ValueArray>();
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public List<ValueArray> getNumericValueArray() {
		return numericValueArray;
	}

	public void setNumericValueArray(List<ValueArray> valuearray) {
		this.numericValueArray = valuearray;
	}
}
