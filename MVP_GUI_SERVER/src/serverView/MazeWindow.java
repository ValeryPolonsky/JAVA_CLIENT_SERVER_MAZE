package serverView;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import serverPresenter.Properties;


/**
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class MazeWindow extends BasicWindow implements View{

	String[]indexes={};
	String[]perspectives={"x","y","z"};
	String axis="";
	String mazeName="defaultMazeName";
	String tempLevel="";
	String []lengths={};
	String []heights={};
	String []widths={};
	String []algorithms={"bfs","dfs"};
	String []views={"CLI","GUI"};
	MazeDisplay mazeDisplay;
	String choosenAlgorithm="";
	String choosenView="";
	String choosenMaxSize="";
	String setLevel="1";
	boolean mazeIsSolved=false;
	String choosenPerspective="";
	boolean confirmationExit;
	boolean closeNow=false;
	boolean gameCanBeClosed=true;
	Label labelDisplayPosition;
	Synchronizator synchronizator;
	Combo setPerspective;
	Combo setHeight;
	Combo setWidth;
	Combo setLength;
	Button btnSolveMaze;
	Button btnDisplaySolution;
	Button btnGenerateMaze;
	Button btnGetHint;

	
	
	Menu menuBar, fileMenu, helpMenu;

	MenuItem fileMenuHeader, helpMenuHeader;

    MenuItem fileExitItem, filePropertiesItem, fileSaveMazeItem, fileLoadMazeItem, helpGetHelpItem;
    
    ArrayList<Thread> threads=new ArrayList<Thread>();

	
	/**Constructor - creates a new maze window
	 * @param title - Name of the window
	 * @param width - Starting width of the window
	 * @param height - Starting height of the window
	 * @param synchronizator - Class for synchronization between MazeWindow and MazeDisplay classes
	 */
	public MazeWindow(String title, int width, int height,Synchronizator synchronizator) {
		super(title, width, height);
		closeNow=false;
		this.synchronizator=synchronizator;
		this.synchronizator.setMazeWindow(this);
	}

	/*
	 * Initializes all widgets in the GUI
	 */
	@Override
	void initWidgets() {
		
		//Creates a new grid layout with 3 columns
		GridLayout gridLayout = new GridLayout(3, false);
		shell.setLayout(gridLayout);
		
		
		//Creates label of perspective settings
		Label labelPerspective = new Label(shell, SWT.NONE);
		labelPerspective.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelPerspective.setText("Perspective: ");
		
		setPerspective = new Combo(shell, SWT.BORDER|SWT.READ_ONLY);
		setPerspective.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		setPerspective.setItems(perspectives);
		GridData gd = new GridData ();
		gd.widthHint = 20;
		setPerspective.setLayoutData (gd);
		
		
		//Sets maze display data
		mazeDisplay=new MazeDisplay(shell,SWT.BORDER,synchronizator);
		mazeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 10));
		

		//Creates maze's length settings 
		Label labelLength = new Label(shell, SWT.NONE);
		labelLength.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelLength.setText("Length: ");
		
		setLength = new Combo(shell, SWT.BORDER|SWT.READ_ONLY);
		setLength.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		setLength.setItems(lengths);
		gd = new GridData ();
		gd.widthHint = 20;
		setLength.setLayoutData (gd);
		
		
		//Creates maze's height settings 
		Label labelHeight= new Label(shell, SWT.NONE);
		labelHeight.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelHeight.setText("Height: ");
		
		setHeight = new Combo(shell, SWT.BORDER|SWT.READ_ONLY);
		setHeight.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		setHeight.setItems(lengths);
		gd = new GridData ();
		gd.widthHint = 20;
		setHeight.setLayoutData (gd);
		
		
		//Creates maze's width settings 
		Label labelWidth = new Label(shell, SWT.NONE);
		labelWidth.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelWidth.setText("Width: ");
		
		setWidth = new Combo(shell, SWT.BORDER|SWT.READ_ONLY);
		setWidth.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		setWidth.setItems(lengths);
		gd = new GridData ();
		gd.widthHint = 20;
		setWidth.setLayoutData (gd);
		
		
		//Creates character position settings 
		Label labelPosition = new Label(shell, SWT.NONE);
		labelPosition.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelPosition.setText("Position: ");
		
		labelDisplayPosition = new Label(shell, SWT.BORDER);
		labelDisplayPosition.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelDisplayPosition.setText("{0,0,0}  ");
		gd = new GridData ();
		gd.widthHint = 55;
		gd.heightHint=20;
		FontData[] fD = labelDisplayPosition.getFont().getFontData();
		fD[0].setHeight(10);
		labelDisplayPosition.setFont( new Font(display,fD[0]));
		labelDisplayPosition.setLayoutData (gd);
		
		
		
		//Creates button for maze generation
		btnGenerateMaze=new Button(shell, SWT.NONE);
		btnGenerateMaze.setText(" Generate maze ");
		btnGenerateMaze.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty = new Label(shell, SWT.NONE);
		labelEmpty.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty.setText("");
		
		
		//Creates button for solving maze
		btnSolveMaze=new Button(shell, SWT.NONE);
		btnSolveMaze.setText("    Solve maze    ");
		btnSolveMaze.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty1 = new Label(shell, SWT.NONE);
		labelEmpty1.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty1.setText("");
		
		
		
		//Creates button for hint option
		btnGetHint=new Button(shell, SWT.NONE);
		btnGetHint.setText("       Get hint       ");
		btnGetHint.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty2 = new Label(shell, SWT.NONE);
		labelEmpty2.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty2.setText("");
		
		
		
		//Creates button for displaying solution
		btnDisplaySolution=new Button(shell, SWT.NONE);
		btnDisplaySolution.setText("Display solution");
		btnDisplaySolution.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty3 = new Label(shell, SWT.NONE);
		labelEmpty3.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty3.setText("");
		
		
		//Creates button to start a new game
		Button btnStartNewGame=new Button(shell, SWT.NONE);
		btnStartNewGame.setText("Start new game ");
		btnStartNewGame.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty4 = new Label(shell, SWT.NONE);
		labelEmpty4.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty4.setText("");
		
		
		//Creates menu bar
		menuBar = new Menu(shell, SWT.BAR);
	    fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");

	    fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);

	    filePropertiesItem = new MenuItem(fileMenu, SWT.PUSH);
	    filePropertiesItem.setText("&Properties");
	    
	    fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("&Exit");
	    
	    fileSaveMazeItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveMazeItem.setText("&Save maze");
	    
	    fileLoadMazeItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileLoadMazeItem.setText("&Load maze");

	    //Sets menu bar
	    shell.setMenuBar(menuBar);
	    
	    
	    
	    //Shell listener - starts when user tries to close window
	    shell.addListener(SWT.Close, new Listener()
	    {
	    	@Override
	        public void handleEvent(Event event)
	        {
	            int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
	            MessageBox messageBox = new MessageBox(shell, style);
	            messageBox.setText("Confirmation");
	            messageBox.setMessage("        Are you sure you want to quit?");
	            if(messageBox.open() == SWT.YES)
	            {
	            	if(gameCanBeClosed)
	            	{
	            	mazeDisplay.stop();
	            	String command="exit";
	            	setChanged();
	            	notifyObservers(command);
	            	stopThreads();
	            	event.doit=true;
	            	closeNow=true;
	            	}
	            	else
	            	{
	            		displayMessage("Close all message boxes");
	            		event.doit=false;
	            	}
	            }
	            else
	            {  	
	            	event.doit=false;
	            }
	            
	        }
	    });
	    
	    
	    //Listener for Exit command in the menu bar
	    fileExitItem.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		shell.close();
		   }
	    });
	    
	    //Listener for Properties command in the menu bar
	    filePropertiesItem.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		
	    		Thread thread=new Thread(new Runnable(){ 
	    		    public void run() {
	    		    	//Creates a new shell and display for menu
	    		    	Display display1 = new Display();
	    			    Shell shell1 = new Shell(display1);

	    			    //Sets new grid layout with 2 columns    			    
	    			    GridLayout gridLayout = new GridLayout(2, false);
	    				shell1.setLayout(gridLayout);
	    				
	    				//Creates algorithms to choose settings
	    				Label labelAlgorithms = new Label(shell1, SWT.NONE);
	    				labelAlgorithms.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				labelAlgorithms.setText("Algorithm: ");
	    				
	    				Combo setAlgorithm = new Combo(shell1, SWT.BORDER|SWT.READ_ONLY);
	    		        setAlgorithm.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				setAlgorithm.setItems(algorithms);
	    				setAlgorithm.setText(choosenAlgorithm);
	    				GridData gd = new GridData ();
	    				gd.widthHint = 20;
	    				setAlgorithm.setLayoutData (gd);
	    				
	    				
	    				//Creates view to choose settings
	    				Label labelView = new Label(shell1, SWT.NONE);
	    				labelView.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				labelView.setText("View: ");
	    			
	    				Combo setView = new Combo(shell1, SWT.BORDER|SWT.READ_ONLY);
	    				setView.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				setView.setItems(views);
	    				setView.setText(choosenView);
	    				gd = new GridData ();
	    				gd.widthHint = 20;
	    				setView.setLayoutData (gd);
	    				
	    				
	    				//Creates maximum size of the maze settings 
	    				Label labelMazeSize = new Label(shell1, SWT.NONE);
	    				labelMazeSize.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				labelMazeSize.setText("Max size: ");
	    				    				
	    				Spinner setMaxSize = new Spinner(shell1, SWT.BORDER|SWT.READ_ONLY);
	    				setMaxSize.setMinimum(3);
	    				setMaxSize.setMaximum(100);	    			    
	    				setMaxSize.setIncrement(1);	    		
	    				setMaxSize.pack();
	    				setMaxSize.setSelection(Integer.parseInt(choosenMaxSize));
	    				gd = new GridData ();
	    				gd.widthHint = 20;
	    				setMaxSize.setLayoutData (gd);
	    					    					    				
	    				
	    				//Creates exit button in the menu window
	    				Button btnExit=new Button(shell1, SWT.NONE);
	    				btnExit.setText("Close");
	    				btnExit.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				
	    				
	    				//Creates save button in the menu window
	    				Button btnSave=new Button(shell1, SWT.NONE);
	    				btnSave.setText(" Save ");
	    				btnSave.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				
	    				
	    				
	    				//Listener of the exit button
	    			    btnExit.addSelectionListener(new SelectionListener() {
	    					
	    					@Override
	    					public void widgetSelected(SelectionEvent arg0) {	    						
	    						shell1.close();
	    						display1.close();
	    					}
	    					
	    					@Override
	    					public void widgetDefaultSelected(SelectionEvent arg0) {}
	    				});
	    			    
	    			    
	    			    //Listener of the save button
	    			    btnSave.addSelectionListener(new SelectionListener() {
	    					
	    					@Override
	    					public void widgetSelected(SelectionEvent arg0) {
	    						
	    						String command="saveProperties"+" "+(String)setAlgorithm.getText()+" "+(String)setMaxSize.getText()+" "+(String)setView.getText();	    						
	    						setChanged();
	    						notifyObservers(command);
	    						shell1.close();
	    						display1.close();
	    					}
	    					
	    					@Override
	    					public void widgetDefaultSelected(SelectionEvent arg0) {}
	    				});
	    			    
	    			    //Starts displaying a menu window
	    			    shell1.setSize(135,160);
	    			    shell1.open();
	    			    
	    			    
	    			    while (!shell1.isDisposed()&&!closeNow) {
	    			        if (!display1.readAndDispatch())
	    			        	display1.sleep();
	    			        if(closeNow){
	    			        	shell1.close();
	    	    			    display1.dispose();
	    			        }
	    			      }
	    			    	    			  	    			    		    
	    		    }
	            });
	    		thread.start();
	    		threads.add(thread);
	    	}
	    });
	    	
	    
	    //Listener of save command in the menu bar
	    fileSaveMazeItem.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		Thread thread=new Thread(new Runnable(){

	    			@Override
	    			public void run() {
	    				//Creates a new display and shell for save window
	    				Display display1 = new Display();
	    			    Shell shell1 = new Shell(display1);

	    			    //Sets new grid layout		    			    	    			    
	    			    GridLayout gridLayout = new GridLayout(2, false);
	    				shell1.setLayout(gridLayout);
	    				
	    				//Creates maze's name settings
	    				Label labelMazeName = new Label(shell1, SWT.NONE);
	    				labelMazeName.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				labelMazeName.setText("Maze name: ");
	    				
	    				Text titleMazeName = new Text(shell1, SWT.SINGLE | SWT.BORDER);
	    			   	
	    				
	    				//Creates save button
	    				Button btnSave=new Button(shell1, SWT.NONE);
	    				btnSave.setText("   Save   ");
	    				btnSave.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				
	    				//Creates exit button
	    				Button btnExit=new Button(shell1, SWT.NONE);
	    				btnExit.setText("   Close  ");
	    				btnExit.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));

	    				
	    				//Listener of the save button
	    				btnSave.addSelectionListener(new SelectionListener() {
	    					
	    					@Override
	    					public void widgetSelected(SelectionEvent arg0) {
	    						
	    						String command="saveMaze"+" "+mazeName+" "+(String)titleMazeName.getText();
	    						setChanged();
	    						notifyObservers(command);
	    						shell1.close();
	    						display1.close();
	    					}
	    					
	    					@Override
	    					public void widgetDefaultSelected(SelectionEvent arg0) {}
	    				});
	    				
	    				
	    				//Listener of the exit button
	    				btnExit.addSelectionListener(new SelectionListener() {
	    					
	    					@Override
	    					public void widgetSelected(SelectionEvent arg0) {
	    						shell1.close();
	    						display1.close();
	    					}
	    					
	    					@Override
	    					public void widgetDefaultSelected(SelectionEvent arg0) {}
	    				});
	    			
	    				
	    				//Starts shell and display of the save window
	    			    shell1.setSize(180,100);
	    			    shell1.open();
	    			    
	    			    while (!shell1.isDisposed()&&!closeNow) {
	    			        if (!display1.readAndDispatch())
	    			        	display1.sleep();
	    			        if(closeNow){
	    			        	shell1.close();
	    			        	display1.dispose();
	    			        }
	    			      }
	    			    display1.dispose();
	    			}
	    		});
	    		thread.start();
	    		threads.add(thread);
		   }
	    });
	    
	    
	    //Listener of load command in the menu bar
	    fileLoadMazeItem.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		Thread thread=new Thread(new Runnable(){

	    			@Override
	    			public void run() {
	    				//Creates a new shell and display for load window
	    				Display display1 = new Display();
	    			    Shell shell1 = new Shell(display1);
	    			    
	    			    //Sets a new grid layout with 2 columns
	    			    GridLayout gridLayout = new GridLayout(2, false);
	    				shell1.setLayout(gridLayout);
	    				
	    				
	    				//Creates settings for loading maze with specific name
	    				Label labelMazeToLoadName = new Label(shell1, SWT.NONE);
	    				labelMazeToLoadName.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				labelMazeToLoadName.setText("Maze to load: ");
	    				
	    				Text titleMazeToLoadName = new Text(shell1, SWT.SINGLE | SWT.BORDER);
	    				
	    				
	    				//Creates settings for a new name of the maze
	    				Label labelMazeNewName = new Label(shell1, SWT.NONE);
	    				labelMazeNewName.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				labelMazeNewName.setText("New name: ");
	    				
	    				Text titleMazeNewName = new Text(shell1, SWT.SINGLE | SWT.BORDER);
	    			   	    
	    				
	    				//Creates save button
	    				Button btnSave=new Button(shell1, SWT.NONE);
	    				btnSave.setText("   Load   ");
	    				btnSave.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
	    				
	    				
	    				//Creates exit button
	    				Button btnExit=new Button(shell1, SWT.NONE);
	    				btnExit.setText("   Close  ");
	    				btnExit.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));

	    				
	    				//Listener of the save button
	    				btnSave.addSelectionListener(new SelectionListener() {
	    					
	    					@Override
	    					public void widgetSelected(SelectionEvent arg0) {
	    						
	    						String command="loadMaze"+" "+(String)titleMazeToLoadName.getText()+" "+(String)titleMazeNewName.getText();
	    						mazeName=(String)titleMazeNewName.getText();
	    						//displayMessage("Press <<Start new game>> button to display the loaded maze");
	    						setChanged();
	    						notifyObservers(command);
	    						shell1.close();
	    						display1.close();
	    					}
	    					
	    					@Override
	    					public void widgetDefaultSelected(SelectionEvent arg0) {}
	    				});
	    				
	    				
	    				
	    				//Listener of the exit button
	    				btnExit.addSelectionListener(new SelectionListener() {
	    					
	    					@Override
	    					public void widgetSelected(SelectionEvent arg0) {
	    						shell1.close();
	    						display1.close();
	    					}
	    					
	    					@Override
	    					public void widgetDefaultSelected(SelectionEvent arg0) {}
	    				});
	    			
	    				
	    				//Starts shell and display
	    			    shell1.setSize(180,130);
	    			    shell1.open();
	    			    
	    			    while (!shell1.isDisposed()&&!closeNow) {
	    			        if (!display1.readAndDispatch())
	    			        	display1.sleep();
	    			        if(closeNow){
	    			        	shell1.close();
	    			        	display1.dispose();
	    			        }
	    			      }
	    			    display1.dispose();	    			    
	    			}
	    		});
	    		thread.start();
	    		threads.add(thread);
		   }
	    });
		
		
		//Disables buttons in the GUI		
		setPerspective.setEnabled(false);
		setLength.setEnabled(false);
		setWidth.setEnabled(false);
		setHeight.setEnabled(false);
		btnGenerateMaze.setEnabled(false);
		btnDisplaySolution.setEnabled(false);
		btnSolveMaze.setEnabled(false);
		btnGetHint.setEnabled(false);
				
		
		//Listener of perspective combo box
		setPerspective.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  int index=0;
		    	  if(setPerspective.getText().equals("x"))
		    	  {
		    		  index=mazeDisplay.getCurrentPosition().getX();
		    	  }
		    	  if(setPerspective.getText().equals("y"))
		    	  {
		    		  index=mazeDisplay.getCurrentPosition().getY();
		    	  }
		    	  if(setPerspective.getText().equals("z"))
		    	  {
		    		  index=mazeDisplay.getCurrentPosition().getZ();
		    	  }
		    	  String command="displayCrossSection"+" "+setPerspective.getText()+" "+String.valueOf(index)+" "+mazeName;
		    	  mazeDisplay.setPerspective((String)setPerspective.getText());
		    	  choosenPerspective=(String)setPerspective.getText();
		    	  setChanged();
		    	  notifyObservers(command);
		      }
		});
		

		//Listener of generate maze button
		btnGenerateMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnGetHint.setEnabled(true);
				String command="generate3dMaze"+" "+mazeName+" "+(String)setLength.getText()+" "+(String)setHeight.getText()+" "+(String)setWidth.getText();
		    	setChanged();
		    	notifyObservers(command);
		    	
				setPerspective.setEnabled(true);
				btnSolveMaze.setEnabled(true);
			
				shell.redraw();
				if(setLength.getText().equals("")==false&&setHeight.getText().equals("")==false&&setWidth.getText().equals("")==false)
				{
					btnGenerateMaze.setEnabled(false);
					setPerspective.setText("y");
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		//Listener solve maze button
		btnSolveMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			if(mazeDisplay.ifGameStarted())
			{
				String command="solve"+" "+mazeName+" "+(String)choosenAlgorithm;
		    	setChanged();
		    	notifyObservers(command);
				btnGenerateMaze.setEnabled(false);
				btnDisplaySolution.setEnabled(true);
				btnGetHint.setEnabled(true);
				btnSolveMaze.setEnabled(false);
			}
			else
			{
				displayMessage("You have to generate maze first");
			}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		//Listener of display solution button
		btnDisplaySolution.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(mazeDisplay.ifGameStarted())
				{
				  if(choosenPerspective.equals("")==false)
				  {
					  setPerspective.setEnabled(false);
					  String command="displaySolution"+" "+mazeName;
					  btnDisplaySolution.setEnabled(false);
					  setChanged();
					  notifyObservers(command);				  
				  }
				  else
				  {
					  displayMessage("You have to choose perspective first");
				  }
				}
				else
				{
					displayMessage("You have to generate maze first");
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		//Listener of get hint button
		btnGetHint.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				displayMessage("Exit from the maze is: "+mazeDisplay.getExit().toString());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		//Listener of the new game button 
		btnStartNewGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//Enables buttons in the GUI
				setPerspective.setEnabled(true);
				setLength.setEnabled(true);
				setWidth.setEnabled(true);
				setHeight.setEnabled(true);
				btnGenerateMaze.setEnabled(true);
				
				//Disables buttons in the GUI
				btnDisplaySolution.setEnabled(false);
				btnSolveMaze.setEnabled(false);
				btnGetHint.setEnabled(false);
				
				//Initializes empty maze data for maze display
				int mazeData[][]=new int[1][1];
				int maze[][][]=new int[1][1][1];
				mazeData[0][0]=0;
				
				//Sets data in the maze display class
				mazeDisplay.setMazeData(mazeData);
				mazeDisplay.setCurrentPosition(new Position(0,1,1));
				mazeDisplay.setMaze(maze);
				mazeDisplay.stopGame();
				mazeDisplay.stop();
				mazeDisplay.setLevel("1");
				mazeDisplay.setPerspective("");
				mazeName="defaultMazeName";
				
				//Checks if the default maze is used
				/*if(mazeName.equals("defaultMazeName")==false)
				{
					 String command="displayCrossSection"+" "+"y"+" "+"1"+" "+mazeName;
					 setChanged();
					 notifyObservers(command);
					 mazeDisplay.startGame();
					 mazeDisplay.setPerspective("y");
				}*/
				
				//Displays instructions for the user
				String message="1) Choose dimensions of the maze.\n2) Press button <<Generate maze>>.\n3) Choose perspective";
				displayMessage(message);
				 	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		//Listener for maze display for character moving
		mazeDisplay.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent event) 
			{
				
		        switch (event.keyCode) {
		        case SWT.ARROW_LEFT:{
		        	 mazeDisplay.moveLeft();
		        	 break;
		        }
		        case SWT.ARROW_RIGHT:{
		        	 mazeDisplay.moveRight();
		        	 break;
		        }
		        case SWT.ARROW_DOWN:{
		        	 mazeDisplay.moveDown();
		        	 break;
		        }
			    case SWT.ARROW_UP:{
		             mazeDisplay.moveUp();
		        	 break;
			    }
			    case SWT.PAGE_DOWN:{
		        	 mazeDisplay.moveLevelDown();
		        	 break;
			    }
			    case SWT.PAGE_UP:{
		             mazeDisplay.moveLevelUp();
		        	 break;
			    }
		        }		       
		        if(mazeDisplay.cameToExit())
		        {
		        	
		        	displayWinnerMessage();
		        }
		        labelDisplayPosition.setText(mazeDisplay.getCurrentPosition().toString());
		     }
		});
	}
	
	/*
	 * Displays cross section of the maze according to given axis and index
	 */
	@Override
	public void displayCrossSection(Maze3d maze, String axis, int index) {
        int mazeAsArray[][][]=maze.getMazeAs3DimArray();
		int mazeData[][]=null;
		if(axis.equals("x")==true){
			mazeData=new int[maze.getHeight()][maze.getWidth()];
			for(int y=0;y<maze.getHeight();y++)
			{
				for(int z=0;z<maze.getWidth();z++)
				{
					mazeData[y][z]=mazeAsArray[index][y][z];
				}
			}
		}
			
		if(axis.equals("y")==true){
			mazeData=new int[maze.getLength()][maze.getWidth()];
			for(int x=0;x<maze.getLength();x++)
			{
				for(int z=0;z<maze.getWidth();z++)
				{
					mazeData[x][z]=mazeAsArray[x][index][z];
				}
			}
		}
			
		if(axis.equals("z")==true){
			mazeData=new int[maze.getLength()][maze.getHeight()];
			for(int x=0;x<maze.getLength();x++)
			{
				for(int y=0;y<maze.getHeight();y++)
				{
					mazeData[x][y]=mazeAsArray[x][y][index];
				}
			}
		}
		
		mazeDisplay.setMazeData(mazeData);
		mazeDisplay.setMaze(mazeAsArray);
		mazeDisplay.setExit(maze.getGoalPosition());
	}
	
	/*
	 * Starts GUI
	 */
	@Override
	public void start()
	{
		this.run();
	}

	/*
	 * Displays messages for the user by using message box
	 */
	@Override
	public void displayMessage(String message) {
		
		Thread thread=new Thread(new Runnable(){

			@Override
			public void run() {
				gameCanBeClosed=false;
				Display display1 = new Display();
			    Shell shell1 = new Shell(display1);

			    int style = SWT.ICON_INFORMATION |SWT.OK | SWT.CANCEL;
			    
			    MessageBox messageBox = new MessageBox(shell1, style);
			    messageBox.setMessage(message);
			    int rc = messageBox.open();

			    switch (rc) {
			    case SWT.OK:
			      break;
			    case SWT.CANCEL:
			      break;
			    case SWT.YES:
			      break;
			    case SWT.NO:
			      break;
			    case SWT.RETRY:
			      break;
			    case SWT.ABORT:
			      break;
			    case SWT.IGNORE:
			      break;
			    }
			    
			    display1.dispose();
			    gameCanBeClosed=true;
			}
		});
		thread.start();
		threads.add(thread);
		
		//If maze was generated, so get cross section starts
		String search1  = "Maze";
		String search2 =  "is ready";
		if ((message.toLowerCase().indexOf(search1.toLowerCase()) != -1 )&&(message.toLowerCase().indexOf(search2.toLowerCase()) != -1 )) {
			mazeIsSolved=true;
			mazeDisplay.startGame();
			
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    	setPerspective.setText("y");
			    	String command="displayCrossSection"+" "+"y"+" "+"1"+" "+mazeName;
			        mazeDisplay.setPerspective((String)setPerspective.getText());
			    	choosenPerspective="y";
			        setChanged();
			    	notifyObservers(command);
			    }
			});
		}
		
		
		//If maze was loaded, so get cross section starts
		search1="Maze";
		search2="loaded";
		if ((message.toLowerCase().indexOf(search1.toLowerCase()) != -1 )&&(message.toLowerCase().indexOf(search2.toLowerCase()) != -1 )) {
			mazeIsSolved=true;
			mazeDisplay.startGame();
			
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			    	setPerspective.setText("y");
			    	String command="displayCrossSection"+" "+"y"+" "+"1"+" "+mazeName;
			        mazeDisplay.setPerspective((String)setPerspective.getText());
			    	choosenPerspective="y";
			        setChanged();
			    	notifyObservers(command);
			    	setPerspective.setEnabled(true);
			    	btnSolveMaze.setEnabled(true);
			    }
			});
		}
		
	}

	@Override
	public void displayMaze(Maze3d maze) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void displayFilesList(String[] filesList) {
		// TODO Auto-generated method stub		
	}

	/*
	 * Displays solution of the maze
	 */
	@Override
	public void displaySolution(Solution solution) {
		mazeDisplay.setSolution(solution);
		mazeDisplay.start();
	}

	/*
	 * Sets properties from the properties.xml file
	 */
	@Override
	public void setProperties(Properties dfp) {
		
		int size=Integer.parseInt(dfp.getMaxMazeSize());
		lengths=new String[size-2];
		heights=new String[size-2];
		widths=new String[size-2];
		indexes=new String[size-2];
		
		for(int i=0;i<size-2;i++)
		{
			
			lengths[i]=""+(i+3)+"";
			heights[i]=""+(i+3)+"";
			widths[i]=""+(i+3)+"";
			indexes[i]=""+(i+3)+"";
		}
		
		choosenAlgorithm=dfp.getDefaultAlgorithm();
		choosenMaxSize=dfp.getMaxMazeSize();
		choosenView=dfp.getViewSetup();
	}
	
	/*
	 * Displays message for user if he came to the exit from maze
	 */
	public void displayWinnerMessage()
	{
		Thread thread=new Thread(new Runnable(){
		

		@Override
		public void run() {
			final Display display1 = new Display();
		    final Shell shell1 = new Shell(display1);
		    shell1.setText("!!!WINNER!!!");
		    shell1.setLayout(new FillLayout());

		    Canvas canvas = new Canvas(shell1, SWT.NONE);
				    
		    canvas.addPaintListener(new PaintListener() {
		      public void paintControl(PaintEvent e) {
		        
		    	Image image = new Image(display1, "resources/winner.jpg");
		        e.gc.drawImage(image, 0, 0);
		        image.dispose();		       		        
		      }
		    });
		    
		    shell1.setSize(600,310);
		    shell1.open();
		    while (!shell1.isDisposed()&&!closeNow) {
		      if (!display1.readAndDispatch()) {
		        display1.sleep();
		      }
		      if(closeNow){
		        	shell1.close();
		        	display1.dispose();
		        }
		    }
		    display1.dispose();
		}
		
	    });
		thread.start();
		threads.add(thread);
	}
	
	/*
	 * Stops threads
	 */
	public void stopThreads()
	{
		for(int i=0;i<threads.size();i++)
		{
			threads.get(i).interrupt();
		}
	}
	
	/*
	 * Sets current position of the character
	 */
	public void setCurrentPosition(Position position) {
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	labelDisplayPosition.setText(position.toString());
		    }
		});
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
}
