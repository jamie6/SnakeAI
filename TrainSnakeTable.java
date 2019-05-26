import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jamie
 */
public class TrainSnakeTable
{
    JPanel tableJPanel;
    JLabel generationJL; // label to show current generation
    JScrollPane statJSP;
    JTable statJT; // statistical data on current snake population   
    DefaultTableModel model; // jtable model
    JFrame frame;
    SnakeContainer[] sc;
    int windowWidth, windowHeight, FPS, ST, N, snakePopulation, generation, columns, maxSteps = 200;

    public TrainSnakeTable(JFrame frame, int snakePopulation, int windowWidth, int windowHeight, int generation)
    {
        FPS = 200;
        ST = 1000 / FPS;
        this.frame = frame;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.generation = generation;
        this.snakePopulation = snakePopulation;
        initializeTable();
    }

    public void setGeneration(int generation)
    {
        this.generation = generation;
        generationJL.setText("Generation: " + generation);
    }

    final public void initializeTable()
    {
        // ************************* TABLE ********************************
        generationJL = new JLabel();
        generationJL.setText("Generation: " + generation);
        generationJL.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 18));
        generationJL.setSize(new Dimension(100, 100));
        String tableColumnNames[] =
        {
            "Snake", "Length", "Steps Taken", "Food Consumed", "Final Fitness"
        };
        String data[][] = new String[snakePopulation][tableColumnNames.length];
        for (int i = 0; i < snakePopulation; i++) // initialize data to 0
            for (int j = 0; j < tableColumnNames.length; j++)
                data[i][j] = (j == 0) ? ((Integer) i).toString() : "";
        model = new DefaultTableModel(data, tableColumnNames);
        statJT = new JTable(model);
        statJT.setSize(windowWidth / 3, (int) (windowHeight * 0.1) + windowHeight / 2);
        statJT.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 18));
        statJT.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        statJSP = new JScrollPane(statJT);
        statJSP.setPreferredSize(new Dimension(statJT.getWidth(), statJT.getHeight()));
        tableJPanel = new JPanel();
        tableJPanel.add(generationJL, BorderLayout.NORTH);
        tableJPanel.add(statJSP, BorderLayout.CENTER);
        tableJPanel.setOpaque(true);
        tableJPanel.setVisible(true);

        frame.add(tableJPanel, BorderLayout.WEST);
    }
    
    public void highlightRow(int i)
    {
        statJT.changeSelection(i, 0, false, false);
    }

    public int updateTable(int i, SnakeContainer sc)
    {
        statJT.setValueAt(sc.getLength(), i, 1);
        statJT.setValueAt(sc.getStepsTaken(), i, 2);
        statJT.setValueAt(sc.getTotalFoodConsumed(), i, 3);
        if (sc.isGameOver())
        {
            statJT.setValueAt(sc.getFitness(), i, 4);
            return 1;
        }
        return 0;
    }
}
