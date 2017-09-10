package serverView;

import org.eclipse.swt.events.PaintEvent;

import org.eclipse.swt.graphics.Image;


/**GameCharacter - class that represents game character in GUI
 * @author Valery Polonsky and Tomer Dricker
 *
 */
public class GameCharacter {
	int x;
	int y;
	int z;
	Image image;
	
	
	/**
	 * Constructor - creates a new game character with given parameters
	 * @param x - Position of the character according to X axis
	 * @param y - Position of the character according to Y axis
	 * @param z - Position of the character according to Z axis
	 */
	public GameCharacter(int x,int y,int z){
		this.x=x;
		this.y=y;
		this.z=z;				
	}
	
	
	/**Paint function - Draws the character according to given parameters
	 * @param e - Paint event to use for drawing the image
	 * @param w - Width of the character
	 * @param h - Height of the character
	 */
	public void paint(PaintEvent e,int w,int h)
	{		
		image = new Image(e.display, "resources/character.jpg");				
		e.gc.drawImage(image, 0, 0, image.getBounds().width,image.getBounds().height,x,y,w,h);	    
	}
}
