package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object Representation of Material from the System
 * 
 * @author Robert Smith
 *
 */
public class Material {

    @JsonProperty("name")
	private String name;

    @JsonProperty("materialType")
	private String materialType;

	public Material() {
		name = "";
		materialType = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
}
