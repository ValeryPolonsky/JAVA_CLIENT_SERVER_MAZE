package serverView;

import java.util.concurrent.Semaphore;

public class ServerGUISynchronizator {
	
	MyTCPIPServer server;
	ServerWindow gui;
	private Semaphore mutex=new Semaphore(1);
	
	
	public ServerGUISynchronizator()
	{
		this.server=null;
		this.gui=null;
	}
	
	public void setGUI(ServerWindow gui)
	{
		this.gui=gui;
	}
	
	public void setServer(MyTCPIPServer server)
	{
		this.server=server;
	}
	
	public ServerGUISynchronizator(MyTCPIPServer server,ServerWindow gui)
	{
		this.server=server;
		this.gui=gui;
		this.server.setSynchronizator(this);
	}
	
	public void sendMessageToGui(String message)
	{
		try {
			mutex.acquire();
			gui.setMessage(message);
			mutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setClientsToGUI(String[]clients)
	{
		gui.setClients(clients);
	}
}
