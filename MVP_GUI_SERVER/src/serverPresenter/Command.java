package serverPresenter;


/**
 * Command - interface that represents a View <--> Presenter <--> Model communicator feature 
 * @author Valery Polonsky and Tomer Dricker
 *
 */

public interface Command {
	/**
	 * doCommand - function to be overridden
	 * @param args - arguments to use in command
	 */
	void doCommand(String[]args);
}
