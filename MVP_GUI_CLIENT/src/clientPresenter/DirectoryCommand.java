package clientPresenter;

import clientModel.Model;
import clientView.View;


/**
 * DirectoryCoommand - gets all files in the given directory and sends them to the view
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class DirectoryCommand implements Command {

	Model model;
	View view;
	
	
	/**
	 * Constructor - creates a new command with given model and view
	 * @param model - model to set
	 * @param view - view to set
	 */
	public DirectoryCommand(Model model,View view){
		this.view=view;
		this.model=model;
	}
	
	
	/*
	 * Overridden command with needed functionality
	 */
	@Override
	public void doCommand(String[] args) {
		String path="";
		path=args[0];
		for(int i=1;i<args.length;i++)
		{
			path=path+" "+args[i];
		}
		String[]filesList=model.getFilesList(path);
		if(filesList!=null)
			view.displayFilesList(filesList);
		else
			view.displayMessage("!!!There is no such directory!!!\n");
	}
}
