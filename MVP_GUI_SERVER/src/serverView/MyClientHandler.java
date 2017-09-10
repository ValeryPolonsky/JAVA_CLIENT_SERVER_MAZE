package serverView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import common.CommandAndData;


public class MyClientHandler{
	private Socket client;	
	private boolean stopClient;
	private ServerWindow view;
	@SuppressWarnings("rawtypes")
	private CommandAndData cadToSend;
	private final Object lock = new Object();
	private MyTCPIPServer server;
	private int clientID;
	private ServerGUISynchronizator synchronizator;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;

	public MyClientHandler(Socket client, ServerWindow view) {
		this.client = client;
		this.stopClient=false;
		this.view = view;
		this.cadToSend=null;
	}
	
	public MyClientHandler() {
		this.client = null;
		this.stopClient=false;
		this.view=null;
		this.cadToSend=null;
	}
	
	@SuppressWarnings("rawtypes")
	public void setCommandAndData(CommandAndData cadToSend)
	{
		this.cadToSend=cadToSend;
		synchronized(lock){
            lock.notifyAll();
        }
	}
	
	public void setSynchronizator(ServerGUISynchronizator synchronizator)
	{
		this.synchronizator=synchronizator;
	}
	
	
	@SuppressWarnings("rawtypes")
	public void start() {
		Thread thread=new Thread(new Runnable(){

			@Override
			public void run() {
				while(!stopClient){
				try {
					ois = new ObjectInputStream(client.getInputStream());
					oos = new ObjectOutputStream(client.getOutputStream());

					byte[]bytes=(byte[])ois.readObject();
					CommandAndData cad=(CommandAndData)decompressData(bytes);
					
					if(cad.getCommand().equals("exit"))
					{
						stopClient();
						ois.close();
						oos.close();
					}
					else
					{
					synchronizator.sendMessageToGui("Client "+clientID+" sent command: "+cad.getCommand());
					/*System.out.println("The command from client is: "+cad.getCommand());
					for(int i=0;i<cad.getObjectData().length;i++)
					{
						System.out.println(cad.getObjectData()[i]);
					}*/
					setClientHandler();
					//view.setClientID(clientID);
					view.setCommandAndData(cad,clientID);
					synchronized(lock){
			            while(cadToSend==null){
			                try {
								lock.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
			            }
			        }
					bytes=null;
					bytes=compressData(cadToSend);
		           // System.out.println("Bytes array size is: "+bytes.length);
		            
		            oos.writeObject(bytes);
					cadToSend=null;
		                     		   
					}
				} catch (IOException e) {
					//System.out.println("ERROR HERE >>>> MyClientHandler: start()");
					//e.printStackTrace();	
					continue; 
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}});
		thread.start();
		thread.setName("MyClientHandler thread");
	}
	
	private void setClientHandler()
	{
		view.setClientHandler(this);
	}
	
	public void setServer(MyTCPIPServer server)
	{
		this.server=server;
	}
	
	public void stopClient(){
		//System.out.println("MyClientHandler: stopClient()");
		stopClient=true;
		try {
			if(ois!=null&&oos!=null)
			{
				ois.close();
				oos.close();
			}
			client.close();
			synchronizator.sendMessageToGui("Client "+clientID+" left");
			server.updateClientsList(this,clientID);
		} catch (IOException e) {
			System.out.println("ERROR HERE >>>> MyClientHandler: stopClient()");
			e.printStackTrace();
		}
		server.decrementUsersCounter();
	}

	public void setClientID(int clientID) {
		this.clientID=clientID;
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
