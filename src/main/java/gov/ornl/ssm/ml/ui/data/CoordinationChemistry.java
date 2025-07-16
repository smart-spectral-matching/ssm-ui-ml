package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object Representation of CoordinationChemistry from the System
 * 
 * @author Robert Smith
 *
 */
public class CoordinationChemistry {

    @JsonProperty("uranium coordination chemistry")
	private String coordination;

    @JsonProperty("multiplicity")
    private String multiplicity;

	public CoordinationChemistry() {
		coordination = "";
		multiplicity = "";
	}

	public String getCoordination() {
		return coordination;
	}

	public void setCoordination(String coordination) {
		this.coordination = coordination;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
}
