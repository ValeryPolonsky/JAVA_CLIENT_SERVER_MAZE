package serverPresenter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import algorithms.search.Solution;
import serverModel.Model;
import serverView.View;

/**
 * SolveMazeCommand - starts solveMaze function at the model
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class SolveMazeCommand implements Command {

	Model model;
	View view;
	ExecutorService executor;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 * @param executor - executor to set
	 */
	public SolveMazeCommand(Model model,View view,ExecutorService executor)
	{
		this.model=model;
		this.view=view;
		this.executor=executor;
	}
	
	/*
	 *Overridden command with needed implementation
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
		
		if(!args[1].equals("bfs")&&!args[1].equals("dfs"))
		{
			view.displayMessage("\n!!!The algorithm "+args[1]+" doesn't exist!!!\n\n");
			return;
		}
		
		Callable<Solution>callable=new SolveMazeCallable(model,args[0],args[1]);
		@SuppressWarnings("unused")
		Future<Solution>future=executor.submit(callable);
	}
}
