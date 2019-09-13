import javax.swing.JFrame;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Point;
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

    TrainSnakeUI tsui;
    TrainSnakeTable tst;
    SnakeContainerJComponent scjc;
    static int snakePopulation;
    int windowWidth, windowHeight, N, generation, columns, maxSteps = 200;
    int space[][]; // this is what the ai will see. 0 = empty space, 1 = occupied, 3 = food
    SnakeContainer[] sc; // holds multiple instances of the snake game
    java.util.Random rand = new java.util.Random();

    public TrainSnakeWindow()
    {
        initialize();
        newgame();
    }
    
    public TrainSnakeWindow(int population, int hiddenNodes, int hiddenLayers)
    {
        initialize();
        snakePopulation = population;
        Snake.hiddenNodes = hiddenNodes;
        Snake.hiddenLayers = hiddenLayers;
        newgame();
    }
    
    private void initialize()
    {
        N = 20; // NxN grid
        snakePopulation = 100;
        windowWidth = 1600;
        windowHeight = 800;
        frame = new JFrame();
        frame.setSize(windowWidth + 50, windowHeight + 75);
        frame.setPreferredSize(frame.getSize());
        frame.setTitle("Snake A.I.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generation = 0;

        Random random = new Random();
        for (int i = 0; i < 100; i++)
        {
            foodLocations.add(new Point(random.nextInt(N), random.nextInt(N)));
        }
    }

    @Override
    public void update()
    {
        if (!tsui.isPaused())
        {
            int go = 0; // gameover
            for (int i = 0; i < sc.length; i++)
            {
                sc[i].update();
                go += tst.updateTable(i, sc[i]); // returns 1 if game over, returns 0 if not
            }
            if (go == sc.length) // if all the snakes are at gameover then ...
            {
                gameover();
            }
        }
    }

    @Override
    public void draw()
    {
        frame.repaint();
    }

    @Override
    public int getFPS()
    {
        return tsui.getFPS();
    }

    @Override
    public int getST()
    {
        return tsui.getST();
    }

    public void gameover()
    {
        frame.getContentPane().remove(scjc);
        newgame();
    }

    final public void newgame()
    {
        int wholeContainerSize = (int) (windowWidth * 0.45);
        columns = (int) Math.ceil(Math.sqrt(snakePopulation));// columns for snake container ui
        generation++;

        if (generation > 1) 
            geneticAlgorithm();
        else
        {
            int contSize = (int) (0.9 * wholeContainerSize / columns);
            int spacing = (int) (0.1 * wholeContainerSize / (columns + 1));
            int startingPoint = (wholeContainerSize - ((spacing + contSize) * columns - spacing - spacing)) / 2;
            int currX = 0;
            int currY = startingPoint;
            sc = new SnakeContainer[snakePopulation];
            for (int i = 0; i < snakePopulation; i++)
            {
                if (i % columns == 0)
                    currX = startingPoint;
                sc[i] = new SnakeContainer(currX, currY, N, contSize, foodLocations);
                sc[i].setHumanInput(false);
                sc[i].setMaxSteps(maxSteps);
                currX += contSize + spacing;
                if ((i + 1) % columns == 0)
                    currY += contSize + spacing;
            }

            tst = new TrainSnakeTable(frame, snakePopulation, windowWidth, windowHeight, generation);
            tsui = new TrainSnakeUI(frame, goats, sc, windowWidth, windowHeight, generation);
        }
        tst.setGeneration(generation);
        tsui.setGeneration(generation);
        tsui.setSC(sc);

        if (scjc != null)
        {
            int temp = scjc.getSelectedIndex();
            scjc.newgame(sc,tst);
            scjc.setSelectedIndex(temp);
            tst.highlightRow(temp);
        } else
        {
            scjc = new SnakeContainerJComponent(wholeContainerSize, sc, tst);
            frame.pack();
            frame.setVisible(true);
        }

        frame.add(scjc, BorderLayout.EAST);
    }

    public void geneticAlgorithm()
    {
        // fitness && selection
        int[] x = new int[snakePopulation]; // store index of snakes to breed
        int[] y = new int[snakePopulation]; // store index of snakes to breed
        java.util.LinkedList<SnakeContainer> snakesByFitness = new java.util.LinkedList<>();
        int totalFitness = 0;
        for (int i = 0; i < snakePopulation; i++)
        {
            snakesByFitness.add(sc[i]);
            totalFitness += sc[i].getFitness(); // fitness is calculated when the snake dies
            sc[i].setFitnessProbability(totalFitness);
        }

        // sort with in ascending order
        snakesByFitness.sort((SnakeContainer sc1, SnakeContainer sc2) -> ((int) (sc1.getFitness() - sc2.getFitness())));

        // collect the best snake of the generation
        if (goats.isEmpty())
            goats.add(snakesByFitness.getLast());
        else if (snakesByFitness.getLast().getFitness() > goats.getLast().getFitness())
        {
            goats.add(snakesByFitness.getLast());
            goats.getLast().saveSnake(Integer.toString(generation));
        } else
            snakesByFitness.set(snakesByFitness.size() - 1, goats.getLast());
        // sort the goats
        goats.sort((a, b) -> (a.getFitness() - b.getFitness()));
        if (goats.size() > 20)
            for (int i = 0; i < 15; i++)
                goats.removeFirst(); // only save 20

        // select the breeding partners
        for (int i = 0; i < snakePopulation; i++)
        {
            int tempX = rand.nextInt(totalFitness);
            int tempY = rand.nextInt(totalFitness);
            for (int j = 0; j < sc.length; j++)
            {
                if (tempX < sc[j].getFitnessProbability())
                {
                    x[i] = j;
                    break;
                }
            }
            for (int j = 0; j < snakesByFitness.size(); j++)
            {
                if (tempY < sc[j].getFitnessProbability())
                {
                    y[i] = j;
                    break;
                }
            }
        }

        // crossover && mutation
        SnakeContainer[] children = new SnakeContainer[snakePopulation];
        for (int i = 0; i < snakePopulation; i++)
        {
            // ************ crossover ***********
            if (i < snakePopulation / 2)
                children[i] = new SnakeContainer(sc[i].getXOffset(), sc[i].getYOffset(), N, sc[i].getSize(), foodLocations, snakesByFitness.get(x[i]).crossover(snakesByFitness.get(y[i])));
            else
                children[i] = new SnakeContainer(sc[i].getXOffset(), sc[i].getYOffset(), N, sc[i].getSize(), foodLocations, goats.getLast().crossover(snakesByFitness.getLast()));
            children[i].setHumanInput(false);
            children[i].setMaxSteps(maxSteps);

            // ************ mutate ********
            double chanceOfMutation = 0.75;
            if (rand.nextDouble() < chanceOfMutation)
                children[i].mutation();
        }
        // assign children to next generation
        sc = children;
    }
}
