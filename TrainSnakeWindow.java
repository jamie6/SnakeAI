import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
/**
 *
 * @author Jamie
 */
public class TrainSnakeWindow implements Window
{
    LinkedList<SnakeContainer> goats = new LinkedList<>(); // a collection of goat snakes (greatest of all time)
    LinkedList<Point> foodLocations = new LinkedList<>(); // all the food in every container will have the same location
    JFrame frame;
    
    JTextField fpsJTF;
    JTextField maxStepsJTF;
    JPanel buttonsJPanel;
    JPanel tableJPanel;
    JLabel generationJL; // label to show current generation
    JLabel fpsJL; // displays the current fps
    JScrollPane statJSP;
    JTable statJT; // statistical data on current snake population
    DefaultTableModel model; // jtable model
    JButton startJB, stopJB; // pauses and plays snake game
    JButton slowerJB, fasterJB; // controls the fps
    JButton maxStepsJB;
    JButton saveJB; // used to save nn of goat snake
    JButton setFPSJB;
    
    boolean isPaused;
    java.awt.event.KeyListener mkl; // user input
    DrawLayerComponent dlc;
    int windowWidth, windowHeight, FPS, ST, N, snakePopulation, generation, columns, maxSteps = 200;
    int space[][]; // this is what the ai will see. 0 = empty space, 1 = occupied, 3 = food
    SnakeContainer[] sc; // holds multiple instances of the snake game
    java.util.Random rand = new java.util.Random();
    
    public TrainSnakeWindow()
    {
	FPS = 25; // frames per second
	ST = 1000 / FPS; // skip ticks
	N = 20; // NxN grid
	snakePopulation = 100;
	windowWidth = 1600;
	windowHeight = 800;
	frame = new JFrame();
	frame.setSize(windowWidth+50, windowHeight+75);
        frame.setPreferredSize(frame.getSize()); 
	frame.setTitle("Snake A.I.");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	generation = 0;
        
        Random random = new Random();
        for ( int i = 0; i < 100; i++ )
        {
            foodLocations.add(new Point(random.nextInt(N), random.nextInt(N)));
        }
        
	newgame();
    }
    
    public void update()
    {
	if ( !isPaused )
	{
	    int go = 0; // gameover
	    for ( int i = 0; i < sc.length; i++ )
	    {
		sc[i].update();
		// "Snake", "Length", "Steps Taken", "Food Consumed", "Final Fitness"
		statJT.setValueAt(sc[i].getLength(), i, 1);
		statJT.setValueAt(sc[i].getStepsTaken(), i, 2);
		statJT.setValueAt(sc[i].getTotalFoodConsumed(), i, 3);
		if ( sc[i].isGameOver() ) 
		{
		    statJT.setValueAt(sc[i].getFitness(), i, 4);
		    go++;
		}
	    }
	    if ( go == sc.length ) // if all the snakes are at gameover then ...
	    {
		gameover();
	    }
	}
    }
    
    public void draw()
    {
	frame.repaint();
    }
    
    public void gameover()
    {
	frame.getContentPane().removeAll();
	newgame();
    }
    
