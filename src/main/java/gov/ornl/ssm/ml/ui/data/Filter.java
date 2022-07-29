package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of a JSON Filter.
 * 
 * @author Robert Smith
 *
 */
public class Filter {

	private List<List<Object>> features;

	private List<String> label;

	public Filter() {
		setFeatures(new ArrayList<List<Object>>());
		setLabel(new ArrayList<String>());
	}

	@JsonProperty("Features")
	public List<List<Object>> getFeatures() {
		return features;
	}

	public void setFeatures(List<List<Object>> features) {
		this.features = features;
	}

	@JsonProperty("Label")
	public List<String> getLabel() {
		return label;
	}

	public void setLabel(List<String> labels) {
		this.label = labels;
	}
}
