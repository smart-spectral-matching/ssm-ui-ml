package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the Facet from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Facet {

	private String atoms;
	
	@JsonProperty("crystal system")
	private String crystalSystem;
	
	private String formula;

	private String materialType;

	private String multiplicity;
	
	@JsonProperty("structure type")
	private String structureType;
	

	@JsonProperty("uranium coordination chemistry")
	private String uraniumCoordinationChemistry;

	public Facet() {
		atoms = null;
		crystalSystem = null;
		formula = "";
		materialType = "";
		multiplicity = "";
		structureType = null;
		uraniumCoordinationChemistry = null;
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

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public String getUraniumCoordinationChemistry() {
		return uraniumCoordinationChemistry;
	}

	public void setUraniumCoordinationChemistry(String uraniumCoordinationChemistry) {
		this.uraniumCoordinationChemistry = uraniumCoordinationChemistry;
	}

	public String getCrystalSystem() {
		return crystalSystem;
	}

	public void setCrystalSystem(String crystalSystem) {
		this.crystalSystem = crystalSystem;
	}


}
