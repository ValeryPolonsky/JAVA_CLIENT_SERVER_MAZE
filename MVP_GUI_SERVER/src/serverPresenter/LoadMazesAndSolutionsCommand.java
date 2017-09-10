package serverPresenter;

import serverModel.Model;
import serverView.View;
/**
 * LoadMazesAndSolutionsCommand - starts loadMazesAndSolutions command at the model
 * @author Valery Polosnky and Tomer Dricker
 *
 */
public class LoadMazesAndSolutionsCommand {
	
	Model model;
	View view;
	
	/**
	 * COnstructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public LoadMazesAndSolutionsCommand(Model model,View view)
	{
		this.model=model;
		this.view=view;
		loadMazesAndSolutions();
	}
	
	/**
	 * Function that starts the loading mechanism
	 */
	private void loadMazesAndSolutions()
	{
		model.loadMazesAndSolutions();
	}
}
