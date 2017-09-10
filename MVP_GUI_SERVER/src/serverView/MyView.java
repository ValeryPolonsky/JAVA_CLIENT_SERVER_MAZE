package serverView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import serverPresenter.Properties;

public class MyView extends Observable implements View, Observer {

	@SuppressWarnings("unused")
	private BufferedReader in;
	private Writer out;
	private CLI cli;
	
	
	/**
	 * Constructor - Creates a new view
	 * @param in - BufferedReader to set
	 * @param out - Writer to set
	 */
	public MyView(BufferedReader in, Writer out){
		this.in=in;
		this.out=out;
		cli=new CLI(in,out);
		cli.addObserver(this);
	}
	
	
	/* 
	 * update function of observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o==cli){
			this.setChanged();
			this.notifyObservers(arg);
		}
	}

	/*
	 * Displays messages for the user
	 */
	@Override
	public void displayMessage(String message) {
		try{
			out.write(message);
			out.flush();
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	/*
	 * Starts CLI loop
	 */
	@Override
	public void start() {
		Thread thread=new Thread(new Runnable(){

			@Override
			public void run() {
				cli.start();
			}
			
		});
		thread.start();
	}
	
	/*
	 *Displays maze
	 */
	@Override
	public void displayMaze(Maze3d maze) {
		int length=maze.getLength();
		int height=maze.getHeight();
		int width=maze.getWidth();
		int mazeAsArray[][][]=maze.getMazeAs3DimArray();
		
		try {
			for(int y=0;y<height;y++)
			{
				for(int x=0;x<length;x++)
				{
					for(int z=0;z<width;z++)
					{
						out.write(Integer.toString(mazeAsArray[x][y][z]));
					}
					out.write("\n");
			    }
				out.write("\n");
				out.write("\n");
		     }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Displays cross section of the maze
	 */
	@Override
	public void displayCrossSection(Maze3d maze, String axis, int index) {
		int mazeAsArray[][][]=maze.getMazeAs3DimArray();
		
		try{
			if(axis.equals("x")==true){
				for(int y=0;y<maze.getHeight();y++)
				{
					for(int z=0;z<maze.getWidth();z++)
					{
						out.write(Integer.toString(mazeAsArray[index][y][z]));
					}
					out.write("\n");
				}
				out.write("\n");
				out.write("\n");
			}
			}catch (IOException e){
				e.printStackTrace();
			}
		
		try{
			if(axis.equals("y")==true){
				for(int x=0;x<maze.getLength();x++)
				{
					for(int z=0;z<maze.getWidth();z++)
					{
						out.write(Integer.toString(mazeAsArray[x][index][z]));
					}
					out.write("\n");
				}
				out.write("\n");
				out.write("\n");
			}
			}catch (IOException e){
				e.printStackTrace();
			}
		
		try{
			if(axis.equals("z")==true){
				for(int x=0;x<maze.getLength();x++)
				{
					for(int y=0;y<maze.getHeight();y++)
					{
						out.write(Integer.toString(mazeAsArray[x][y][index]));
					}
					out.write("\n");
				}
				out.write("\n");
				out.write("\n");
			}
			}catch (IOException e){
				e.printStackTrace();
			}
	}
	
	/*
	 * Displays file list
	 */
	@Override
	public void displayFilesList(String[] filesList) {
		try {
			out.write("\nList of files: \n\n");
			for(int i=0;i<filesList.length;i++)
			{
				out.write(filesList[i]);
				out.write("\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Displays solution of the maze
	 */
	@Override
	public void displaySolution(Solution solution) {
		try {
			out.write(solution.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Sets properties
	 */
	@Override
	public void setProperties(Properties dfp) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
}
