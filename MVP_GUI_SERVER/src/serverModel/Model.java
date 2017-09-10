package serverModel;


import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import serverPresenter.Properties;


/**
 * Model - represents a data manipulation class
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public interface Model {
	
	/**
	 * Function that generates a new maze according to given parameters
	 * @param name - maze's name to generate
	 * @param length - length of the maze
	 * @param height - height of the maze
	 * @param width - width of the maze
	 * @return - generated maze
	 */
	Maze3d generateMaze(String name,int length,int height,int width);
	
	/**
	 * Saves the maze with a given name
	 * @param mazeName - maze to save
	 * @param fileName - file's name to save maze to
	 */
	void saveMaze(String mazeName,String fileName);

	/**Loads maze with a given name
	 * @param fileName - file's name to load 
	 * @param mazeName - new name of the loaded maze 
	 */
	void loadMaze(String fileName,String mazeName);
	
	/**
	 * @return message
	 */
	String getMessage();
	
	/**
	 * Function that returns maze with a given name
	 * @param mazeName - maze to return
	 * @return - maze
	 */
	Maze3d getMaze(String mazeName);
	
	/**
	 * Function that returns maze's size according to given name
	 * @param mazeName - maze's name
	 * @return size of the maze
	 */
	int getMazeSize(String mazeName);
	
	/**
	 * Returns file size
	 * @param fileName - file's name
	 * @return size of the file
	 */
	double getFileSize(String fileName);
	
	
	/**
	 * Returns files list from the given directory
	 * @param directory - directory's name 
	 * @return - files list
	 */
	String[] getFilesList(String directory);
	
	/**Solves given maze with given algorithm 
	 * @param mazeName
	 * @param algrorithmName
	 * @return solution of the maze
	 */
	Solution solveMaze(String mazeName,String algrorithmName);
	
	/**
	 * Returns solution of the maze
	 * @param mazeName - maze's name
	 * @return - solution of the maze
	 */
	Solution getSolution(String mazeName);
	
	/**
	 * Returns true if the given maze exists
	 * @param mazeName - maze's name
	 * @return true or false
	 */
	boolean mazeExists(String mazeName);
	
	/**
	 * Loads mazes and solutions from files
	 */
	public void loadMazesAndSolutions();
	
	/**
	 * Saves mazes and solutions
	 */
	public void saveMazesAndSolutions();
	
	/**
	 * Deletes the given maze
	 * @param mazeName - mazes' name to delete
	 */
	public void deleteMaze(String mazeName);
	
	/**
	 * Loads properties from the .xml file
	 * @return properties
	 */
	Properties loadProperties();
	
	/**Saves properties to the file
	 * @param algorithm - algorithm name to save
	 * @param maxMazeSize - maximum maze size
	 * @param viewSetup - CLI or GUI
	 */
	void saveProperties(String algorithm, String maxMazeSize,String viewSetup);
	
	public void setClientID(int clientID);
	
}
