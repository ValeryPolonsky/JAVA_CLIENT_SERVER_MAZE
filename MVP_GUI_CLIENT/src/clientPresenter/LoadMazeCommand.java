package clientPresenter;

import clientModel.Model;
import clientView.View;

/**
 * LoadMazeCommand - starts loadMaze command at the model
 * @author Valery Polosnky and Tomer Dricker
 *
 */
public class LoadMazeCommand implements Command {

	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model
	 * @param view
	 */
	public LoadMazeCommand(Model model,View view){
		this.model=model;
		this.view=view;
	}
	
	/*
	 * Overridden function with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		if(args.length!=2)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		model.loadMaze(args[0], args[1]);
	}
}
