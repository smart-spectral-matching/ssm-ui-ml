package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of the Facet from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Facet {

	private String atoms;
	
	private String formula;

	private String materialType;

	private String multiplicity;

	public Facet() {
		atoms = "";
		formula = "";
		materialType = "";
		multiplicity = "";
	}

	public String getAtoms() {
		return atoms;
	}

	public void setAtoms(String atoms) {
		this.atoms = atoms;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
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
