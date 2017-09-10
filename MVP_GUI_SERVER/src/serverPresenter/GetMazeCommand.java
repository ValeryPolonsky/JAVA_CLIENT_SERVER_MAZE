package serverPresenter;

import algorithms.mazeGenerators.Maze3d;
import serverModel.Model;
import serverView.View;

public class GetMazeCommand implements Command {
	private Model model;
	private View view;
	
	/**
	 * Constructor - creates a new command 
	 * @param model - model to set
	 * @param view - view to set
	 */
	public GetMazeCommand(Model model,View view){
		this.view=view;
		this.model=model;
	}
	
	/*
	 * Overridden function with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		Maze3d maze3d=model.getMaze(args[0]);
		view.displayMaze(maze3d);
	}

}
