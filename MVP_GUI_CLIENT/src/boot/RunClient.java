package boot;

import clientModel.MyModel;
import clientPresenter.Presenter;
import clientView.MazeWindow;
import clientView.Synchronizator;


/**
 * Runs the project
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class RunClient {

	public static void main(String[] args) {

		    MyModel model=new MyModel("localhost",5555);
		    Synchronizator synchronizator=new Synchronizator();
		    MazeWindow view = new MazeWindow("Game",500,500,synchronizator);
			Presenter presenter = new Presenter(model, view);
			view.addObserver(presenter);
		    model.addObserver(presenter);	
			view.start();
	}
}
