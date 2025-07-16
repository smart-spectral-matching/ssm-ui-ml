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

	private List<AxisContainer> dataseries;
	
	private String description;
	
	private Methodology methodology;
	
	private String property;
	
	private System system;
	
	public Scidata() {
		dataseries = new ArrayList<AxisContainer>();
		description = null;
		methodology = null;
		property = null;
		system = null;
	}

	public List<AxisContainer> getDataseries() {
		return dataseries;
	}

	public void setDataseries(List<AxisContainer> dataseries) {
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

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}
	
	
}
