package gov.ornl.ssm.ml.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the Dataseries from the model JSON.
 * 
 * @author Robert Smith
 *
 */
public class Dataseries {

	private Axis xAxis;
	
	private Axis yAxis;
	
	public Dataseries() {
		xAxis = null;
		yAxis = null;
	}

	@JsonProperty("x-axis")
	public Axis getxAxis() {
		return xAxis;
	}

	public void setxAxis(Axis xAxis) {
		this.xAxis = xAxis;
	}

	@JsonProperty("y-axis")
	public Axis getyAxis() {
		return yAxis;
	}

	public void setyAxis(Axis yAxis) {
		this.yAxis = yAxis;
	}
	
	
}
