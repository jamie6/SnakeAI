/**
 *
 * @author Jamie
 */
public class Food
{
    private static int tileSize; // size of tile on the grid
    private static int foodSize;
    java.awt.Rectangle rect;
    java.awt.Color color;
    int x, y, xOffset, yOffset;
    public Food(int xOffset, int yOffset, int tileSize)
    {	
	this.tileSize = tileSize;
	this.foodSize = (int)(tileSize * .8);
	color = new java.awt.Color(0,0,255);
	x = -1;
	y = -1;
	rect = new java.awt.Rectangle(x, y, foodSize, foodSize);
	this.xOffset = xOffset;
	this.yOffset = yOffset;
    }
    
    public java.awt.Rectangle getRect()
    {
	return rect;
    }
    
    public java.awt.Color getColor()
    {
	return color;
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
    
    public void setPosition(int x, int y, int[][] space)
    {
	space[this.x+1][this.y+1] = 1; //set previous position to 1
	this.x = x;
	this.y = y;
	space[x+1][y+1] = 3; // new position to 3
	rect.x = xOffset + (int)((x * tileSize) + 0.5 * ( tileSize - rect.width ));
	rect.y = yOffset + (int)((y * tileSize) + 0.5 * (tileSize - rect.height));
    }
}
