package clientView;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import clientPresenter.Properties;

/**
 * @author Valery Polonsky and Tomer Dricker
 *
 */

public interface View {
	
	/**
	 * Displays messages for the user
	 * @param message - Message to display
	 */
	void displayMessage(String message);
	
	/**
	 * Displays maze for the user
	 * @param maze - Maze to display
	 */
	void displayMaze(Maze3d maze);
	
	
	/**
	 * Displays cross section of the maze 
	 * @param maze - Maze to display from
	 * @param axis - Axis to display according to
	 * @param index - Index to display according to
	 */
	void displayCrossSection(Maze3d maze, String axis, int index);
	
	
	/**
	 * Displays files list
	 * @param filesList
	 */
	void displayFilesList(String[]filesList);
	
	
	/**
	 * Displays solution of the maze
	 * @param solution - Solution to display
	 */
	void displaySolution(Solution solution);
	
	
	/**
	 * Starts view representation
	 */
	void start();
	
	
	/**
	 * Sets properties from the properties.xml file
	 * @param dfp
	 */
	void setProperties(Properties dfp);
}
