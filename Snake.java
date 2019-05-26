import java.util.LinkedList;
import java.awt.Point;
/**
 *
 * @author Jamie
 */
public class Snake
{
    static int hiddenNodes = 20, hiddenLayers = 1;
    
    boolean humanInput = false;
    int fitness;
    
    static final int NORTH = 1;
    static final int SOUTH = -1;
    static final int EAST = 2;
    static final int WEST = -2;
    
    java.util.Random rand;
    int segSize, direction;
    int xOffset, yOffset;
    int tailIndex, addedSegmentCount;
    LinkedList<SnakeSegment> body;
    java.awt.Color color;
    int[][] space;
    int manhattanDistanceToFood;
    
    NeuralNetwork nn;
    int maxSteps = 400;
    int stepsTaken = 0;
    
    public Snake(int xOffset, int yOffset, int gridSize, int n, int[][] space)
    {
        // changing output from 4 (north, south, east, west) to 3 (left, right, straight)
	nn = new NeuralNetwork(16, hiddenNodes, 3, 2 + hiddenLayers); // input, hidden, output, layers
	initializeSnake(xOffset, yOffset, gridSize, n, space);
    }
    
    public Snake(int xOffset, int yOffset, int gridSize, int n, int[][] space, NeuralNetwork nn)
    {
	this.nn = nn;
	initializeSnake(xOffset, yOffset, gridSize, n, space);
    }
    
    private void initializeSnake(int xOffset, int yOffset, int gridSize, int n, int[][] space)
    {
        addedSegmentCount=0;
	rand = new java.util.Random();
	direction = -2;
	this.xOffset = xOffset;
	this.yOffset = yOffset;
	body = new LinkedList<>();
	segSize = gridSize/n;
	int x =(n/2);
	int y =(n/2);
	this.space = space;
	body.add(new SnakeSegment(x, y, xOffset, yOffset, segSize, 0)); // add the head
	x++;
	body.add(new SnakeSegment(x, y, xOffset, yOffset, segSize, rand));
	x++;
	body.add(new SnakeSegment(x, y, xOffset, yOffset, segSize, rand));
	tailIndex = 2;
    }
    
    public NeuralNetwork crossover( NeuralNetwork partner)
    {
	return nn.crossover(partner);
    }
    
    public void mutation()
    {
	nn.mutation();
    }
    
    public boolean checkCollisionWithSelfOrBorder()
    {
	return space[body.get(0).getX()+1][body.get(0).getY()+1] == 2;
    }
    
    public boolean checkCollisionWithFood()
    {
	if ( space[body.get(0).getX()+1][body.get(0).getY()+1] == 4 )
	{
	    space[body.get(0).getX()+1][body.get(0).getY()+1] = 1;
	    return true;
	}
	return false;
    }
    
    public void addSegment()
    {
	addedSegmentCount++;
	body.add(++tailIndex, new SnakeSegment(body.get(body.size()-1).getX(), body.get(body.size()-1).getY(), xOffset, yOffset, segSize, rand));
    }
    
    public int getLength()
    {
	return body.size();
    }
    
    public int getStepsTaken()
    {
	return stepsTaken;
    }
    
    public boolean isSnakeOutOfSteps()
    {
	return stepsTaken>=maxSteps;
    }
    
    public void update()
    {
	if ( !humanInput )stepsTaken++;
	if ( addedSegmentCount > 0 )
	{
	    addedSegmentCount--;
	    if ( space[body.get(tailIndex).getX()+1][body.get(tailIndex).getY()+1] != 1 )
		space[body.get(tailIndex).getX()+1][body.get(tailIndex).getY()+1] = 1;
	}
	else space[body.get(tailIndex).getX()+1][body.get(tailIndex).getY()+1] -= 1;
	
	body.get(tailIndex).setX(body.get(0).getX());
	body.get(tailIndex).setY(body.get(0).getY());
	if ( --tailIndex == 0 ) tailIndex = body.size()-1;
	
	if ( direction == NORTH ) body.get(0).setY(body.get(0).getY()-1); // up
	else if ( direction == EAST ) body.get(0).setX(body.get(0).getX()+1); // right
	else if ( direction == SOUTH ) body.get(0).setY(body.get(0).getY()+1); // down
	else if ( direction == WEST ) body.get(0).setX(body.get(0).getX()-1); // left
	space[body.get(0).getX()+1][body.get(0).getY()+1] += 1; // head entering new space
    }
    
    public void updateDirection( int newDirection )
    {
	if ( direction + newDirection != 0 )
	    direction = newDirection;
    }
    
