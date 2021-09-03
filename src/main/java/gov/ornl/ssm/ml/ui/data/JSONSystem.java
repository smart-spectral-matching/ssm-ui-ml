package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of the System from the Model JSON
 * 
 * @author Robert Smith
 *
 */
public class JSONSystem {

	private List<Facet> facets;

	public JSONSystem() {
		facets = new ArrayList<Facet>();
	}

	public List<Facet> getFacets() {
		return facets;
	}

	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}
}
