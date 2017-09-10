package clientPresenter;

import clientModel.Model;
import clientView.View;


/**
 * LoadAndSetPropertiesCommand - starts load function in the model and than sets properties at the view
 * @author Valery Polosnky and Tomer Drciker
 *
 */
public class LoadAndSetPropertiesCommand {
	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public LoadAndSetPropertiesCommand(Model model,View view)
	{
		this.model=model;
		this.view=view;
		loadAndSetProperties();
	}
	
	
	/**
	 * Function that starts the mechanism of loading and setting properties
	 */
	public void loadAndSetProperties()
	{
		Properties dfp=model.loadProperties();
		view.setProperties(dfp);
	}
}
