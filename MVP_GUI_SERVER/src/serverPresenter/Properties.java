package serverPresenter;


/**
 * Properties - class that takes all loaded properties data
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class Properties{
	
	private String defaultAlgorithm;
	private String maxMazeSize;
	private String viewSetup;
	
	/**
	 * Default constructor - creates a new Properties appearance
	 */
	public Properties()
	{
		 defaultAlgorithm=null;
		 maxMazeSize=null;
		 viewSetup=null;
	}
	
	/**
	 * @return returns default algorithm name
	 */
	public String getDefaultAlgorithm() {
		return defaultAlgorithm;
	}
	
	/**
	 * Sets a given default algorithm to this one
	 * @param defaultAlgorithm - algorithm to set
	 */
	public void setDefaultAlgorithm(String defaultAlgorithm) {
		this.defaultAlgorithm = defaultAlgorithm;
	}

	/**
	 * @return maximum size of the maze
	 */
	public String getMaxMazeSize() {
		return maxMazeSize;
	}

	/**
	 * Sets maximum size of the maze
	 * @param maxMazeSize - size to set
	 */
	public void setMaxMazeSize(String maxMazeSize) {
		this.maxMazeSize = maxMazeSize;
	}

	/**
	 * @return CLI or GUI
	 */
	public String getViewSetup() {
		return viewSetup;
	}

	/**
	 * Sets view setup
	 * @param viewSetup - view to set (CLI or GUI)
	 */
	public void setViewSetup(String viewSetup) {
		this.viewSetup = viewSetup;
	}
}
