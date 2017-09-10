package serverPresenter;

import algorithms.search.Solution;
import serverModel.Model;
import serverView.View;

public class GetSolutionCommand implements Command {
	Model model;
	View view;
	
	public GetSolutionCommand(Model model,View view)
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
			//view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		Solution solution=model.getSolution(args[0]);
		if(solution==null)
		{
			//view.displayMessage("\n!!!There is no solution for "+args[0]+" maze!!!\n\n");
			return;
		}
		
		view.displaySolution(solution);
	}
}
