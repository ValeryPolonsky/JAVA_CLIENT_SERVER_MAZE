package clientPresenter;

import java.util.concurrent.Callable;

import algorithms.mazeGenerators.Maze3d;
import clientModel.Model;


/**
 * GenerateMazeCallable - callable class needed for executor work
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class GenerateMazeCallable implements Callable<Maze3d> {

	private Model model;
	private String mazeName;
	private int length;
	private int height;
	private int width;
	
	
	/**
	 * Constructor - creates a new callable class
	 * @param model - model to set
	 * @param mazeName - maze name to set
	 * @param length - length of the maze to set
	 * @param height - height of the maze to set
	 * @param width - width of the maze to set
	 */
	public GenerateMazeCallable(Model model,String mazeName,int length,int height,int width)
	{
		this.model=model;
		this.mazeName=mazeName;
		this.length=length;
		this.height=height;
		this.width=width;
	}
	
	/*
	 * Overridden command with needed implementation
	 */
	@Override
	public Maze3d call() throws Exception {
		return model.generateMaze(mazeName, length, height, width);
	}
}
