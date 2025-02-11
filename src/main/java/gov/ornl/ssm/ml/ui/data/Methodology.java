package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of a Methodology from the Model JSON,
 * 
 * @author Robert Smith
 *
 */
public class Methodology {

	private String technique;

    private String techniqueType;
	
    private String instrumentType;

    private List<Setting> settings;

    public Methodology() {
		technique = "";
		techniqueType = "";
		instrumentType = "";
        settings = new ArrayList<Setting>();
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

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

    public List<Setting> getSettings() {
        return settings;
    }
 
    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

}
