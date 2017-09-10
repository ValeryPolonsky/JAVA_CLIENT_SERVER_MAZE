package serverView;



import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;


/**
 * MazeDisplay - class that represents game board for the user
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class MazeDisplay extends Canvas{

	
	int[][]mazeData;
	int[][][]mazeAs3DimArray;
	Synchronizator synchronizator;
	GameCharacter gameCharacter;
	Timer timer;
	TimerTask timerTask;
	Solution solution;
	boolean firstPath;
	Position previous;
	Position currentPosition=new Position(1,1,1);
	String perspective="";
	String level;
	Position exit;
	boolean gameStarted;
	boolean displayingSolution=false;
	

	
	/**
	 * Constructor - Creates a new game board
	 * @param parent - Represents a parent shell 
	 * @param style - Represents SWT style (BORDER/NONE/...)
	 * @param synchronizator - Synchronizator class for synchronization between MazeDisplay and MazeWindow
	 */
	public MazeDisplay(Composite parent,int style,Synchronizator synchronizator) {
		super(parent,style);
		this.synchronizator=synchronizator;
		this.synchronizator.setMazeDispaly(this);
		level="1";
		gameCharacter=new GameCharacter(0,0,0);
		setBackground(new Color(null,255,255,255));
		currentPosition=new Position(0,1,1);
		gameStarted=false;
		exit=new Position();
		Image wall=new Image(getDisplay(),"resources/wall.jpg");
		Image floor=new Image(getDisplay(),"resources/floor.jpg");
		
		//Paint listener - responds if window size changes
		addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				if(gameStarted)
				{									
				int width=getSize().x;
				int height=getSize().y;
				
				int w=width/mazeData[0].length;
				int h=height/mazeData.length;
				
				//Draws maze cross section according to mazeData
				for(int i=0;i<mazeData.length;i++)
				{
					for(int j=0;j<mazeData[i].length;j++)
					{
						int x=j*w;
						int y=i*h;
						if(mazeData[i][j]!=0)
						{
							e.gc.drawImage(wall, 0, 0, wall.getBounds().width,wall.getBounds().height,x,y,w,h);
						}
						else
						{
							e.gc.drawImage(floor, 0, 0, wall.getBounds().width,wall.getBounds().height,x,y,w,h);
						}
					}
				}
					
				//Sets character position according to given perspective								
				    if(perspective.equals("y")){
				    	gameCharacter.paint(e, w, h);
				    	gameCharacter.y=currentPosition.getX()*h;
				    	gameCharacter.x=currentPosition.getZ()*w;
				    }
				    
				    if(perspective.equals("x")){
				    	gameCharacter.paint(e, w, h);
					    gameCharacter.y=currentPosition.getY()*h;
						gameCharacter.x=currentPosition.getZ()*w;
					}
				    
				    if(perspective.equals("z")){
				    	gameCharacter.paint(e, w, h);
					    gameCharacter.y=currentPosition.getX()*h;
						gameCharacter.x=currentPosition.getY()*w;
					}
				}
				
				
			}
			
		});
	}
	
	
	/**
	 * Sets flag of the game to true value
	 */
	public void startGame()
	{
		gameStarted=true;
	}
	
	
	/**
	 * @return Returns boolean value that represents the status of the game
	 */
	public boolean ifGameStarted()
	{
		return gameStarted;
	}
	
	
	/**
	 * setMazeData - sets mazeData
	 * @param mazeData - data to set
	 */
	public void setMazeData(int[][]mazeData)
	{
		this.mazeData=mazeData;
		
		int width=getSize().x;
		int height=getSize().y;
		
		int w=width/mazeData[0].length;
		int h=height/mazeData.length;
		
	    if(perspective.equals("y")){
	    	gameCharacter.y=currentPosition.getX()*h;
	    	gameCharacter.x=currentPosition.getZ()*w;
	    }
	    
	    if(perspective.equals("x")){
		    gameCharacter.y=currentPosition.getY()*h;
			gameCharacter.x=currentPosition.getZ()*w;
		}
	    
	    if(perspective.equals("z")){
		    gameCharacter.y=currentPosition.getX()*h;
			gameCharacter.x=currentPosition.getY()*w;
		}
	    
		redraw();
	}
	
	
	/**
	 * setMaze - Sets the whole maze
	 * @param mazeAs3dimArray - maze to set
	 */
	public void setMaze(int[][][]mazeAs3dimArray)
	{
		this.mazeAs3DimArray=mazeAs3dimArray;
	}
	
	
	/**
	 * setSolution - sets the given solution to this solution
	 * @param solution - solution to set
	 */
	@SuppressWarnings("rawtypes")
	public void setSolution(Solution solution)
	{
		this.solution=solution;
		@SuppressWarnings("unused")
		Stack<State>solutionPathes=solution.getSolution();				
	}
	
	
	
	/**
	 * levelChanged - by comparing to positions decides if the level changed according to Y perspective
	 * @param previous - Previous position
	 * @param current - Current position
	 * @return - True or False according to decision
	 */
	public boolean levelChanged(Position previous,Position current)
	{
		if(previous.getY()!=current.getY())
			return true;
		else
			return false;
	}
	
	
	/**
	 * setNewLevelData - sets mazeData array according to given position and perspective
	 * @param position - position to set data according to
	 */
	public void setNewLevelData(Position position)
	{
		if(perspective.equals("y")){
		int y=position.getY();
		if(y>-1&&y<mazeAs3DimArray[0].length)
		{
			for(int x=0;x<mazeAs3DimArray.length;x++)
			{
				for(int z=0;z<mazeAs3DimArray[0][0].length;z++)
				{
					mazeData[x][z]=mazeAs3DimArray[x][y][z];
				}
			}
		}
		}
		

		if(perspective.equals("x")){
		int x=position.getX();
		if(x>-1&&x<mazeAs3DimArray.length)
		{
			for(int y=0;y<mazeAs3DimArray[0].length;y++)
			{
				for(int z=0;z<mazeAs3DimArray[0][0].length;z++)
				{
					mazeData[y][z]=mazeAs3DimArray[x][y][z];
				}
			}
		}
		}
		
		if(perspective.equals("z")){
			int z=position.getZ();
			if(z>-1&&z<mazeAs3DimArray[0][0].length)
			{
				for(int x=0;x<mazeAs3DimArray.length;x++)
				{
					for(int y=0;y<mazeAs3DimArray[0].length;y++)
					{
						mazeData[x][y]=mazeAs3DimArray[x][y][z];
					}
				}
			}
		}
	}
	
	
	/**
	 * Starts displaying solution of the maze
	 */
	@SuppressWarnings("rawtypes")
	public void start()
	{
		
		displayingSolution=true;
		Stack<State>solutionPathes=solution.getSolution();
		setPerspective("y");
		setNewLevelData((new Position()).toPosition(solutionPathes.peek().getStateName().toString()));
		int startPositionX=((new Position()).toPosition(solutionPathes.peek().getStateName().toString())).getX();
		int startPositionY=((new Position()).toPosition(solutionPathes.peek().getStateName().toString())).getZ();
		previous=new Position();
		firstPath=true;
		timer=new Timer();
		timerTask=new TimerTask(){

			@Override
			public void run() {
				getDisplay().syncExec(new Runnable(){

					
					@Override
					public void run() {
						
						int width=getSize().x;
						int height=getSize().y;
						
						int w=width/mazeData[0].length+startPositionX/mazeData[0].length;
						int h=height/mazeData.length+startPositionY/mazeData.length;
						
						
						Position current=new Position();
						if(!solutionPathes.isEmpty())
						{
							if(!firstPath)
							{
								if(!levelChanged(previous,current))
								{
									previous=previous.toPosition(solutionPathes.peek().getStateName().toString());
									current=current.toPosition(solutionPathes.pop().getStateName().toString());
									gameCharacter.y=current.getX()*h;
									gameCharacter.x=current.getZ()*w;
									setCurrentPosition(current);
									synchronizator.setCurrentPosition();
									redraw();
								}
								else
								{
									previous=previous.toPosition(solutionPathes.peek().getStateName().toString());
									current=current.toPosition(solutionPathes.pop().getStateName().toString());
									setNewLevelData(current);
									gameCharacter.y=current.getX()*h;
									gameCharacter.x=current.getZ()*w;
									setCurrentPosition(current);
									synchronizator.setCurrentPosition();
									redraw();
								}
							}
							else
							{
								
								previous=previous.toPosition(solutionPathes.peek().getStateName().toString());
								current=current.toPosition(solutionPathes.pop().getStateName().toString());
								gameCharacter.y=current.getX()*h;
								gameCharacter.x=current.getZ()*w;
								setCurrentPosition(current);
								synchronizator.setCurrentPosition();
								firstPath=false;
								redraw();
							}
						}
						else
						{
							stop();
							synchronizator.enableChoosePerspective();
						}	
					}
				});
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 500);
	}
	
	
	/**
	 * Stops displaying solution
	 */
	public void stop()
	{
		if(displayingSolution)
		{
		  displayingSolution=false;
		  timer.cancel();
		  timerTask.cancel();
		}
	}

	
	/**
	 * Moves left the game character
	 */
	public void moveLeft() {
		int width=getSize().x;				
		int w=width/mazeData[0].length;		
		if(perspective.equals("y"))
		{
	    if(mazeAs3DimArray[currentPosition.getX()][currentPosition.getY()][currentPosition.getZ()-1]==0)
	    {
	    	gameCharacter.x=(currentPosition.getZ()-1)*w;
	    	currentPosition.setZ(currentPosition.getZ()-1);
	    	redraw();
	    }
		}
		
		if(perspective.equals("x"))
		{
	    if(mazeData[currentPosition.getY()][currentPosition.getZ()-1]==0)
	    {
	    	gameCharacter.x=(currentPosition.getZ()-1)*w;
	    	currentPosition.setZ(currentPosition.getZ()-1);
	    	redraw();
	    }
		}
		
		if(perspective.equals("z"))
		{
	    if(mazeData[currentPosition.getX()][currentPosition.getY()-1]==0)
	    {
	    	gameCharacter.x=(currentPosition.getY()-1)*w;
	    	currentPosition.setY(currentPosition.getY()-1);
	    	redraw();
	    }
		}										
	}

	
	
	/**
	 * Moves right the game character
	 */
	public void moveRight() {
		int width=getSize().x;				
		int w=width/mazeData[0].length;
		
		if(perspective.equals("y"))
		{
		if(mazeAs3DimArray[currentPosition.getX()][currentPosition.getY()][currentPosition.getZ()+1]==0)
		{
			gameCharacter.x=(currentPosition.getZ()+1)*w;
			currentPosition.setZ(currentPosition.getZ()+1);
			redraw();
		}
		}
		
		if(perspective.equals("x"))
		{
		if(mazeData[currentPosition.getY()][currentPosition.getZ()+1]==0)
		{
			gameCharacter.x=(currentPosition.getZ()+1)*w;
			currentPosition.setZ(currentPosition.getZ()+1);
			redraw();
		}
		}
		
		if(perspective.equals("z"))
		{
		if(mazeData[currentPosition.getX()][currentPosition.getY()+1]==0)
		{
			gameCharacter.x=(currentPosition.getY()+1)*w;
			currentPosition.setY(currentPosition.getY()+1);
			redraw();
		}
		}				
	}

	
	/**
	 * Moves down the game character
	 */
	public void moveDown() {		
		int height=getSize().y;				
		int h=height/mazeData.length;
				
		if(perspective.equals("y"))
		{
		if(currentPosition.getX()<mazeAs3DimArray.length-1){
		if(mazeAs3DimArray[currentPosition.getX()+1][currentPosition.getY()][currentPosition.getZ()]==0)
		{
			gameCharacter.y=(currentPosition.getX()+1)*h;
			currentPosition.setX(currentPosition.getX()+1);
			redraw();
			
		}
		}
		}
		
		if(perspective.equals("x")){
	    if(currentPosition.getY()<mazeAs3DimArray[0].length-1){
		if(mazeData[currentPosition.getY()+1][currentPosition.getZ()]==0){
			
			gameCharacter.y=(currentPosition.getY()+1)*h;
			currentPosition.setY(currentPosition.getY()+1);
			redraw();
		}}}
		
		if(perspective.equals("z")){
	    if(currentPosition.getX()<mazeAs3DimArray.length-1){
		if(mazeData[currentPosition.getX()+1][currentPosition.getY()]==0){
				
			gameCharacter.y=(currentPosition.getX()+1)*h;
			currentPosition.setX(currentPosition.getX()+1);
			redraw();
		}}}	
	}

	
	
	/**
	 * Moves up the game character
	 */
	public void moveUp() {		
		int height=getSize().y;
		int h=height/mazeData.length;
				
		if(perspective.equals("y"))
		{
		if(currentPosition.getX()>0){
		if(mazeAs3DimArray[currentPosition.getX()-1][currentPosition.getY()][currentPosition.getZ()]==0)
		{
			gameCharacter.y=(currentPosition.getX()-1)*h;
			currentPosition.setX(currentPosition.getX()-1);
			redraw();
		}
		}
		}
		
		if(perspective.equals("x"))
		{
		if(currentPosition.getY()>0){
		if(mazeData[currentPosition.getY()-1][currentPosition.getZ()]==0)
		{
			gameCharacter.y=(currentPosition.getY()-1)*h;
			currentPosition.setY(currentPosition.getY()-1);
			redraw();
		}
		}
		}
		
		if(perspective.equals("z"))
		{
		if(currentPosition.getX()>0){
		if(mazeData[currentPosition.getX()-1][currentPosition.getY()]==0)
		{
			gameCharacter.y=(currentPosition.getX()-1)*h;
			currentPosition.setX(currentPosition.getX()-1);
			redraw();
		}
		}
		}
		
		
		
	}

	
	/**
	 * Moves the character one level up
	 */
	public void moveLevelUp() {
		if(perspective.equals("y"))
		{
		if(currentPosition.getY()<mazeAs3DimArray[0].length-1){
		if(mazeAs3DimArray[currentPosition.getX()][currentPosition.getY()+1][currentPosition.getZ()]==0)
		{
			currentPosition.setY(currentPosition.getY()+1);
			setNewLevelData(currentPosition);
			setLevel(""+currentPosition.getY()+"");
		}
		}
		}
		
		if(perspective.equals("x"))
		{
		if(currentPosition.getX()<mazeAs3DimArray.length-1){
		if(mazeAs3DimArray[currentPosition.getX()+1][currentPosition.getY()][currentPosition.getZ()]==0)
		{
			currentPosition.setX(currentPosition.getX()+1);
			setNewLevelData(currentPosition);
			setLevel(""+currentPosition.getY()+"");
		}
		}
		}
		
		if(perspective.equals("z"))
		{
		if(currentPosition.getX()<mazeAs3DimArray[0][0].length-1){
		if(mazeAs3DimArray[currentPosition.getX()][currentPosition.getY()][currentPosition.getZ()+1]==0)
		{
			currentPosition.setZ(currentPosition.getZ()+1);
			setNewLevelData(currentPosition);
			setLevel(""+currentPosition.getY()+"");
		}
		}
		}
		redraw();
	}

	
	
	/**
	 * Moves the character one level down 
	 */
	public void moveLevelDown() {
		if(perspective.equals("y"))
		{
		if(currentPosition.getY()>0){
		if(mazeAs3DimArray[currentPosition.getX()][currentPosition.getY()-1][currentPosition.getZ()]==0)
		{
			currentPosition.setY(currentPosition.getY()-1);
			setNewLevelData(currentPosition);
			setLevel(""+currentPosition.getY()+"");
		}
		}
		}
		
		if(perspective.equals("x"))
		{
		if(currentPosition.getX()>0){
		if(mazeAs3DimArray[currentPosition.getX()-1][currentPosition.getY()][currentPosition.getZ()]==0)
		{
			currentPosition.setX(currentPosition.getX()-1);
			setNewLevelData(currentPosition);
			setLevel(""+currentPosition.getX()+"");
		}
		}
		}
		
		if(perspective.equals("z"))
		{
		if(currentPosition.getZ()>0){
		if(mazeAs3DimArray[currentPosition.getX()][currentPosition.getY()][currentPosition.getZ()-1]==0)
		{
			currentPosition.setZ(currentPosition.getZ()-1);
			setNewLevelData(currentPosition);
			setLevel(""+currentPosition.getZ()+"");
		}
		}
		}
		redraw();
	}
	
	
	/**
	 * setPerspective - sets the perspective of the game to the given one
	 * @param perspective - perspective to set
	 */
	public void setPerspective(String perspective)
	{
		this.perspective=perspective;
	}
	
	
	

	/*public boolean isPerspectiveY()
	{
		if(perspective.equals("y"))
			return true;
		else
			return false;
	}*/
	
	
	
	
	/**
	 * setLevel - sets the level of the game character according to perspective
	 * @param level - level to set
	 */
	public void setLevel(String level)
	{
		this.level=level;
	}
	
	
	/**
	 * getLevel - returns current level of the character
	 * @return - level
	 */
	public String getLevel()
	{
		return level;
	}

	
	/**
	 * setExit - sets exit position  
	 * @param exit - position to set
	 */
	public void setExit(Position exit)
	{
		this.exit=exit;
	}
	
	
	/**
	 * getExit - returns exit position
	 * @return - exit position
	 */
	public Position getExit()
	{
		return exit;
	}
	
	
	/**
	 * setCurrentPosition - sets current position of the character
	 * @param position - position to set
	 */
	public void setCurrentPosition(Position position)
	{
		currentPosition=position;
	}
	
	
	/**
	 * getCurrentPosition - returns current position of the game character
	 * @return - current position
	 */
	public Position getCurrentPosition()
	{
		return currentPosition;
	}
	
	
	/**
	 * cameToExit - checks if the game character came to exit
	 * @return - true or false according to decision
	 */
	public boolean cameToExit() {
		if(currentPosition.toString().equals(exit.toString()))
			return true;
		else
			return false;
	}

	
	/**
	 * Sets gameStarted flag to false
	 */
	public void stopGame() {
		
		gameStarted=false;
	}
	
	
	/**
	 * @return true if solution is being displaying right now, false otherwise
	 */
	public boolean isDisplayingSolution()
	{
		return displayingSolution;
	}
	
	
}
