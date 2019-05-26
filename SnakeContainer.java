import java.awt.Point;
import java.util.LinkedList;
/**
 *
 * @author Jamie
 */
public class SnakeContainer
{
    LinkedList<Point> foodLocations;
    boolean isFoodEaten = false;
    boolean isFoodRandom = true;
            
    boolean humanInput;
    private int xOffset, yOffset, N, size;
    int space[][]; // this is what the ai will see. 0 = empty space, 1 = occupied, 3 = food
    Food[] food;
    int amountOfFood=1;
    Snake snake;
    java.util.Random rand;
    boolean gameOver;
    boolean child;
    int fitnessProbability;
    
    public SnakeContainer( int xOffset, int yOffset, int N, int size )
    {
	initializeContainer(xOffset, yOffset, N, size);
	newgame();
    }
    
    public SnakeContainer( int xOffset, int yOffset, int N, int size, NeuralNetwork nn )
    {
	initializeContainer(xOffset, yOffset, N, size);
	newgame(nn);
    }
    
    public SnakeContainer( int xOffset, int yOffset, int N, int size, LinkedList<Point> foodLocations )
    {
	initializeContainer(xOffset, yOffset, N, size);
        this.foodLocations = foodLocations;
        isFoodRandom = false;
	newgame();
    }
    
    public SnakeContainer( int xOffset, int yOffset, int N, int size, LinkedList<Point> foodLocations, NeuralNetwork nn )
    {
	initializeContainer(xOffset, yOffset, N, size);
        this.foodLocations = foodLocations;
        isFoodRandom = false;
        newgame(nn);
    }
    
    private void initializeContainer(int xOffset, int yOffset, int N, int size) {
        humanInput = false;
	this.xOffset = xOffset;
	this.yOffset = yOffset;
	this.N = N;
	this.size = size;
	rand = new java.util.Random();
	space = new int[N+2][N+2]; 
    }
    
    private void initializeSnake( NeuralNetwork nn )
    {
	snake = new Snake(xOffset, yOffset, size, N, space, nn);
        initializeSnake();
    }
    
    private void initializeSnake() {
        if ( snake == null ) snake = new Snake(xOffset, yOffset, size, N, space);
	
	for ( int i = 0; i < snake.getLength(); i++ )
	{ // getSnakeBody returns a list of the snake segments so that you can reference each segments coordinates
	    space[snake.getSnakeBody().get(i).getX()+1][snake.getSnakeBody().get(i).getY()+1] = 1;
	}
	size = N*snake.getSnakeSegmentSize();
    }
    
    private void initializeSpace() {
        for ( int i = 0; i < N+2; i++ ) // initialize space values
	{
	    for ( int j = 0; j < N+2; j++ )
	    {
		if ( i == 0 || i == N+1 || j == 0 || j == N+1 ) space[i][j] = 1;
		else space[i][j] = 0;
	    }
	}
    }
    
    private void initializeFood() {
        food = new Food[amountOfFood];
	for ( int i = 0; i < amountOfFood; i++ )
	    food[i] = new Food(xOffset, yOffset, size/N);
        
        if ( isFoodRandom ) // place food in random spot
        {
            for ( int i = 0; i < amountOfFood; i++)
            {
                Point point = findWhereToPutFood();
                food[i].setPosition( (int)point.x, (int)point.y, space);
            }
        }
    }
    
    final public void newgame(NeuralNetwork nn) {
        gameOver = false;
        initializeSpace();
        initializeSnake(nn);
        initializeFood();
    }
    
    final public void newgame() {
        gameOver = false;
        initializeSpace();
        initializeSnake();
        initializeFood();
    }
    
    private Point findWhereToPutFood()
    {
	java.awt.Point point = new java.awt.Point();
	
	int x = rand.nextInt(N);
	int y = rand.nextInt(N);
	while ( space[x+1][y+1] > 0 )
	{
	    x = rand.nextInt(N);
	    y = rand.nextInt(N);
	}
	point.x = x;
	point.y = y;
	return point;
    }
    
    public boolean isFoodRandom()
    {
        return isFoodRandom;
    }
    
    public void setIsFoodRandom(boolean isFoodRandom)
    {
        this.isFoodRandom = isFoodRandom;
    }
   
