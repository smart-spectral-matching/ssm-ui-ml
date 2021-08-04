package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of the Facet from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Facet {

	private String atoms;

	private String materialType;

	private String multiplicity;

	public Facet() {
		atoms = null;
		materialType = null;
		multiplicity = null;
	}

	public String getAtoms() {
		return atoms;
	}

	public void setAtoms(String atoms) {
		this.atoms = atoms;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

}
