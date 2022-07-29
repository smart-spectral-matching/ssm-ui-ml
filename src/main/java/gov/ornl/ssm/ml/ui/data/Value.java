package gov.ornl.ssm.ml.ui.data;

/**
 * Object representation of the value in the json
 * 
 * @author Robert Smith
 *
 */
public class Value {

	private String number;
	
	private String unitref;
	
	private String unitstr;
	
	public Value() {
		setNumber("");
		setUnitstr("");
		unitref = "";
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
