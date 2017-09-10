package clientPresenter;

import clientModel.Model;
import clientView.View;

/**
 * SaveMazeCommand - strarts save command at the model
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class SaveMazeCommand implements Command {

	Model model;
	View view;
	
	/**
	 * COnstructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public SaveMazeCommand(Model model,View view){
		this.model=model;
		this.view=view;
	}
	
	/*
	 * Overridden command with needed implementation 
	 */
	@Override
	public void doCommand(String[] args) {
		
		if(args.length!=2)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		if(!model.mazeExists(args[0]))
		{
			view.displayMessage("\n!!!The maze "+args[0]+" doesn't exist!!!\n\n");
			return;
		}
		
		model.saveMaze(args[0], args[1]);
	}

}
