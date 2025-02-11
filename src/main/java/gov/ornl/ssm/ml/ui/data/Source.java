package gov.ornl.ssm.ml.ui.data;

/**
 * Object Representation of entries in the sources list from the json
 * 
 * @author Robert Smith
 *
 */
public class Source {

	private String citation;

    private String doi;
	
	private String reftype;

    private String url;
	
	public Source() {
		citation = "";
		doi = "";
		reftype = "";
		url = "";
	}

	public String getCitation() {
		return citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

    public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getReftype() {
		return reftype;
	}

	public void setReftype(String reftype) {
		this.reftype = reftype;
	}
 
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
