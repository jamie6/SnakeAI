/**
 *
 * @author Jamie
 */
public class SnakeSegment
{
    private java.awt.Rectangle rect;
    private java.awt.Color color;
    protected static int segSize;
    int x, y;
    
    public SnakeSegment(int x, int y, int xOffset, int yOffset, int segSize, java.util.Random rand)
    {
	initialSetup(x, y, xOffset, yOffset, segSize);
	color = new java.awt.Color( 0, rand.nextInt(255), 0 );
    }
	
    public SnakeSegment(int x, int y, int xOffset, int yOffset, int segSize, int head )
    {
	initialSetup(x, y, xOffset, yOffset, segSize);
	color = new java.awt.Color( 255, 0, 0 );
    }
    
    private void initialSetup(int x, int y, int xOffset, int yOffset, int segSize)
    {
	this.segSize = segSize;
	this.x = x;
	this.y = y;
	rect = new java.awt.Rectangle(x*segSize + xOffset, y*segSize + yOffset, segSize, segSize);
    }
    
    public void setX( int x )
    {
	rect.x += (x-this.x) * segSize;
	this.x = x;
    }
    
    public void setY( int y )
    {
	rect.y += (y-this.y)*segSize;
	this.y=y;
    }
    
    public int getX()
    {
	return x;
    }
    
    public int getY()
    {
	return y;
    }
    
    public int getActualX()
    {
	return rect.x;
    }
    
    public int getActualY()
    {
	return rect.y;
    }
    
    public java.awt.Color getColor()
    {
	return color;
    }
    
    public java.awt.Rectangle getRect()
    {
	return rect;
    }
}