package clientPresenter;

import clientModel.Model;
import clientView.View;

/**
 * DisplayMessageCommand - gets message from the model and sends it to the view
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class DisplayMessageCommand implements Command {

	private View view;
	private Model model;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public DisplayMessageCommand(Model model,View view){
		this.model=model;
		this.view=view;
	}
	
	/*
	 * Overriden command with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		String msg=model.getMessage();
		view.displayMessage(msg);
	}
}
