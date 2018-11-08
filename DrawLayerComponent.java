import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 *
 * @author Jamie
 */
public class DrawLayerComponent extends JComponent
{
    boolean showSnakeId = true;
    java.awt.Color color1 = new java.awt.Color(255, 220, 120);
    java.awt.Color color2 = new java.awt.Color(0, 120, 130);
    
    SnakeContainer sc[];
    Food[] food;
    
    java.awt.Rectangle background;
    java.util.LinkedList<java.awt.Rectangle> bgs;
    int fontSize;
    int labelOffset;

    public DrawLayerComponent(int width, int height, SnakeContainer sc[])
    {
	background = new java.awt.Rectangle(0, 0, width, height);
	this.sc = sc;
	bgs = new java.util.LinkedList<>();
	for ( int i = 0; i < sc.length; i++ )
	    bgs.add(new java.awt.Rectangle(sc[i].getXOffset(), sc[i].getYOffset(), sc[i].getSize(), sc[i].getSize()));
        
	fontSize = 12;
        labelOffset = (int)(bgs.get(0).getSize().height * .1);
    }
    
    public void paintComponent(Graphics g)
    {
	Graphics2D g2 = (Graphics2D) g;

	g2.setFont( new java.awt.Font("Arial", java.awt.Font.PLAIN, fontSize));
	
	g2.setColor(color2);
	g2.fill(background);
	
	if ( bgs != null )
	{
	    g2.setColor(color1);
	    for ( int i = 0; i < bgs.size(); i++ )
            {
                g2.setColor(color1);
		g2.fill(bgs.get(i));
                g2.setColor(java.awt.Color.BLACK);
                if ( showSnakeId) g2.drawString("Snake " + i, bgs.get(i).getLocation().x, bgs.get(i).getLocation().y+labelOffset);
                
            }
	}
	if ( sc != null && sc[0] != null )
	{
	    for ( int i = 0; i < sc.length; i++ )
	    {	
		for ( int j = 0; j < sc[i].getFood().length; j++ )
		{
		    food = sc[i].getFood();
		    g2.setColor(food[j].getColor());
		    g2.fill(food[j].getRect());
		}
		
		java.util.LinkedList<SnakeSegment> tempSS = sc[i].getSnake();
		for ( int j = 0; j < tempSS.size(); j++ )
		{
		    g2.setColor((tempSS.get(j)).getColor());
		    g2.fill((tempSS.get(j)).getRect());
		}
	    }
	}
    } // end paintComponent
    
    public void setShowSnakeId(boolean showSnakeId)
    {
        this.showSnakeId = showSnakeId;
    }
}
