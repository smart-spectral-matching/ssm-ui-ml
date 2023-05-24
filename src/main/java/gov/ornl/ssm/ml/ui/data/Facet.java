package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the Facet from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Facet {

	private String aggregation;
	
	private String atoms;
	
	private String chebi;
	
	private ArrayList<String> constituents;
	
	@JsonProperty("crystal system")
	private String crystalSystem;
	
	private String description;
	
	private String formula;
	
	private String inchi;
	
	private String inchikey;
	
	private String iupacname;

	private String materialType;
	
	private ArrayList<String> mixtype;
	
	private String molweight;

	private String multiplicity;
	
	private String phase;
	
	@JsonProperty("structure type")
	private String structureType;

	@JsonProperty("uranium coordination chemistry")
	private String uraniumCoordinationChemistry;

	public Facet() {
		aggregation = "";
		atoms = null;
		chebi = "";
		constituents = new ArrayList<String>();
		crystalSystem = null;
		description = "";
		formula = "";
		inchi = "";
		inchikey = "";
		iupacname = "";
		materialType = "";
		mixtype = new ArrayList<String>();
		molweight = "";
		multiplicity = "";
		phase = "";
		structureType = null;
		uraniumCoordinationChemistry = null;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public String getAtoms() {
		return atoms;
	}

	public void setAtoms(String atoms) {
		this.atoms = atoms;
	}

	public String getChebi() {
		return chebi;
	}

	public void setChebi(String chebi) {
		this.chebi = chebi;
	}

	public ArrayList<String> getConstituents() {
		return constituents;
	}

	public void setConstituents(ArrayList<String> constituents) {
		this.constituents = constituents;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getInchi() {
		return inchi;
	}

	public void setInchi(String inchi) {
		this.inchi = inchi;
	}

	public String getInchikey() {
		return inchikey;
	}

	public void setInchikey(String inchikey) {
		this.inchikey = inchikey;
	}

	public String getIupacname() {
		return iupacname;
	}

	public void setIupacname(String iupacname) {
		this.iupacname = iupacname;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public ArrayList<String> getMixtype() {
		return mixtype;
	}

	public void setMixtype(ArrayList<String> mixType) {
		this.mixtype = mixType;
	}

	public String getMolweight() {
		return molweight;
	}

	public void setMolweight(String molweight) {
		this.molweight = molweight;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
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
