package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object Representation of Compound from the System
 * 
 * @author Robert Smith
 *
 */
public class Compound {

    @JsonProperty("name")
	private String name;

    @JsonProperty("formula")
	private String formula;

	public Compound() {
		name = "";
		formula = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
}
