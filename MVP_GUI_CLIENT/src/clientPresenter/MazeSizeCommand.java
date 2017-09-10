package clientPresenter;

import clientModel.Model;
import clientView.View;

/**
 * MazeSizeDisplay - gets maze size from the model and sends it to the view
 * @author Valery Polosnky and Tomer Dricker
 *
 */
public class MazeSizeCommand implements Command {

	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public MazeSizeCommand(Model model,View view){
		this.view=view;
		this.model=model;
	}
	
	/*
	 * Overriden command with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		
		if(args.length!=1)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		int mazeSize=model.getMazeSize(args[0]);
		if(mazeSize==-1)
		{
			view.displayMessage("\n!!!The maze "+args[0]+" doesn't exist!!!\n\n");
			return;
		}
		
		String message="\nTotal size of the maze "+args[0]+" is "+mazeSize+" bytes\n\n";
		view.displayMessage(message);
	}
}
