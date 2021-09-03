package gov.ornl.ssm.ml.ui.data;

import java.util.List;

/**
 * Object representation of the dataseries dictionaries from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Axis {

	private String axisType;

	private String label;

	private Parameter parameter;

	public Axis() {
		axisType = null;
		label = null;
		parameter = null;
	}

	public String getAxisType() {
		return axisType;
	}

	public void setAxisType(String axis) {
		this.axisType = axis;
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
