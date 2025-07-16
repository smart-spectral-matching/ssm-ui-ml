package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the Setting in the json.
 * 
 * @author Robert Smith
 *
 */
public class Setting {


    private String id;

    private String type;

    private String property;
	
	private String quantity;
	
	private Value value;

    public Setting() {
		setxId("");
		setxType("");
		setProperty(null);
		setQuantity(null);
		setValue(null);
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

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
