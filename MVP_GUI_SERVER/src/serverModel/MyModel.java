package serverModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.mazeGenerators.Maze3DSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import in.MyDecompressorInputStream;
import io.MyCompressorOutputStream;
import serverPresenter.Properties;

/**
 * MyModel - implements Model interface and extends Observable class.
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class MyModel extends Observable implements Model {

	private ArrayList<Thread>threads=new ArrayList<Thread>();
	private HashMap<String,Maze3d>mazes=new HashMap<String,Maze3d>();
	private HashMap<String,Solution>solutions=new HashMap<String,Solution>();
	private String message;
	private int clientID;
	
	public MyModel(){
		
	}
	
	/**
	 * Function that generates a new maze according to given parameters
	 * @param name - maze's name to generate
	 * @param length - length of the maze
	 * @param height - height of the maze
	 * @param width - width of the maze
	 * @return - generated maze
	 */
	@Override
	public Maze3d generateMaze(String name, int length, int height, int width) {
		
		MyMaze3dGenerator mg=new MyMaze3dGenerator();
		Maze3d maze3d=mg.generate(length, height, width);
		String mazeName=name+""+clientID+"";
		mazes.put(mazeName,maze3d);
		message="mazeGenerated";
		setChanged();
		notifyObservers("displayMessage");
		return maze3d;
	}

	/**
	 * Saves the maze with a given name
	 * @param mazeName - maze to save
	 * @param fileName - file's name to save maze to
	 */
	@Override
	public void saveMaze(String mazeName, String fileName) {
		Thread thread=new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					@SuppressWarnings("resource")
					MyCompressorOutputStream cmp=new MyCompressorOutputStream(new FileOutputStream(fileName+".mz"));
					cmp.write(mazes.get(mazeName+""+clientID+"").toByteArray());
					message="mazeSaved";
					setChanged();
					notifyObservers("displayMessage");
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}) ;
		thread.setName("SAVE MAZE THREAD");
		thread.start();
		threads.add(thread);
	}

	/**Loads maze with a given name
	 * @param fileName - file's name to load 
	 * @param mazeName - new name of the loaded maze 
	 */
	@Override
	public void loadMaze(String fileName, String mazeName) {
		Thread thread=new Thread(new Runnable(){
			@Override
			public void run() {
				boolean fileLoaded=false;
				File f = new File(System.getProperty("user.dir"));
				File[] filesInDirectory = f.listFiles();
				for(int i=0;i<filesInDirectory.length;i++)
				{
					if(filesInDirectory[i].getName().equals(fileName+".mz")==true)
					{
						try {
							@SuppressWarnings("resource")
							MyDecompressorInputStream dcmp=new MyDecompressorInputStream(new FileInputStream(filesInDirectory[i]));
							int fileSize=(int)(filesInDirectory[i].length()*5);
							byte[]b=new byte[fileSize];
							dcmp.read(b);
							Maze3d maze=new Maze3d(b);
							mazes.put(mazeName, maze);
							message="mazeLoadingSucceeded";
							setChanged();
							notifyObservers("displayMessage");
							fileLoaded=true;
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}	
				}
				if(fileLoaded==false)
				{
					message="mazeLoadingFailed";
					setChanged();
					notifyObservers("displayMessage");
				}
			}
		});
		thread.setName("LOAD MAZE THREAD");
		thread.start();
		threads.add(thread);
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
	@Override
	public Maze3d getMaze(String mazeName) {
		String search  = "defaultMazeName";
		String newMazeName=mazeName;

		if ( mazeName.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
			newMazeName=newMazeName+""+clientID+"";
		}
		return mazes.get(newMazeName);
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
	@Override
	public Solution solveMaze(String mazeName, String algrorithmName) {
		String search  = "defaultMazeName";
		String newMazeName=mazeName;

		if ( mazeName.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
			newMazeName=newMazeName+""+clientID+"";
		}
		Maze3d maze=mazes.get(newMazeName);
		Solution solution=null;
		if(algrorithmName.equals("bfs")==true)
		{
			solution=testSearcher(new BFS(), new Maze3DSearchable(maze));
		}
		if(algrorithmName.equals("dfs")==true)
		{
			solution=testSearcher(new DFS(), new Maze3DSearchable(maze));
		}
		solutions.put(newMazeName, solution);
		message="mazeSolved";
		setChanged();
		notifyObservers("displayMessage");
		return solution;
	}
	
	@SuppressWarnings("rawtypes")
	private Solution testSearcher(Searcher searcher, Searchable searchable){
		 Solution solution=searcher.Search(searchable);
		 return solution;
	}

	/**
	 * Returns solution of the maze
	 * @param mazeName - maze's name
	 * @return - solution of the maze
	 */
	@Override
	public Solution getSolution(String mazeName) {
		String search  = "defaultMazeName";
		String newMazeName=mazeName;

		if ( mazeName.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
			newMazeName=newMazeName+""+clientID+"";
		}
		Solution solution=solutions.get(newMazeName);
		//if(solution!=null)
		//{
		//	message="solutionFound";
		//	setChanged();
		//	notifyObservers("displayMessage");
		//}
		return solution;
	}

	/**
	 * Returns true if the given maze exists
	 * @param mazeName - maze's name
	 * @return true or false
	 */
	@Override
	public boolean mazeExists(String mazeName) {
		String search  = "defaultMazeName";
		String newMazeName=mazeName;

		if ( mazeName.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
			newMazeName=newMazeName+""+clientID+"";
		}
		
		if(mazes.get(newMazeName)!=null)
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
		String search  = "defaultMazeName";
		String newMazeName=mazeName;

		if ( mazeName.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
			newMazeName=newMazeName+""+clientID+"";
		}
		mazes.remove(newMazeName);
		solutions.remove(newMazeName);
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
	
	public void setClientID(int clientID)
	{
		this.clientID=clientID;
	}

}
