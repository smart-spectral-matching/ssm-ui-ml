package gov.ornl.ssm.ml.ui.data;

public class MethodologyAspect {

	private Setting hasSetting;
	
	private String instrument;
	
	private String instrumentType;
	
	private String technique;
	
	private String techniqueType;
	
	public MethodologyAspect() {
		hasSetting = null;
		instrument = "";
		instrumentType = "";
		technique = "";
		techniqueType = "";
	}
	
	public Setting getHasSetting() {
		return hasSetting;
	}

	public void setHasSetting(Setting hasSetting) {
		this.hasSetting = hasSetting;
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
