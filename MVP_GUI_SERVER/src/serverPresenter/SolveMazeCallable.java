package serverPresenter;

import java.util.concurrent.Callable;

import algorithms.search.Solution;
import serverModel.Model;

/**
 * SolveMazeColable - callable class needed for executor work
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class SolveMazeCallable implements Callable<Solution> {

	private Model model;
	private String mazeName;
	private String algorithmName;
	
	/**
	 * Constructor - creates a new callable appearance
	 * @param model - model to set
	 * @param mazeName - maze name to set
	 * @param algorithmName - algorithm's name
	 */
	public SolveMazeCallable(Model model,String mazeName,String algorithmName)
	{
		this.model=model;
		this.mazeName=mazeName;
		this.algorithmName=algorithmName;
	}
	
	/*
	 * Overriden command with needed implementation
	 */
	@Override
	public Solution call() throws Exception {
		return model.solveMaze(mazeName, algorithmName);
	}

}
