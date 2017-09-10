package clientView;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class BasicWindow extends Observable implements Runnable {

	Display display;
	Shell shell;
	
	/*
	 * Constructor - creates a new window with given sizes
	 */
	public BasicWindow(String title,int width,int height)
	{
		display=new Display();
		shell=new Shell(display);
		shell.setSize(width,height);
		shell.setText(title);
	}
	
	/*
	 * Command that has to be overridden
	 */
	abstract void initWidgets();
	
	/*
	 * Runs main shell
	 */
	@Override
	public void run() {
		initWidgets();
		shell.open();
		while(!shell.isDisposed())
		{
			if(!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		display.dispose();
	}
}
