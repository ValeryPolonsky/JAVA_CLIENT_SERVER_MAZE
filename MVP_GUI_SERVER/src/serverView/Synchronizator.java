package serverView;

import algorithms.mazeGenerators.Position;


/**
 * Class needed for synchronization between MazeDisplay and MazeWindow
 * @author Valery Polonsky and Tomer Dricker
 * 
 */
public class Synchronizator {
	MazeWindow mazeWindow;
	MazeDisplay mazeDisplay;
	
	
	/**
	 * Constructor - Creates a new synchronizator
	 * @param mazeWindow - MazeWindow to set
	 * @param mazeDisplay - MazeDisplay to set
	 */
	public Synchronizator(MazeWindow mazeWindow,MazeDisplay mazeDisplay){
		this.mazeWindow=mazeWindow;
		this.mazeDisplay=mazeDisplay;
	}
	
	
	/**
	 * Constructor - Creates a new empty synchronizator
	 */
	public Synchronizator(){
		this.mazeWindow=null;
		this.mazeDisplay=null;
	}
	
	
	/**
	 * Gets current position of character from the mazeDisplay and sets it in mazeWindow
	 */
	public void setCurrentPosition()
	{
		Position position=mazeDisplay.getCurrentPosition();
		mazeWindow.setCurrentPosition(position);
	}
	
	
	/**
	 * Enables to choose perspective in mazeWindow
	 */
	public void enableChoosePerspective()
	{
		mazeWindow.setPerspective.setEnabled(true);
	}
	
	
	/**
	 * Sets maze window
	 * @param mazeWindow - MazeWindow to set
	 */
	public void setMazeWindow(MazeWindow mazeWindow)
	{
		this.mazeWindow=mazeWindow;
	}
	
	
	/**
	 * Sets mazeDisplay
	 * @param mazeDisplay - Maze Display to set
	 */
	public void setMazeDispaly(MazeDisplay mazeDisplay)
	{
		this.mazeDisplay=mazeDisplay;
	}
	
}