    public void newgame()
    {
	columns = (int)Math.ceil(Math.sqrt(snakePopulation));// columns for snake container ui
	generation++;
	isPaused = false;
	initializeJControls();
        
	if ( generation > 1 ) geneticAlgorithm();
	else
	{
	    int snakeArea = (int)(0.9*windowWidth/2);
	    int contSize = (int)(0.9*snakeArea/columns);
	    int spacing = (int)(0.1*snakeArea/columns);
	    int currX = 0;
	    int currY = 2*spacing;
	    sc = new SnakeContainer[snakePopulation];
	    for ( int i = 0; i < snakePopulation; i++ )
	    {	    
		if ( i%columns == 0 )
		    currX = (int)(frame.getWidth()*0.025);
		sc[i] = new SnakeContainer(currX, currY, N, contSize, foodLocations);
		sc[i].setHumanInput(false);
                sc[i].setMaxSteps(maxSteps);
		currX += contSize + spacing;
		if ( (i+1)%columns == 0 ) currY += contSize + 2*spacing;
	    }
	}
        dlc = new DrawLayerComponent(windowWidth/2, windowWidth/2, sc);
        dlc.setPreferredSize(new Dimension(frame.getWidth()/2, frame.getHeight()));
        dlc.setLocation(frame.getWidth()/2, 0);
        frame.add(dlc, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void geneticAlgorithm()
    {
        // ********** ui stuff *************
	int snakeArea = (int)(0.9*windowWidth/2);
	int contSize = (int)(0.9*snakeArea/columns);
	int spacing = (int)(0.1*snakeArea/columns);
	int currX = 0;
	int currY = 2*spacing;
	// *********************************
	
	// selection
	int[] x = new int[snakePopulation]; // store index of snakes to breed
	int[] y = new int[snakePopulation]; // store index of snakes to breed
	java.util.LinkedList<SnakeContainer> snakesByFitness = new java.util.LinkedList<>();
	int totalFitness = 0;
	for ( int i = 0; i < snakePopulation; i++) 
	{
	    snakesByFitness.add(sc[i]);
	    totalFitness+=sc[i].getFitness(); // fitness is calculated when the snake dies
	    sc[i].setFitnessProbability(totalFitness);
	}
        
	// sort with in ascending order
	snakesByFitness.sort((SnakeContainer sc1, SnakeContainer sc2)->( (int)(sc1.getFitness() - sc2.getFitness())));
        
        // collect the best snake of the generation
        if ( goats.isEmpty() ) goats.add(snakesByFitness.getLast());
        else
        {
            if ( snakesByFitness.getLast().getFitness() > goats.getLast().getFitness() )
            {
                goats.add(snakesByFitness.getLast());
                goats.getLast().saveSnake(Integer.toString(generation));
            }
            else snakesByFitness.set(snakesByFitness.size()-1, goats.getLast());
        }
        // sort the goats
        goats.sort((a, b)->(a.getFitness()-b.getFitness()));
        if ( goats.size() > 20 ) for ( int i = 0; i < 15; i++ ) goats.removeFirst();

        // select the breeding partners
        for ( int i = 0; i < snakePopulation; i++ )
	{
	    int tempX = rand.nextInt(totalFitness);
	    int tempY = rand.nextInt(totalFitness);
	    for ( int j = 0; j < sc.length; j++ )
	    {
		if ( tempX < sc[j].getFitnessProbability() )
		{
		    x[i] = j;
		    break;
		}
	    }
	    for ( int j = 0; j < snakesByFitness.size(); j++ )
	    {
		if ( tempY < sc[j].getFitnessProbability() )
		{
		    y[i] = j;
		    break;
		}
	    }
	}
	
	// crossover && mutation
	SnakeContainer[] children = new SnakeContainer[snakePopulation];
	for ( int i = 0; i < snakePopulation; i++ )
	{
	    // ************ ui calculations *****
	    if ( i%columns == 0 ) currX = (int)(frame.getWidth()*0.025);
            
	    // ************ crossover ***********
            if ( i < snakePopulation/2 )
                children[i] = new SnakeContainer(currX, currY, N, contSize, foodLocations, snakesByFitness.get(x[i]).crossover(snakesByFitness.get(y[i])));
            else children[i] = new SnakeContainer(currX, currY, N, contSize, foodLocations, goats.getLast().crossover(snakesByFitness.getLast()));
	    children[i].setHumanInput(false);
            children[i].setMaxSteps(maxSteps);
	    
	    // ************ ui stuff ***********
	    currX += contSize + spacing;
	    if ( (i+1)%columns == 0 ) currY += contSize + 2*spacing;
	    
            // ************ mutate ********
            double chanceOfMutation = 0.75;
	    if ( rand.nextDouble() < chanceOfMutation ) children[i].mutation();
	}
        // assign children to next generation
        sc = children;
    }
    
    public void initializeJControls()
    {
        //********************** Controls ***************************
        buttonsJPanel = new JPanel();
	startJB = new JButton();
	startJB.setText("START");
	startJB.addActionListener(new ButtonActionListener());
	stopJB = new JButton();
	stopJB.setText("STOP");
	stopJB.addActionListener(new ButtonActionListener());
	slowerJB = new JButton();
	slowerJB.setText("<<");
	slowerJB.addActionListener(new ButtonActionListener());
	fasterJB = new JButton();
	fasterJB.setText(">>");
	fasterJB.addActionListener(new ButtonActionListener());
        maxStepsJB = new JButton();
        maxStepsJB.setText("Change Max Steps");
        maxStepsJB.addActionListener(new ButtonActionListener());
        saveJB = new JButton();
        saveJB.setText("Save");
        saveJB.addActionListener(new ButtonActionListener());
        setFPSJB = new JButton();
        setFPSJB.setText("Change Speed");
        setFPSJB.addActionListener(new ButtonActionListener());
	fpsJL = new JLabel();
	fpsJL.setText("Speed: ");
        fpsJTF = new JTextField();
        fpsJTF.setPreferredSize(new Dimension(40,30));
        fpsJTF.setText(Integer.toString(FPS));
        maxStepsJTF = new JTextField();
        maxStepsJTF.setText(Integer.toString(maxSteps));
        maxStepsJTF.setPreferredSize(new Dimension(40,30));
        buttonsJPanel.add(maxStepsJB);
        buttonsJPanel.add(maxStepsJTF);
        buttonsJPanel.add(saveJB);
        buttonsJPanel.add(startJB);
        buttonsJPanel.add(stopJB);
        buttonsJPanel.add(slowerJB);
        buttonsJPanel.add(fpsJL);
        buttonsJPanel.add(fpsJTF);
        buttonsJPanel.add(fasterJB);
        buttonsJPanel.add(setFPSJB);
	
        // ************************* TABLE ********************************
        generationJL = new JLabel();
	generationJL.setText("Generation: " + generation);
	generationJL.setFont(new java.awt.Font("Calibri",java.awt.Font.PLAIN, 18));
	generationJL.setSize(new Dimension(200, 200));
	generationJL.setLocation(50, 0);
	String tableColumnNames[] = {"Snake", "Length", "Steps Taken", "Food Consumed", "Final Fitness"};
	String data[][] = new String[snakePopulation][tableColumnNames.length];
	for ( int i = 0; i < snakePopulation; i++ ) // initialize data to 0
	    for ( int j = 0; j < tableColumnNames.length; j++ )
                data[i][j] = (j == 0) ? ((Integer)i).toString() : "";
	model = new DefaultTableModel(data, tableColumnNames);
	statJT= new JTable(model);
	statJT.setSize(windowWidth/4, (int)(windowHeight*0.1) + windowHeight/2);
	statJT.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 18));
        statJT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	statJSP = new JScrollPane(statJT);
	statJSP.setPreferredSize(new Dimension(statJT.getWidth(), statJT.getHeight()));
        tableJPanel = new JPanel();
        tableJPanel.add(generationJL, BorderLayout.CENTER);
        tableJPanel.add(statJSP, BorderLayout.CENTER);
        tableJPanel.setOpaque(true);
        tableJPanel.setVisible(true);
        
        //************************* frame ****************************
        frame.add(tableJPanel, BorderLayout.WEST);
        frame.add(buttonsJPanel, BorderLayout.SOUTH);
    }
    
