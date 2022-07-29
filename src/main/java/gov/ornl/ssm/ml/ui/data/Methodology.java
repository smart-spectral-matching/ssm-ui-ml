package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of a Methodology from the Model JSON,
 * 
 * @author Robert Smith
 *
 */
public class Methodology {

	private String evaluationMethod;
	
	private MethodologyAspect hasMethodologyAspect;

	public Methodology() {
		evaluationMethod = "";
		hasMethodologyAspect = null;
	}

	public String getEvaluationMethod() {
		return evaluationMethod;
	}

	public void setEvaluationMethod(String evalutationMethod) {
		this.evaluationMethod = evalutationMethod;
	}

	public MethodologyAspect getHasMethodologyAspect() {
		return hasMethodologyAspect;
	}

	public void setHasMethodologyAspect(MethodologyAspect hasMethodologyAspect) {
		this.hasMethodologyAspect = hasMethodologyAspect;
	}

}
