package gov.ornl.ssm.ml.ui.data;

import java.util.List;

/**
 * Object representation of the dataseries dictionaries from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Axis {

	private String axis;

	private String label;

	private Parameter parameter;

	public Axis() {
		axis = null;
		label = null;
		parameter = null;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
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
