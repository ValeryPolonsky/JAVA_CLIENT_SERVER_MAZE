package clientPresenter;

import clientModel.Model;
import clientView.View;

/**
 * ExitCommand - stops all working processes
 * @author Vaelry Polonsky and Tomer Dricker
 *
 */
public class ExitCommand implements Command {

	Presenter presenter;
	Model model;
	View view;
	
	
	/**
	 * Constructor - creates a new command
	 * @param presenter
	 * @param model
	 * @param view
	 */
	public ExitCommand(Presenter presenter,Model model,View view)
	{
		this.presenter=presenter;
		this.model=model;
		this.view=view;
	}
	
	/* 
	 * Overridden command with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		model.exit();
		//new SaveMazesAndSolutionsCommand(model,view);
		presenter.shutdownExecutor();
	}

}
