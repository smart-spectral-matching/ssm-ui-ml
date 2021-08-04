package gov.ornl.ssm.ml.ui.data;

import com.vaadin.flow.component.HasComponents;

/**
 * A Feature as represented in the GUI.
 * 
 * @author Robert Smith
 *
 */
public interface Feature {

	/**
	 * Draw this feature's editor in the given layout.
	 * 
	 * @param layout
	 *            The layout where the editor will be drawn.
	 * @param index
	 *            The index of this feature in the containing Filter's list of
	 *            features
	 */
	public void draw(HasComponents layout, int index);

}
