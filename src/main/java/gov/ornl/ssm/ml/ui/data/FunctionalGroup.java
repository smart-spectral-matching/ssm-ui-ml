package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object Representation of FunctionalGroup from the System
 * 
 * @author Robert Smith
 *
 */
public class FunctionalGroup {

    @JsonProperty("atoms")
	private String atoms;

	@JsonProperty("multiplicity")
	private String multiplicity;

	public FunctionalGroup() {
		atoms = "";
		multiplicity = "";
	}

	public String getAtoms() {
		return atoms;
	}

	public void setAtoms(String atoms) {
		this.atoms = atoms;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
}
