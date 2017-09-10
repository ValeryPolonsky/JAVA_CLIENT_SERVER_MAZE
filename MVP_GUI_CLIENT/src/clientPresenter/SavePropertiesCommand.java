package clientPresenter;

import clientModel.Model;
import clientView.View;

/**
 * SavePropertiesCommand - starts the saveProperties function at the model
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class SavePropertiesCommand implements Command {
	Model model;
	View view;
	
	/**Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public SavePropertiesCommand(Model model, View view)
	{
		this.model=model;
		this.view=view;
	}

	/*
	 * Overridden command with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		// <algorithm , size , view>
		model.saveProperties(args[0],args[1],args[2]);
		view.displayMessage("Changes will take place after reload of the program");
	}
	
	

}
