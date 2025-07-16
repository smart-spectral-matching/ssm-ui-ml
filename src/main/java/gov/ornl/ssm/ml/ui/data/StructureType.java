package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object Representation of StructureType from the System
 * 
 * @author Robert Smith
 *
 */
public class StructureType {

    @JsonProperty("structure type")
	private String structuretype;

	public StructureType() {
		structuretype = "";
	}

	public String getStructureType() {
		return structuretype;
	}

	public void setStructureType(String structuretype) {
		this.structuretype = structuretype;
	}
}
