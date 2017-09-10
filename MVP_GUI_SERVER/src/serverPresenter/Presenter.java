package serverPresenter;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.CommandAndData;
import serverModel.Model;
import serverView.MyTCPIPServer;
import serverView.View;


/**
 * Presenter - class needed for communication management between view and model
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class Presenter implements Observer {

	private Model model;
	private View view;
	private MyTCPIPServer server;
	private HashMap<String,Command> viewCommands;
	private HashMap<String,Command> modelCommands;
	private ExecutorService executor;
	
	/**
	 * Constructor - creates a new presenter
	 * @param model - model to set
	 * @param view - view to set
	 */
	public Presenter(Model model,View view,MyTCPIPServer server){
		this.model=model;
		this.view=view;
		this.server=server;
		this.executor=Executors.newFixedThreadPool(3);
		buildCommands();
	}
	
	public void setServer(MyTCPIPServer server)
	{
		this.server=server;
	}
	
	/**
	 * Function that sets all commands that neede for communication between view and model
	 */
	private void buildCommands(){
		viewCommands=new HashMap<String,Command>();
		viewCommands.put("generate3dMaze",new GenerateMazeCommand(model,view,executor));
		viewCommands.put("display", new DisplayMazeCommand(model,view));
		viewCommands.put("saveMaze", new SaveMazeCommand(model,view));
		viewCommands.put("loadMaze", new LoadMazeCommand(model,view));
		viewCommands.put("displayCrossSection", new DisplayCrossSectionCommand(model,view));
		viewCommands.put("mazeSize", new MazeSizeCommand(model,view));
		viewCommands.put("fileSize", new FileSizeCommand(model,view));
		viewCommands.put("dir", new DirectoryCommand(model,view));
		viewCommands.put("solveMaze", new SolveMazeCommand(model,view,executor));
		viewCommands.put("displaySolution", new DisplaySolutionCommand(model,view));
		viewCommands.put("exit", new ExitCommand(this,model,view));
		viewCommands.put("saveProperties", new SavePropertiesCommand(model,view));
		viewCommands.put("getSolution", new GetSolutionCommand(model,view));
		viewCommands.put("getMaze", new GetMazeCommand(model,view));
		viewCommands.put("exitClient", new ExitClientCommand(view,model,server));
		//new LoadMazesAndSolutionsCommand(model,view);
		new LoadAndSetPropertiesCommand(model,view);
		
		modelCommands=new HashMap<String,Command>();
		modelCommands.put("displayMessage",new DisplayMessageCommand(model,view));
		
	}
	
	
	/**
	 * Stops the executor
	 */
	public void shutdownExecutor()
	{
		executor.shutdown();
	}
	
	/* 
	 * Overridden function with neede implemetation
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void update(Observable o, Object arg) {
		if(o==model){
			String commandName=(String)arg;
			Command command=modelCommands.get(commandName);
			command.doCommand(null);
		}else if(o==view){
			/*String commandLine=(String)arg;
			String[]arr=commandLine.split(" ");
			String commandName=arr[0];
			String[]args=new String[arr.length-1];
			System.arraycopy(arr, 1, args, 0, arr.length-1);
			
			Command command=viewCommands.get(commandName);
			if(command==null)
			{
				view.displayMessage("\n!!!Unknown command!!!\n\n");
			}
			else
			{
				command.doCommand(args);
			}*/
			//System.out.println("Came to presenter");		
			CommandAndData cad=(CommandAndData)arg;
			//System.out.println("Command from client: "+cad.getCommand());
			//for(int i=0;i<cad.getObjectData().length;i++)
			//{
			//	System.out.println("Object data: "+cad.getObjectData()[i]);
			//}
			
			Command command=viewCommands.get(cad.getCommand());
			model.setClientID(cad.getClientID());
			command.doCommand(cad.getObjectData());
		}
	}
}