    public int getFPS()
    {
	return FPS;
    }
    public int getST()
    {
	return ST;
    }
    
    class ButtonActionListener implements java.awt.event.ActionListener
    {
        @Override
	public void actionPerformed(java.awt.event.ActionEvent event)
	{
	    if ( event.getSource().equals(startJB) )
	    {
		isPaused = false;
	    }
	    else if ( event.getSource().equals(stopJB) )
	    {
		isPaused = true;
	    }
	    else if ( event.getSource().equals(slowerJB) )
	    {
		FPS--;
		if ( FPS < 1 ) FPS = 1;
		ST = 1000 / FPS; // skip ticks
                fpsJTF.setText(Integer.toString(FPS));
	    }
	    else if ( event.getSource().equals(fasterJB) )
	    {
		FPS++;
		ST = 1000 / FPS; // skip ticks
                fpsJTF.setText(Integer.toString(FPS));
	    }
            else if ( event.getSource().equals(maxStepsJB))
            {
                try
                {
                    maxSteps = Integer.parseInt(maxStepsJTF.getText());
                    if ( maxSteps < 1 ) maxSteps = 1;
                    for ( SnakeContainer s : sc ) s.setMaxSteps(maxSteps);
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }
            }
            else if ( event.getSource().equals(saveJB))
            {
                if ( !goats.isEmpty() ) goats.getLast().saveSnake("saved" + generation);
            }
            else if ( event.getSource().equals(setFPSJB))
            {
                try
                {
                    FPS = Integer.parseInt(fpsJTF.getText());
                    if (FPS > 300 ) FPS = 300;
                    else if ( FPS < 1 ) FPS = 1;
                    fpsJTF.setText(Integer.toString(FPS));
                    ST = 1000 / FPS; // skip ticks
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
	}
    }
}