    public void updateDirectionWithNeuralNetwork()
    {
	int newDirection = nn.getOutput(processSpaceIntoInput()); // outputs values 0, 1, 2
        /*
        0   go straight
        1   go left
        2   go right
        */
        
        if ( newDirection == 0 ) return;
        
        if ( direction == NORTH ) 
        {
            if ( newDirection == 1 ) // GO LEFT
            {
                direction = WEST;
            }
            else if ( newDirection == 2 ) // GO RIGHT
            {
                direction = EAST;
            }
        }
        else if ( direction == SOUTH )
        {
            if ( newDirection == 1 ) // GO LEFT
            {
                direction = EAST;
            }
            else if ( newDirection == 2 ) // GO RIGHT
            {
                direction = WEST;
            }
        }
        else if ( direction == EAST )
        {
            if ( newDirection == 1 ) // GO LEFT
            {
                direction = NORTH;
            }
            else if ( newDirection == 2 ) // GO RIGHT
            {
                direction = SOUTH;
            }
        }
        else if ( direction == WEST )
        {
            if ( newDirection == 1 ) // GO LEFT
            {
                direction = SOUTH;
            }
            else if ( newDirection == 2 ) // GO RIGHT
            {
                direction = NORTH;
            }
        }
    }
    
    public void setManhattanDistanceToFood(int manhattanDistanceToFood )
    {
	this.manhattanDistanceToFood = manhattanDistanceToFood;
    }
    
    private int[] processSpaceIntoInput()
    {
	int[] input = new int[16];
        // look for occupied space in 8 directions
        // look for food in 8 directions
        // Point(row, column)
        //Point ahead, aheadLeft, aheadRight, behind, behindLeft, behindRight, left, right;
        Point[] directions = new Point[8];
        int index = 0;
        if ( direction == NORTH )
        {
            directions[index++] = new Point(-1, 0);
            directions[index++] = new Point(-1,-1);
            directions[index++] = new Point(-1, 1);
            directions[index++] = new Point(1, 0);
            directions[index++] = new Point(1, -1);
            directions[index++] = new Point(1, 1);
            directions[index++] = new Point(0,-1);
            directions[index++] = new Point(0,1);
        }
        else if ( direction == SOUTH )
        {
            directions[index++] = new Point(1, 0);
            directions[index++] = new Point(1,1);
            directions[index++] = new Point(1, -1);
            directions[index++] = new Point(-1, 0);
            directions[index++] = new Point(-1, 1);
            directions[index++] = new Point(-1, -1);
            directions[index++] = new Point(0,1);
            directions[index++] = new Point(0,-1);
        }
        else if ( direction == EAST )
        {
            directions[index++] = new Point(0, 1);
            directions[index++] = new Point(-1,1);
            directions[index++] = new Point(1,1);
            directions[index++] = new Point(0,-1);
            directions[index++] = new Point(-1, -1);
            directions[index++] = new Point(1, -1);
            directions[index++] = new Point(-1,0);
            directions[index++] = new Point(1,0);
        }
        else // if ( direction == WEST )
        {
            directions[index++] = new Point(0, -1);
            directions[index++] = new Point(1,-1);
            directions[index++] = new Point(-1,-1);
            directions[index++] = new Point(0,1);
            directions[index++] = new Point(1,1);
            directions[index++] = new Point(-1,1);
            directions[index++] = new Point(1,0);
            directions[index++] = new Point(-1,0);
        }

        int startingRow = body.get(0).getX();
        int startingColumn = body.get(0).getY();
        int maxRow = space.length-1;
        int maxColumn = space[0].length-1;
        int currentRow;
        int currentColumn;
        
        for ( int i = 0; i < directions.length; i++ )
        {
            currentRow = startingRow + directions[i].x;
            currentColumn = startingColumn + directions[i].y;
            while ( currentRow > -2 && currentRow < maxRow && currentColumn > -2 && currentColumn < maxColumn )
            {
                if ( space[currentRow+1][currentColumn+1] == 1 )
                {
                    input[2*i] = Math.abs(startingRow-currentRow)+Math.abs(startingColumn-currentColumn);
                }
                else if ( space[currentRow+1][currentColumn+1] == 3 )
                {
                    input[2*i+1] = 1; //Math.abs(startingRow-currentRow)+Math.abs(startingColumn-currentColumn);
                }
                currentRow += directions[i].x;
                currentColumn += directions[i].y;
            }
        }
        
	return input;
    }
    
    public LinkedList<SnakeSegment> getSnakeBody()
    {
	return body;
    }
    
    public int getSnakeSegmentSize()
    {
	return segSize;
    }
    
    public int getTotalFoodConsumed()
    {
	return body.size()-3; // 3 is the initial size
    }
    
    public void setFitness(int fitness)
    {
        this.fitness = fitness;
    }
    
    public int getFitness()
    {
        return fitness;
    }
    
    public void setHumanInput(boolean humanInput)
    {
        this.humanInput = humanInput;
    }
}
