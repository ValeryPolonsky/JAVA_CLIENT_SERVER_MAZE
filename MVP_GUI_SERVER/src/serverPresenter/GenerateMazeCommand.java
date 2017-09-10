package serverPresenter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import algorithms.mazeGenerators.Maze3d;
import serverModel.Model;
import serverView.View;


/**
 * GenerateMazeCommand - 
 * @author valer
 *
 */
public class GenerateMazeCommand implements Command {

	private Model model;
	private View view;
	private ExecutorService executor;
	
	public GenerateMazeCommand(Model model,View view,ExecutorService executor){
		this.model=model;
		this.view=view;
		this.executor=executor;
	}
	@Override
	public void doCommand(String[] args) {
		
		if(args.length!=4)
		{
			view.displayMessage("\n!!!Wrong number of arguments!!!\n\n");
			return;
		}
		
		if(!isInteger(args[1])||!isInteger(args[2])||!isInteger(args[3]))
		{
			view.displayMessage("\n!!!Dimensions of the maze must be integeres!!!\n\n");
			return;
		}
		
		if(model.mazeExists(args[0]))
		{
			//view.displayMessage("\n!!!The maze "+args[0]+" already exists!!!\n\n");
			//return;
			model.deleteMaze(args[0]);
		}
		

		String mazeName=args[0];
		int length=Integer.parseInt(args[1]);
		int height=Integer.parseInt(args[2]);
		int width=Integer.parseInt(args[3]);
		
		Callable<Maze3d> callable = new GenerateMazeCallable(model,mazeName,length,height,width);
	    @SuppressWarnings("unused")
		Future<Maze3d> future = executor.submit(callable);
	}
	
	
	/**
	 * Inner function - checks if the string is an integer
	 * @param s - string to check
	 * @return True or false according to check
	 */
	private boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	private boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}
