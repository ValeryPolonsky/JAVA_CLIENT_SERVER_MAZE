package serverPresenter;

import serverModel.Model;
import serverView.View;

/**
 * SaveMazesAndSolutionsCommand - starts the saveMazesAndSolutions command
 * @author Valery Polosnky and Tomer Dricker
 *
 */
public class SaveMazesAndSolutionsCommand {
	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public SaveMazesAndSolutionsCommand(Model model,View view)
	{
		this.model=model;
		this.view=view;
		saveMazesAndSolutions();
	}
	
	/**
	 * Function that starts the saving mechanism
	 */
	private void saveMazesAndSolutions()
	{
		model.saveMazesAndSolutions();
	}

}
