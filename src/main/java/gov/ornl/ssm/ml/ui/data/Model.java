package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Object representation of a JSON Model.
 * 
 * @author Robert Smith
 *
 */
public class Model {

	private String created;
	
	private String full;
	
	private String label;
	
	private String modified;
	
	private List<Feature> invalidFeatures;
	
	private Scidata scidata;
	
	private String title;
	
	private String url;
	
	private String uuid;
	
	public Model() {
		setCreated(null);
		full = "";
		setLabel("");
		setModified(null);
		setScidata(null);
		setTitle(null);
		setUrl(null);
		setUuid(null);
		invalidFeatures = new ArrayList<Feature>();
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getFull() {
		return full;
	}

	public void setFull(String full) {
		this.full = full;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	@JsonIgnore
	public List<Feature> getInvalidFeatures() {
		return invalidFeatures;
	}

	@JsonIgnore
	public void setInvalidFeatures(List<Feature> invalidFeatures) {
		this.invalidFeatures = invalidFeatures;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Scidata getScidata() {
		return scidata;
	}

	public void setScidata(Scidata scidata) {
		this.scidata = scidata;
	}
}
