package clientPresenter;

import algorithms.mazeGenerators.Maze3d;
import clientModel.Model;
import clientView.View;


/**
 * DisplayMazeCommand - gets maze from the model and sends it to the view
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class DisplayMazeCommand implements Command {

	private Model model;
	private View view;
	
	/**
	 * Constructor - creates a new command 
	 * @param model - model to set
	 * @param view - view to set
	 */
	public DisplayMazeCommand(Model model,View view){
		this.view=view;
		this.model=model;
	}
	
	/*
	 * Overridden function with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		
		if(args.length!=1)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		Maze3d maze=model.getMaze(args[0]);
		if(maze==null)
		{
			view.displayMessage("\n!!!The maze "+args[0]+" doesn't exist!!!\n\n");
			return;
		}
		view.displayMaze(maze);
	}
}
