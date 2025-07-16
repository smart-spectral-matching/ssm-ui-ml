package gov.ornl.ssm.ml.ui.data;

import java.util.List;

/**
 * Object representation of the dataseries dictionaries from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Axis {

	private Parameter parameter;

	public Axis() {
		parameter = null;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
}
