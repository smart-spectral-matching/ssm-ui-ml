package gov.ornl.ssm.ml.ui.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object representation of the list of axes from the Model JSON.
 * 
 * @author Robert Smith
 *
 */
public class AxisContainer {

	@JsonProperty("hasAxisType")
	private String hasAxisType;

	@JsonProperty("x-axis")
	private Axis xaxis;

	@JsonProperty("y-axis")
	private Axis yaxis;

	public AxisContainer() {
		setxAxis(null);
		setyAxis(null);
		setHasAxisType(null);
	}

	public String getHasAxisType() {
		return hasAxisType;
	}

	public void setHasAxisType(String hasAxisType) {
		this.hasAxisType = hasAxisType;
	}

	@JsonIgnore
	public Axis getAxis() {
		if (xaxis != null) {
			return xaxis;
		} else {
			return yaxis;
		}
	}

	public Axis getxAxis() {
		return xaxis;
	}

	public void setxAxis(Axis xaxis) {
		this.xaxis = xaxis;
	}

	public Axis getyAxis() {
		return yaxis;
	}

	public void setyAxis(Axis yaxis) {
		this.yaxis = yaxis;
	}
}
