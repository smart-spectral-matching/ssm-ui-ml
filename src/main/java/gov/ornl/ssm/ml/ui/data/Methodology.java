package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of a Methodology from the Model JSON,
 * 
 * @author Robert Smith
 *
 */
public class Methodology {

	private String evalutationMethod;

	public Methodology() {
		evalutationMethod = null;
	}

	public String getEvalutationMethod() {
		return evalutationMethod;
	}

	public void setEvalutationMethod(String evalutationMethod) {
		this.evalutationMethod = evalutationMethod;
	}
}
