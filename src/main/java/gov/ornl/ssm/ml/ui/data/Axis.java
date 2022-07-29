package gov.ornl.ssm.ml.ui.data;

import java.util.List;

/**
 * Object representation of the dataseries dictionaries from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Axis {

	private String hasAxisType;

	private String label;

	private Parameter parameter;

	public Axis() {
		setHasAxisType(null);
		label = null;
		parameter = null;
	}

	public String getHasAxisType() {
		return hasAxisType;
	}

	public void setHasAxisType(String hasAxisType) {
		this.hasAxisType = hasAxisType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
}
