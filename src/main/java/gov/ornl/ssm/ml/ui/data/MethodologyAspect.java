package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// FIXME JSON schema should require hasSetting is always a list, instead of being an object when only one exists.
@JsonIgnoreProperties({"hasSetting"})
public class MethodologyAspect {
	
	private String instrument;
	
	private String instrumentType;
	
	private String technique;
	
	private String techniqueType;
	
	public MethodologyAspect() {
		instrument = "";
		instrumentType = "";
		technique = "";
		techniqueType = "";
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getTechnique() {
		return technique;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}

	public String getTechniqueType() {
		return techniqueType;
	}

	public void setTechniqueType(String techniqueType) {
		this.techniqueType = techniqueType;
	}
}
