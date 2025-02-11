package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of the Scidata from the Model JSON
 * 
 * @author Robert Smith
 *
 */
public class Scidata {

	private List<Dataseries> dataseries;
	
	private String description;
	
	private Methodology methodology;
	
	private String property;
	
	private JSONSystem system;
	
	public Scidata() {
		dataseries = new ArrayList<Dataseries>();
		description = null;
		methodology = null;
		property = null;
		system = null;
	}

	public List<Dataseries> getDataseries() {
		return dataseries;
	}

	public void setDataseries(List<Dataseries> dataseries) {
		this.dataseries = dataseries;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Methodology getMethodology() {
		return methodology;
	}

	public void setMethodology(Methodology methodology) {
		this.methodology = methodology;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public JSONSystem getSystem() {
		return system;
	}

	public void setSystem(JSONSystem system) {
		this.system = system;
	}
	
	
}
