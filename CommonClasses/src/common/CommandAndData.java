package common;

import java.io.Serializable;

public class CommandAndData<T> implements Serializable{
	
	private static final long serialVersionUID = -3188329277471004262L;
	private String command;
	private String[] objectData;
	private T object;
	private int clientID;

	public CommandAndData(String command, T object,String objectName){
		this.setCommand(command);
		this.setObject(object);
	}
	
	public CommandAndData(){
		this.command=null;
		this.object=null;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String[] getObjectData() {
		return objectData;
	}

	public void setObjectData(String[] objectData) {
		this.objectData = objectData;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
}