    public void update()
    {
	if ( !gameOver )
	{
            // try to place food
            if ( !isFoodRandom )
            {
                Point p = foodLocations.get(snake.getTotalFoodConsumed());
                if ( space[p.x+1][p.y+1] == 0 )
                {
                    for ( int i = 0; i < amountOfFood;i++)
                    {
                        food[i].setPosition( (int)p.x, (int)p.y, space);
                    }
                }
            }
	    snake.setManhattanDistanceToFood(getManhattanDistanceToFood());
	    if ( !humanInput ) snake.updateDirectionWithNeuralNetwork();
	    snake.update();
            
	    if ( snake.checkCollisionWithSelfOrBorder() || snake.isSnakeOutOfSteps() )
	    {
		gameover();
	    }
	    
	    if ( snake.checkCollisionWithFood() )
	    {
		snake.addSegment();
                if ( !humanInput ) snake.maxSteps += 100;

                if ( isFoodRandom )
                {
                    for ( int i = 0; i < amountOfFood;i++)
                    {
                        java.awt.Point point = findWhereToPutFood();
                        food[i].setPosition( (int)point.x, (int)point.y, space);
                    }
                }
                /*
                else // this may be causing food bug where food is placed on snake body
                { // and becomes unreachable, therefore unedible preventing snake to grow
                    Point p = foodLocations.get(snake.getTotalFoodConsumed());
                    for ( int i = 0; i < amountOfFood;i++)
                    {
                        food[i].setPosition( (int)p.x, (int)p.y, space);
                    }
                } //*/
	    }
	}
    }
    
    public void gameover()
    {
	calculateFitness();
	gameOver = true;
    }
    
    public boolean isGameOver()
    {
	return gameOver;
    }
    
    private void calculateFitness()
    {
        snake.setFitness(snake.getTotalFoodConsumed() * 5000 + snake.getStepsTaken()/(1+getManhattanDistanceToFood()) + 1000/(1+getManhattanDistanceToFood()));
        //snake.setFitness(snake.getTotalFoodConsumed() + 1);
    }
    
    public int getManhattanDistanceToFood()
    {
	int manhattanDistanceToFood = 0;
	if ( food.length == 1 )
	{
	    int x = food[0].getX();
	    int y = food[0].getY();
	    
	    int x2 = snake.getSnakeBody().getFirst().getX();
	    int y2 = snake.getSnakeBody().getFirst().getY();
	    
	    manhattanDistanceToFood = Math.abs(x-x2) + Math.abs(y-y2);
	}
	else return 1;
	return manhattanDistanceToFood;
    }
    
    public java.util.LinkedList<SnakeSegment> getSnake()
    {
	return snake.getSnakeBody();
    }
    
    public int getStepsTaken()
    {
	return snake.getStepsTaken();
    }
    
    public int getTotalFoodConsumed()
    {
	return snake.getTotalFoodConsumed();
    }
    
    public Food[] getFood()
    {
	return food;
    }
    
    public int getXOffset()
    {
	return xOffset;
    }
    
    public int getYOffset()
    {
	return yOffset;
    }
    
    public int getSize()
    {
	return size;
    }
    
    public int getLength()
    {
	return snake.getLength();
    }
    
    public void updateDirection( int newDirection )
    {
	snake.updateDirection(newDirection);
    }
    
    public void setHumanInput( boolean humanInput )
    {
	this.humanInput = humanInput;
        snake.setHumanInput(humanInput);
    }
    
    public boolean isHumanInput()
    {
	return humanInput;
    }
    
    public int getFitness()
    {
	return snake.getFitness();
    }
    
    public void setFitnessProbability(int fitnessProbability)
    {
	this.fitnessProbability = fitnessProbability;
    }
    
    public int getFitnessProbability()
    {
	return fitnessProbability;
    }
    
    public NeuralNetwork crossover(SnakeContainer partner)
    {
	return snake.crossover(partner.snake.nn);
    }
    
    public void mutation()
    {
	snake.mutation();
    }
    
    public void printSpace()
    {
	for ( int i = 0; i < space.length; i++)
	{
	    for ( int j = 0; j < space[0].length; j++)
		System.out.print(space[j][i] + " " );
	    System.out.println();
	}
    }
    
    public void setFileNumber(String fileName)
    {
        snake.nn.setFileName(fileName);
    }
    
    public void saveSnake(String fileName)
    {
        snake.nn.saveToFile(fileName);
    }
    
    public void setMaxSteps(int maxSteps)
    {
        snake.maxSteps = maxSteps;
    }
}
