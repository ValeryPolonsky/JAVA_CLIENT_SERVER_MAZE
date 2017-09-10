package serverPresenter;

import algorithms.search.Solution;
import serverModel.Model;
import serverView.View;


/**
 * DisplaySolutionCommand - gets solution from the model and sends it to the view
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class DisplaySolutionCommand implements Command {

	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public DisplaySolutionCommand(Model model,View view)
	{
		this.model=model;
		this.view=view;
	}
	
	/*
	 * Overridden command with needed implemetation
	 */
	@Override
	public void doCommand(String[] args) {
		if(args.length!=1)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		Solution solution=model.getSolution(args[0]);
		if(solution==null)
		{
			view.displayMessage("\n!!!There is no solution for "+args[0]+" maze!!!\n\n");
			return;
		}
		
		String message="The solution of maze "+args[0]+" is: ";
		view.displayMessage(message);
		view.displaySolution(solution);
	}
}
