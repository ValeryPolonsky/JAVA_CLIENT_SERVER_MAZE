package clientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Observable;


/**
 * CLI class - class needed for user <--> program interaction
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class CLI extends Observable {
	
	private BufferedReader in;
	private Writer out;
	
	public CLI(BufferedReader in,Writer out){
		this.in=in;
		this.out=out;
	}
	
	public void start(){
		Thread thread=new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					printCommands();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try{
					String line=null;
					do{
						out.write(">>>> ");
						out.flush();
						line=in.readLine();
						setChanged();
						notifyObservers(line);
					}while(!(line.equals("exit")));
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
				
			}
			
		});
		thread.setName("CLI THREAD");
		thread.start();
	}
	public void printCommands() throws IOException{
		out.write("List of all possible Commands:\n");
		out.write("generate3dMaze\n");
		out.write("display\n");
		out.write("saveMaze\n");
		out.write("loadMaze\n");
		out.write("displayCrossSection\n");
		out.write("mazeSize\n");
		out.write("fileSize\n");
		out.write("dir\n");
		out.write("solve\n");
		out.write("displaySolution\n");
		out.write("saveProperties <algorithm , maxMazeSize, CLI/GUI>");
		out.write("exit\n");
	}
}
