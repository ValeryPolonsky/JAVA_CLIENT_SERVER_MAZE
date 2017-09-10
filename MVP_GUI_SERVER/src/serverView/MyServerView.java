package serverView;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Semaphore;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import common.CommandAndData;
import serverPresenter.Properties;

public class MyServerView extends Observable implements View, Observer{

	private MyClientHandler clientHandler;
	@SuppressWarnings("unused")
	private int clientID;
	private Semaphore mutex=new Semaphore(1);


	public MyServerView() {
		this.clientHandler = null;
	}
	
	public void setClientHandler(MyClientHandler clientHandler)
	{	try {
		mutex.acquire();
		this.clientHandler=clientHandler;
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
		
	}
	
	public void start() {
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o==this){
			this.setChanged();
			this.notifyObservers(arg);
		}
	}


	@Override
	public void displayMessage(String message) {
		//System.out.println("Came to display message");
		//System.out.println("The message is: "+message);

		CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
		cad.setCommand(message);
		clientHandler.setCommandAndData(cad);
	    mutex.release();		
	}
	
	
	@Override
	public void displayMaze(Maze3d maze) {
		/*try {
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
			String command="mazeGenerated";
			CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
			cad.setCommand(command);
			cad.setObject(maze);
			oos.writeObject(cad);
		} catch (IOException e) {
			System.out.println("Cannot connect to client");			
		} */
		CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
		cad.setObject(maze);
		cad.setCommand("mazeFound");
		clientHandler.setCommandAndData(cad);
		mutex.release();
		
	}
	
	/*@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private void displayMaze(Maze3d maze,CommandAndData cad) {
		try {
			cad.setObject(maze);
			oos.writeObject(cad);
		} catch (IOException e) {
			System.out.println("Cannot connect to client");			
		}
	}*/


	@Override
	public void displayCrossSection(Maze3d maze, String axis, int index) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void displayFilesList(String[] filesList) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void displaySolution(Solution solution) {
		
		/*CommandAndData<Solution>cad=new CommandAndData<Solution>();
		cad.setObject(solution);
		cad.setCommand("solutionFound");
		System.out.println("Server: Solution of the maze is: "+cad.getObject().toString());
		clientHandler.setCommandAndData(cad);
		mutex.release();*/
		
		
		CommandAndData<String>cad=new CommandAndData<String>();
		cad.setObject(solution.toString());
		cad.setCommand("solutionFound");
		clientHandler.setCommandAndData(cad);
		mutex.release();
		
	}


	@Override
	public void setProperties(Properties dfp) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	public void setCommandAndData(CommandAndData cad,int ClientID) {
		cad.setClientID(ClientID);
		setChanged();
		notifyObservers(cad);
	}
	
	public void setClientID(int clientID)
	{
		this.clientID=clientID;
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
	
}
