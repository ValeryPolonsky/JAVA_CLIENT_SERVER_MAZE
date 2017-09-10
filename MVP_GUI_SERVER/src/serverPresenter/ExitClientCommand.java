package serverPresenter;

import serverModel.Model;
import serverView.MyTCPIPServer;
import serverView.View;

public class ExitClientCommand implements Command {

	View view;
	Model model;
	MyTCPIPServer server=null;
	
	public ExitClientCommand(View view,Model model,MyTCPIPServer server)
	{
		this.view=view;
		this.model=model;
		this.server=server;
	}
	
	@Override
	public void doCommand(String[] args) {
		if(server!=null)
		{
		   server.shutdownClient(args[0]);
		}
	}

}
