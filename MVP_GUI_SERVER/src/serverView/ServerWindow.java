package serverView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import common.CommandAndData;
import serverPresenter.Properties;

public class ServerWindow extends BasicWindow implements View,Observer{
	private Text text;
	private MyClientHandler clientHandler;
	private int clientID;
	private Semaphore mutex=new Semaphore(1);
	private Semaphore mutex1=new Semaphore(1);
	private MyTCPIPServer server;
	private Combo setClient;
	private String[]clients={};
	//private int clientIDToShutdown;
	
	public ServerWindow(String title, int width, int height) {
		super(title, width, height);
		// TODO Auto-generated constructor stub
	}
	
	public void setServer(MyTCPIPServer server)
	{
		this.server=server;
	}

	@Override
	void initWidgets() {
		GridLayout gridLayout = new GridLayout(3, false);
		shell.setLayout(gridLayout);
		
		Label labelClient= new Label(shell, SWT.NONE);
		labelClient.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelClient.setText("  Choose client: ");
		
		setClient = new Combo(shell, SWT.BORDER|SWT.READ_ONLY);
		setClient.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		setClient.setItems(clients);
		GridData gd = new GridData ();
		gd.widthHint = 20;
		setClient.setLayoutData (gd);
		
		
	    text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		
		Button btnExit=new Button(shell, SWT.NONE);
		btnExit.setText("Shutdown server");
		btnExit.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty = new Label(shell, SWT.NONE);
		labelEmpty.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty.setText("");
		
		Button btnExitClient=new Button(shell, SWT.NONE);
		btnExitClient.setText("Shutdown client ");
		btnExitClient.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		
		Label labelEmpty1 = new Label(shell, SWT.NONE);
		labelEmpty1.setLayoutData(new GridData(SWT.None, SWT.None, false, false, 1, 1));
		labelEmpty1.setText("");
		
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
	            	CommandAndData<Object> cad=new CommandAndData<Object>();
	            	cad.setCommand("exit");
	            	setChanged();
	            	notifyObservers(cad);
	            	display.close();
	            	event.doit=true;
	            }
	            else
	            {  	
	            	event.doit=false;
	            }
	            
	        }
	    });
		
		
		btnExit.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		btnExitClient.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				CommandAndData<Object> cad=new CommandAndData<Object>();
				cad.setCommand("exitClient");
				String []args=new String[1];
				args[0]=(String)setClient.getText();
				if(args[0].equals(""))
				{
					displayMessageBox("You have to choose client");
				}
				else
				{
					System.out.println("The argument is :"+args[0]);
					cad.setObjectData(args);
					setChanged();
					notifyObservers(cad);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	}
	
	
	

	private void displayMessageBox(String message) {
		Thread thread=new Thread(new Runnable(){

			@Override
			public void run() {
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
		
			}
		});
		thread.start();	
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
		this.run();
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
		setMessage("Server sent message: "+message+" to client: "+clientID);
		CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
		cad.setCommand(message);
		clientHandler.setCommandAndData(cad);
		mutex.release();
	}
	
	
	@Override
	public void displayMaze(Maze3d maze) {
		setMessage("Server sent maze to client "+clientID);
		CommandAndData<Maze3d>cad=new CommandAndData<Maze3d>();
		cad.setObject(maze);
		cad.setCommand("mazeFound");
		clientHandler.setCommandAndData(cad);
		mutex.release();
		
	}

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
		setMessage("Server sent solution to client "+clientID);
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
		this.clientID=ClientID;
		cad.setClientID(ClientID);
		setChanged();
		notifyObservers(cad);
	}
	
	public void setClientID(int clientID)
	{
		this.clientID=clientID;
	}


	public void setMessage(String message) {
		try {
			mutex1.acquire();
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {	   
			    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
			    	Date date = new Date();
			    	text.append("<<< "+dateFormat.format(date)+" >>> "+message+"\n");
			    }
			});
			mutex1.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setClients(String[]clients)
	{
		//this.clients=clients;
		try {
			mutex1.acquire();
			Display.getDefault().asyncExec(new Runnable() {
			    public void run() {	   
			    	setClient.setItems(clients);
			    }
			});
			mutex1.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exit() {
		server.stopServer();
	}
}
