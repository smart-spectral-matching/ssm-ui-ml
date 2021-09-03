package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of a Methodology from the Model JSON,
 * 
 * @author Robert Smith
 *
 */
public class Methodology {

	private String evaluationMethod;

	public Methodology() {
		evaluationMethod = null;
	}

	public String getEvaluationMethod() {
		return evaluationMethod;
	}

	public void setEvaluationMethod(String evalutationMethod) {
		this.evaluationMethod = evalutationMethod;
	}
}
