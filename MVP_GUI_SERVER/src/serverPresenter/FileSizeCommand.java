package serverPresenter;

import serverModel.Model;
import serverView.View;


/**FileSizeCommand - gets file's size from the model and sends it to the view
 * @author Valery Polosnky and Tomer Dricker
 *
 */
public class FileSizeCommand implements Command {

	Model model;
	View view;
	
	/**
	 * Constructor - creates a new command
	 * @param model - model to set
	 * @param view - view to set
	 */
	public FileSizeCommand(Model model,View view){
		this.view=view;
		this.model=model;
	}
	
	/*
	 * Overridden command with needed implementation
	 */
	@Override
	public void doCommand(String[] args) {
		
		if(args.length!=1)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		double fileSize=model.getFileSize(args[0]);
		if(fileSize==-1)
		{
			view.displayMessage("\n!!!The file "+args[0]+" doesn't exist!!!\n\n");
			return;
		}
		
		String message="\nThe size of file "+args[0]+" is "+fileSize+" bytes\n\n";
		view.displayMessage(message);
	}
}
