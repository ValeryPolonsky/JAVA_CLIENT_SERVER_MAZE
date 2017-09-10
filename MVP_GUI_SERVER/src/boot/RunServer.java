package boot;



import serverModel.MyModel;
import serverPresenter.Presenter;
import serverView.MyTCPIPServer;
import serverView.ServerGUISynchronizator;
import serverView.ServerWindow;




/**
 * Runs the project
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class RunServer {

	public static void main(String[] args) {

		MyModel model = new MyModel();
		ServerWindow view=new ServerWindow("Server",700,500);
		MyTCPIPServer server = new MyTCPIPServer(5555,5,view);
		Presenter presenter = new Presenter(model,view,server);
		model.addObserver(presenter);
		view.addObserver(presenter);	
		view.setServer(server);
		@SuppressWarnings("unused")
		ServerGUISynchronizator synchronizator=new ServerGUISynchronizator(server,view);
		server.start();
		view.start();
	}
}
