package clientPresenter;

import algorithms.mazeGenerators.Maze3d;
import clientModel.Model;
import clientView.View;


/**
 * DisplayCrossSectionCommand - gets cross section from the model and sends it to the view
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class DisplayCrossSectionCommand implements Command {

	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public DisplayCrossSectionCommand(Model model,View view){
		this.view=view;
		this.model=model;
	}
	
	/*
	 * Overriden command with neede implementation
	 */
	@Override
	public void doCommand(String[] args) {
		Maze3d maze=model.getMaze(args[2]);
		
		if(args.length!=3)
		{
			view.displayMessage("\n!!!Number of arguments is wrong!!!\n\n");
			return;
		}
		if(maze==null)
		{
			view.displayMessage("\n!!!The maze "+args[2]+" doesn't exists!!!\n\n");
			return;
		}
		if(!args[0].equals("x")&&!args[0].equals("y")&&!args[0].equals("z"))
		{
			view.displayMessage("\n!!!Wrong axis input!!!\n\n");
			return;
		}
		if(!isInteger(args[1]))
		{
			view.displayMessage("\n!!!Axis index must be an integer!!!\n\n");
			return;
		}
		if(args[0].equals("x")&&Integer.parseInt(args[1])>=maze.getLength())
		{
			view.displayMessage("\n!!!Axis index must be smaller than the length of the maze!!!\n\n");
			return;
		}
		if(args[0].equals("y")&&Integer.parseInt(args[1])>=maze.getHeight())
		{
			view.displayMessage("\n!!!Axis index must be smaller than the height of the maze!!!\n\n");
			return;
		}
		if(args[0].equals("z")&&Integer.parseInt(args[1])>=maze.getLength())
		{
			view.displayMessage("\n!!!Axis index must be smaller than the width of the maze!!!\n\n");
			return;
		}
		
		view.displayCrossSection(maze,args[0],Integer.parseInt(args[1]));
	}
	
	private static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	private static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}
