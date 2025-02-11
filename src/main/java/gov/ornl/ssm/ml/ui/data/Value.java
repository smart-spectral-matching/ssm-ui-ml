package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the value in the json
 * 
 * @author Robert Smith
 *
 */
public class Value {

    private String id;
 
    private String type;

	private String number;
	
	private String unitref;
	
	private String unitstr;
	
	public Value() {
        setxId("");
        setxType("");
        setNumber("");
		setUnitstr("");
		unitref = "";
	}

    @JsonProperty("@id")
    public String getxId() {
        return id;
    }
 
    @JsonProperty("@id")
    public void setxId(String id) {
        this.id = id;
    }
 
    @JsonProperty("@type")
    public String getxType() {
        return id;
    }
 
    @JsonProperty("@type")
    public void setxType(String type) {
        this.type = type;
    }

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUnitstr() {
		return unitstr;
	}

	public void setUnitstr(String unitstr) {
		this.unitstr = unitstr;
	}

	public String getUnitref() {
		return unitref;
	}

	public void setUnitref(String unitref) {
		this.unitref = unitref;
	}
}
