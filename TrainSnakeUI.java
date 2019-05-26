import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Jamie
 */
public class TrainSnakeUI
{
    LinkedList<SnakeContainer> goats = new LinkedList<>();
    SnakeContainer[] sc; // holds multiple instances of the snake game
    JFrame frame;
    boolean paused;
    int windowWidth, windowHeight, FPS, ST, generation, maxSteps = 200;
    JTextField fpsJTF;
    JTextField maxStepsJTF;
    JPanel buttonsJPanel;
    JLabel fpsJL; // displays the current fps
    JButton startJB, stopJB; // pauses and plays snake game
    JButton slowerJB, fasterJB; // controls the fps
    JButton maxStepsJB;
    JButton saveJB; // used to save nn of goat snake
    JButton setFPSJB;
    JMenuBar jMenuBar;
    JMenu fileJMenu;
    JMenuItem newTrainingSessionJMI, newTestingSessionJMI, newPlayingSessionJMI;
    JPopupMenu newTrainingSessionJPM;

    public TrainSnakeUI(JFrame frame, LinkedList<SnakeContainer> goats, SnakeContainer[] sc, int windowWidth, int windowHeight, int generation)
    {
        FPS = 200;
        ST = 1000 / FPS;
        this.frame = frame;
        this.goats = goats;
        this.sc = sc;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.generation = generation;
        initializeJControls();
    }

    public int getFPS()
    {
        return FPS;
    }

    public int getST()
    {
        return ST;
    }

    public void setGoats(LinkedList<SnakeContainer> goats)
    {
        this.goats = goats;
    }

    public void setSC(SnakeContainer[] sc)
    {
        this.sc = sc;
    }

    public void setGeneration(int generation)
    {
        this.generation = generation;
    }

    public boolean isPaused()
    {
        return paused;
    }

    final public void initializeJControls()
    {
        //********************** Controls ***************************
        buttonsJPanel = new JPanel();
        startJB = new JButton();
        startJB.setText("START");
        startJB.addActionListener(new TSUIButtonActionListener());
        stopJB = new JButton();
        stopJB.setText("STOP");
        stopJB.addActionListener(new TSUIButtonActionListener());
        slowerJB = new JButton();
        slowerJB.setText("<<");
        slowerJB.addActionListener(new TSUIButtonActionListener());
        fasterJB = new JButton();
        fasterJB.setText(">>");
        fasterJB.addActionListener(new TSUIButtonActionListener());
        maxStepsJB = new JButton();
        maxStepsJB.setText("Change Max Steps");
        maxStepsJB.addActionListener(new TSUIButtonActionListener());
        saveJB = new JButton();
        saveJB.setText("Save");
        saveJB.addActionListener(new TSUIButtonActionListener());
        setFPSJB = new JButton();
        setFPSJB.setText("Change Speed");
        setFPSJB.addActionListener(new TSUIButtonActionListener());
        fpsJL = new JLabel();
        fpsJL.setText("Speed: ");
        fpsJTF = new JTextField();
        fpsJTF.setPreferredSize(new Dimension(40, 30));
        fpsJTF.setText(Integer.toString(FPS));
        maxStepsJTF = new JTextField();
        maxStepsJTF.setText(Integer.toString(maxSteps));
        maxStepsJTF.setPreferredSize(new Dimension(40, 30));
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

        //************************ tool bar **************************
        jMenuBar = new JMenuBar();
        fileJMenu = new JMenu("File");
        jMenuBar.add(fileJMenu);
        newTrainingSessionJMI = new JMenuItem( new AbstractAction("New Training Session")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                paused = true;
                NewTrainingSessionPopup m = new NewTrainingSessionPopup();
                m.addActionListener((ActionEvent ae) ->
                {
                    frame.dispose();
                });
            }
        }   
        );
        fileJMenu.add(newTrainingSessionJMI);
        
        newTestingSessionJMI = new JMenuItem( new AbstractAction("New Testing Session")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                paused = true;
                frame.dispose();
                try
                {
                    Runtime.getRuntime().exec("java SnakeAI 2");
                    
                } catch (IOException ex)
                {
                    Logger.getLogger(NewTrainingSessionPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        fileJMenu.add(newTestingSessionJMI);
        
        newPlayingSessionJMI = new JMenuItem( new AbstractAction("New Play Session")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                paused = true;
                frame.dispose();
                try
                {
                    Runtime.getRuntime().exec("java SnakeAI 3");
                    
                } catch (IOException ex)
                {
                    Logger.getLogger(NewTrainingSessionPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        fileJMenu.add(newPlayingSessionJMI);

        //************************* frame ****************************
        frame.add(jMenuBar, BorderLayout.NORTH);
        frame.add(buttonsJPanel, BorderLayout.SOUTH);
    }

    class TSUIButtonActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent event)
        {
            if (event.getSource().equals(startJB))
            {
                paused = false;
            } else if (event.getSource().equals(stopJB))
            {
                paused = true;
            } else if (event.getSource().equals(slowerJB))
            {
                FPS--;
                if (FPS < 1)
                    FPS = 1;
                ST = 1000 / FPS; // skip ticks
                fpsJTF.setText(Integer.toString(FPS));
            } else if (event.getSource().equals(fasterJB))
            {
                FPS++;
                ST = 1000 / FPS; // skip ticks
                fpsJTF.setText(Integer.toString(FPS));
            } else if (event.getSource().equals(maxStepsJB))
            {
                try
                {
                    maxSteps = Integer.parseInt(maxStepsJTF.getText());
                    if (maxSteps < 1)
                        maxSteps = 1;
                    for (SnakeContainer s : sc)
                        s.setMaxSteps(maxSteps);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else if (event.getSource().equals(saveJB))
            {
                if (!goats.isEmpty())
                    goats.getLast().saveSnake("saved" + generation);
            } else if (event.getSource().equals(setFPSJB))
            {
                try
                {
                    FPS = Integer.parseInt(fpsJTF.getText());
                    if (FPS > 300)
                        FPS = 300;
                    else if (FPS < 1)
                        FPS = 1;
                    fpsJTF.setText(Integer.toString(FPS));
                    ST = 1000 / FPS; // skip ticks
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
