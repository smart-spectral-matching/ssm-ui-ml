package gov.ornl.ssm.ml.ui.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the System from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class System {

    @JsonProperty("compound")
	private Compound compound;

    @JsonProperty("coordinationchemistry")
	private List<CoordinationChemistry> coordinationchemistry;

    @JsonProperty("crystalsystem")
    private CrystalSystem crystalsystem;

    @JsonProperty("functionalgroup")
    private List<FunctionalGroup> functionalgroups;

    @JsonProperty("material")
	private Material material;

	@JsonProperty("structuretype")
    private StructureType structuretype;

   	public System() {
        compound = null;
        coordinationchemistry = new ArrayList<CoordinationChemistry>();
        crystalsystem = null;
		functionalgroups = new ArrayList<FunctionalGroup>();
        material = null;
        structuretype = null;
	}

    public Compound getCompound() {
		return compound;
	}

	public void setCompound(Compound compound) {
		this.compound = compound;
	}

    public List<CoordinationChemistry> getCoordinationChemistry() {
		return coordinationchemistry;
	}

	public void setCoordinationChemistry(List<CoordinationChemistry> coordinationchemistry) {
		this.coordinationchemistry = coordinationchemistry;
	}

    public CrystalSystem getCrystalSystem() {
		return crystalsystem;
	}

	public void setCrystalSystem(CrystalSystem crystalsystem) {
		this.crystalsystem = crystalsystem;
	}

    public List<FunctionalGroup> getFunctionalGroups() {
		return functionalgroups;
	}

	public void setFunctionalGroups(List<FunctionalGroup> functionalgroups) {
		this.functionalgroups = functionalgroups;
	}

    public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

    public StructureType getStructureType() {
		return structuretype;
	}

	public void setStructureType(StructureType structuretype) {
		this.structuretype = structuretype;
	}
}
