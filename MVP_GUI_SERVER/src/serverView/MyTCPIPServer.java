package serverView;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import serverPresenter.Presenter;


public class MyTCPIPServer{
	private ServerSocket server;
	private ExecutorService executor;
	private boolean stopServer = false;
	Presenter presenter;
	ServerWindow view;
	private AtomicInteger numberOfConnectedUsers= new AtomicInteger(0);
	int clientID=0;
	ServerGUISynchronizator synchronizator;
	private HashMap<String,MyClientHandler> handlers=new HashMap<String,MyClientHandler>();
	private ArrayList<String>connectedClients=new ArrayList<String>();
	
	public MyTCPIPServer(int port, int maxNumOfClients,ServerWindow view) {
		this.view=view;
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(10000);
			executor = Executors.newFixedThreadPool(maxNumOfClients);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void setSynchronizator(ServerGUISynchronizator synchronizator)
	{
		this.synchronizator=synchronizator;
	}
	
	public void start() {
		synchronizator.sendMessageToGui("Server's status: server started.");
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while (!stopServer) {
					//System.out.println("Waiting for clients...");
					synchronizator.sendMessageToGui("Server's status: waiting to clients...");
					try {
						Socket client = server.accept();
						//System.out.println("Client connected");
						if (client != null) {							
							executor.submit(new Runnable() {								
								public void run() {
									incrementUsersCounter();
									MyClientHandler handler = new MyClientHandler(client,view);
									setServerToHandler(handler);
									clientID++;
									setClientIDToHandler(handler,clientID);
									synchronizator.sendMessageToGui("Client "+clientID+ " connected");
									handler.setSynchronizator(synchronizator);
									handler.start();	
									handlers.put(""+clientID+"", handler);
									connectedClients.add(""+clientID+"");
									setClientsList();
								}								
							});
						}				
						
					} catch (IOException e) {
						
					}
					
				}				
			}
		});
		thread.start();			
	}
	
	public void stopServer()
	{
		stopServer=true;
		try {
			server.close();
			executor.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setServerToHandler(MyClientHandler handler)
	{
		handler.setServer(this);
	}
	
	private void setClientIDToHandler(MyClientHandler handler,int clientID)
	{
		handler.setClientID(clientID);
	}
	
	private void setClientsList()
	{
		String[]clients=new String[connectedClients.size()];
		int i=0;
		for (String client: connectedClients) {
		    clients[i]=client;
		    i++;
		}
		synchronizator.setClientsToGUI(clients);
	}
	
	public void incrementUsersCounter() {
		numberOfConnectedUsers.incrementAndGet();
	}

	public void decrementUsersCounter() {
	    numberOfConnectedUsers.decrementAndGet();
	}

	public void shutdownClient(String string) {
		handlers.get(string).stopClient();
		connectedClients.remove(string);
		setClientsList();
	}

	public void updateClientsList(MyClientHandler handler,int clientID) {
		handlers.remove(clientID);
		connectedClients.remove(""+clientID+"");
		setClientsList();
	}
}