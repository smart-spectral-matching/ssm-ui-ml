package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object Representation of CrystalSystem from the System
 * 
 * @author Robert Smith
 *
 */
public class CrystalSystem {

    @JsonProperty("crystal system")
	private String crystalsystem;

	public CrystalSystem() {
		crystalsystem = "";
	}

	public String getCrystalSystem() {
		return crystalsystem;
	}

	public void setCrystalSystem(String crystalsystem) {
		this.crystalsystem = crystalsystem;
	}
}
