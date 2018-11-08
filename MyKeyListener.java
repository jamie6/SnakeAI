import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
/**
 *
 * @author Jamie
 */
public class MyKeyListener implements java.awt.event.KeyListener
{
    int direction = -2; // default direction is left
    @Override
    public void keyPressed(KeyEvent event){action(event);}
    public void keyReleased(KeyEvent event){action(event);}
    public void keyTyped(KeyEvent event){action(event);}
    private void action(KeyEvent event)
    {
        String key = KeyStroke.getKeyStrokeForEvent(event).toString();
        //System.out.println(key);
	key = key.replace("pressed " , "" );
	if ( key.equals("LEFT") )
	{ direction = -2; }
	else if ( key.equals("RIGHT") )
	{ direction = 2; }
	else if ( key.equals("UP") )
	{ direction = 1; }
	else if ( key.equals("DOWN") )
	{  direction = -1; }
    }
    
    public int getDirection()
    {
	return direction;
    }
}
