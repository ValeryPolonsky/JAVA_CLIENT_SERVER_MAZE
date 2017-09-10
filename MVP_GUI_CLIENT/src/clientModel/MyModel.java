package clientModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import algorithms.search.State;
import clientPresenter.Properties;
import common.CommandAndData;


/**
 * MyModel - implements Model interface and extends Observable class.
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class MyModel extends Observable implements Model {

	@SuppressWarnings("unused")
	private ArrayList<Thread>threads=new ArrayList<Thread>();
	private HashMap<String,Maze3d>mazes=new HashMap<String,Maze3d>();
	private HashMap<String,Solution>solutions=new HashMap<String,Solution>();
	private String message;
	@SuppressWarnings("unused")
	private String ip;
	@SuppressWarnings("unused")
	private int port;
	private Socket socket;
	
	public MyModel(String ip,int port){
		this.ip=ip;
		this.port=port;
		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Function that generates a new maze according to given parameters
	 * @param name - maze's name to generate
	 * @param length - length of the maze
	 * @param height - height of the maze
	 * @param width - width of the maze
	 * @return - generated maze
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public Maze3d generateMaze(String name, int length, int height, int width) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			String command="generate3dMaze";
			CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
			cad.setCommand(command);
			String[]mazeData=new String[4];
			mazeData[0]=name;
			mazeData[1]=""+length+"";
			mazeData[2]=""+height+"";
			mazeData[3]=""+width+"";
			cad.setObjectData(mazeData);
			
			byte[]bytes=compressData(cad);
			oos.writeObject(bytes);
			
			bytes=null;
			bytes=(byte[]) ois.readObject();
			String receivedCommand= ((CommandAndData)decompressData(bytes)).getCommand();
			
			System.out.println("generateMaze recieved command: "+receivedCommand);
			
			if(receivedCommand.equals("mazeGenerated")){
				Maze3d maze3d=getMaze(name);
				mazes.put(name,maze3d);
				message="\nMaze "+name+" is ready\n\n";
				setChanged();
				notifyObservers("displayMessage");
				return maze3d;
			}
			else{
				message="\n!!!Cannot generate maze "+name+" !!!\n\n";
				setChanged();
				notifyObservers("displayMessage");
				return null;
			}
			
			
		} catch (IOException e) {
			//System.out.println("generateMaze: Can not connect to server");
			message="Can not connect to server, please try again later";
			setChanged();
			notifyObservers("displayMessage");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Saves the maze with a given name
	 * @param mazeName - maze to save
	 * @param fileName - file's name to save maze to
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void saveMaze(String mazeName, String fileName) {
	try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			String command="saveMaze";
			CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
			cad.setCommand(command);
			String[]mazeData=new String[2];
			mazeData[0]=""+mazeName+"";
			mazeData[1]=""+fileName+"";
			cad.setObjectData(mazeData);
			
			byte[]bytes=compressData(cad);
			oos.writeObject(bytes);
			
			bytes=null;
			bytes=(byte[]) ois.readObject();
			String receivedCommand= ((CommandAndData)decompressData(bytes)).getCommand();
			
			if(receivedCommand.equals("mazeSavingFailed"))
			{
				message="!!!Saving of the maze failed!!!";
				setChanged();
				notifyObservers("displayMessage");
			}
			else
			{
				message="\nMaze "+mazeName+" saved\n\n";
				setChanged();
				notifyObservers("displayMessage");
			}
			
		} catch (IOException e) {
			//System.out.println("saveMaze: Cannot connect to server");	
			message="Can not connect to server, please try again later";
			setChanged();
			notifyObservers("displayMessage");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	/**Loads maze with a given name
	 * @param fileName - file's name to load 
	 * @param mazeName - new name of the loaded maze 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void loadMaze(String fileName, String mazeName) {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			String command="loadMaze";
			CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
			cad.setCommand(command);
			String[]mazeData=new String[2];
			mazeData[0]=""+fileName+"";
			mazeData[1]=""+mazeName+"";
			cad.setObjectData(mazeData);
			
			byte[]bytes=compressData(cad);
			oos.writeObject(bytes);
			
			bytes=null;
			bytes=(byte[]) ois.readObject();
			String receivedCommand= ((CommandAndData)decompressData(bytes)).getCommand();
			
			
			if(receivedCommand.equals("mazeLoadingFailed"))
			{
				message="!!!Loading of the maze failed!!!";
				setChanged();
				notifyObservers("displayMessage");
			}
			else
			{
				Maze3d maze3d = getMaze(mazeName);
				mazes.put(mazeName, maze3d);
				message="\nMaze "+mazeName+" loaded\n\n";
				setChanged();
				notifyObservers("displayMessage");
			}
			
		} catch (IOException e) {
			//System.out.println("Cannot connect to server");	
			message="Can not connect to server, please try again later";
			setChanged();
			notifyObservers("displayMessage");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Function that returns maze with a given name
	 * @param mazeName - maze to return
	 * @return - maze
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Maze3d getMaze(String mazeName) {
		/*return mazes.get(mazeName);*/
		if(mazes.get(mazeName)!=null)
		{
			return mazes.get(mazeName);
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			String command="getMaze";
			CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
			cad.setCommand(command);
			String[]mazeData=new String[1];
			mazeData[0]=""+mazeName+"";
			cad.setObjectData(mazeData);
			
			
			byte[]bytes=compressData(cad);
			oos.writeObject(bytes);
			
			
			bytes=null;
			bytes=(byte[]) ois.readObject();
			CommandAndData receivedCad=(CommandAndData)decompressData(bytes);
			String receivedCommand= receivedCad.getCommand();
			
			if(receivedCommand.equals("mazeNotFound"))
			{
				message="!!!The maze "+mazeName+" not found!!!";
				setChanged();
				notifyObservers("displayMessage");
				return null;
			}
			else
			{
				Maze3d maze3d=(Maze3d) receivedCad.getObject();
				return maze3d;
			}
			
		} catch (IOException e) {
			//System.out.println("getMaze: Cannot connect to server");	
			message="Can not connect to server, please try again later";
			setChanged();
			notifyObservers("displayMessage");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function that returns maze's size according to given name
	 * @param mazeName - maze's name
	 * @return size of the maze
	 */
	@Override
	public double getFileSize(String fileName) {
		File f = new File(System.getProperty("user.dir"));
		File[] filesInDirectory = f.listFiles();
		for(int i=0;i<filesInDirectory.length;i++)
		{
			if(filesInDirectory[i].getName().equals(fileName)==true)
			{
				return filesInDirectory[i].length();
			}	
		}
		return -1;
	}

	/**
	 * Returns file size
	 * @param fileName - file's name
	 * @return size of the file
	 */
	@Override
	public int getMazeSize(String mazeName) {
		Maze3d maze=mazes.get(mazeName);
		if(maze==null)
		{
			return -1;
		}
		else
		{
			int totalSize=maze.getHeight()*maze.getLength()*maze.getWidth()*4;
			return totalSize;
		}
	}

	/**
	 * Returns files list from the given directory
	 * @param directory - directory's name 
	 * @return - files list
	 */
	@Override
	public String[] getFilesList(String directory) {
		
		Path path = FileSystems.getDefault().getPath(directory);
		
		if (Files.exists(path)) {
			File folder = new File(directory);
			File[] listOfFiles = folder.listFiles();
			String[] filesList=new String[listOfFiles.length];
		
			int i=0;
			for (File file : listOfFiles)
			{  
				filesList[i]=file.getName();
		    i++;
		    }
			return filesList;
		}
		else
		{
			return null;
		}
	}

	/**Solves given maze with given algorithm 
	 * @param mazeName
	 * @param algrorithmName
	 * @return solution of the maze
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Solution solveMaze(String mazeName, String algrorithmName) {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			String command="solveMaze";
			CommandAndData<Solution>cad=new CommandAndData<Solution>();
			cad.setCommand(command);
			String[]mazeData=new String[2];
			mazeData[0]=""+mazeName+"";
			mazeData[1]=""+algrorithmName+"";
			cad.setObjectData(mazeData);
			
			byte[]bytes=compressData(cad);
			oos.writeObject(bytes);
			
			bytes=null;
			bytes=(byte[]) ois.readObject();
			String receivedCommand= (String)((CommandAndData)decompressData(bytes)).getCommand();
			System.out.println("solveMaze received command: "+receivedCommand);
			
			if(receivedCommand.equals("mazeSolved"))
			{
				Solution solution=getSolution(mazeName);
				solutions.put(mazeName, solution);
				message="The maze "+mazeName+" solved";
				setChanged();
				notifyObservers("displayMessage");
				return solution;
			}
			else
			{
				message="!!!Solving of the maze "+mazeName+" failed!!!";
				setChanged();
				notifyObservers("displayMessage");
				return null;
			}
			
		} catch (IOException e) {
			//System.out.println("Cannot connect to server");	
			message="Can not connect to server, please try again later";
			setChanged();
			notifyObservers("displayMessage");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * Returns solution of the maze
	 * @param mazeName - maze's name
	 * @return - solution of the maze
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Solution getSolution(String mazeName) {
		/*Solution solution=solutions.get(mazeName);
		return solution;*/
		
		
		if(solutions.get(mazeName)==null){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			String command="getSolution";
			CommandAndData<Solution>cad=new CommandAndData<Solution>();
			cad.setCommand(command);
			String[]mazeData=new String[1];
			mazeData[0]=""+mazeName+"";
			cad.setObjectData(mazeData);
			
			
			byte[]bytes=compressData(cad);
			oos.writeObject(bytes);
			
			bytes=null;
			bytes=(byte[]) ois.readObject();
			CommandAndData receivedCad=(CommandAndData)decompressData(bytes);
			String receivedCommand= (String)receivedCad.getCommand();
			System.out.println("Client: Received command: "+receivedCommand);
			if(receivedCommand.equals("solutionFound"))
			{
				String stringSolution=(String)receivedCad.getObject();
				Solution solution=stringToSolution(stringSolution);
				return solution;
			}
			else
			{
				message="!!!There is no solution for the maze "+mazeName+"!!!";
				setChanged();
				notifyObservers("displayMessage");
				return null;
			}
			
		} catch (IOException e) {
			//System.out.println("getSolution: Cannot connect to server");
			message="Can not connect to server, please try again later";
			setChanged();
			notifyObservers("displayMessage");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
		else
		{
			Solution solution=solutions.get(mazeName);
			return solution;
		}
		return null;
	}

	private Solution stringToSolution(String stringSolution) {
		Solution solution=new Solution();
		String[] positions = stringSolution.split(" ");
		
		for(int i=positions.length-1;i>1;i--)
		{
			State<String>tempState=new State<String>();
			tempState.setStateName(positions[i]);
			solution.addStateToSolution(tempState);
		}
		return solution;
	}

	/**
	 * Returns true if the given maze exists
	 * @param mazeName - maze's name
	 * @return true or false
	 */
	@Override
	public boolean mazeExists(String mazeName) {
		if(mazes.get(mazeName)!=null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Saves mazes and solutions
	 */
	public void saveMazesAndSolutions()
	{
		try {
			FileOutputStream fos = new FileOutputStream("solutions.hm");
			GZIPOutputStream gz = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(solutions);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("mazes.hm");
			GZIPOutputStream gz = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(mazes);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads mazes and solutions from files
	 */
	@SuppressWarnings("unchecked")
	public void loadMazesAndSolutions()
	{
		try {
		    FileInputStream fis = new FileInputStream("solutions.hm");
	        GZIPInputStream gs = new GZIPInputStream(fis);
	        ObjectInputStream ois = new ObjectInputStream(gs);
	        solutions = (HashMap<String, Solution>) ois.readObject();
	        ois.close();
	        fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
		    FileInputStream fis = new FileInputStream("mazes.hm");
	        GZIPInputStream gs = new GZIPInputStream(fis);
	        ObjectInputStream ois = new ObjectInputStream(gs);
	        mazes = (HashMap<String, Maze3d>) ois.readObject();
	        ois.close();
	        fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the given maze
	 * @param mazeName - mazes' name to delete
	 */
	@Override
	public void deleteMaze(String mazeName) {
		mazes.remove(mazeName);
		solutions.remove(mazeName);
	}

	/**
	 * Loads properties from the .xml file
	 * @return properties
	 */
	@Override
	public Properties loadProperties() {
		SaveAndLoadProperties salp=new SaveAndLoadProperties();
		Properties dfp=salp.loadGameProperties();
		return dfp;
	}

	/**Saves properties to the file
	 * @param algorithm - algorithm name to save
	 * @param maxMazeSize - maximum maze size
	 * @param viewSetup - CLI or GUI
	 */
	@Override
	public void saveProperties(String algorithm,String maxMazeSize, String viewSetup) {
		SaveAndLoadProperties salp=new SaveAndLoadProperties();
		salp.saveGameProperties(viewSetup , maxMazeSize,algorithm);
	}

	@Override
	public void exit() {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				@SuppressWarnings("unused")
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

				String command="exit";
				CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
				cad.setCommand(command);

				byte[]bytes=compressData(cad);
				oos.writeObject(bytes);

			} catch (IOException e) {
				System.out.println("getSolution: Cannot connect to server");			
			} 	
	}
	
	
	public byte[] compressData(Object object)
	{
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzipOut;
			gzipOut = new GZIPOutputStream(baos);
			ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
			objectOut.writeObject(object);
			objectOut.close();
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object decompressData(byte[]bytes)
	{
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			GZIPInputStream gzipIn = new GZIPInputStream(bais);
			ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
			Object object = objectIn.readObject();
			objectIn.close();
			return object;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
