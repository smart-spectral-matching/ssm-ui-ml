package gov.ornl.ssm.ml.ui.data;

/**
 * Object Representation of entries in the sources list from the json
 * 
 * @author Robert Smith
 *
 */
public class Source {

	private String bibliographicCitation;
	
	private String type;
	
	public Source() {
		bibliographicCitation = "";
		type = "";
	}

	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
