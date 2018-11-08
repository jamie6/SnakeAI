import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.io.File;

/**
 *
 * @author Jamie
 */
public class TestSnakeWindow implements Window
{
    JFrame frame;
    
    JTextField fpsJTF;
    JPanel buttonsJPanel;
    JLabel fpsJL; // displays the current fps
    JButton startJB, stopJB; // pauses and plays snake game
    JButton slowerJB, fasterJB; // controls the fps
    JButton setFPSJB;
    JButton openNNJB; // button to show open dialog
    JButton restartJB; // restart
    JFileChooser jFileChooser;
    
    boolean isPaused;
    DrawLayerComponent dlc;
    int windowWidth, windowHeight, FPS, ST, N;
    int space[][]; // this is what the ai will see. 0 = empty space, 1 = occupied, 3 = food
    SnakeContainer sc[]; // holds the snake 
    NeuralNetwork nn;
    
    public TestSnakeWindow()
    {
	FPS = 10; // frames per second
	ST = 1000 / FPS; // skip ticks
	N = 20; // NxN grid
	windowWidth = 800;
	windowHeight = 800;
	frame = new JFrame();
	frame.setSize(windowWidth+50, windowHeight+75);
        frame.setPreferredSize(frame.getSize()); 
	frame.setTitle("Snake A.I.");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nn = null;
        newgame(nn);
    }
    
    public void update()
    {
	if ( !isPaused )
	{
            sc[0].update();
	    if ( sc[0].isGameOver() )  gameover();
	}
    }
    
    public void draw()
    {
	frame.repaint();
    }
    
    public void gameover()
    {
	frame.getContentPane().removeAll();
        newgame(nn);       
        startJB.setEnabled(true);
        stopJB.setEnabled(true);
        slowerJB.setEnabled(true);
        fasterJB.setEnabled(true);
        setFPSJB.setEnabled(true);
        restartJB.setEnabled(true);
    }
    
    public void newgame(NeuralNetwork nn)
    {
	isPaused = true;
	initializeJControls();
        
        if ( nn != null )
        {
            sc = new SnakeContainer[1];
            sc[0] = new SnakeContainer(0,0, N, (int)(windowHeight * 0.85), nn);
            sc[0].setHumanInput(false);
            dlc = new DrawLayerComponent(windowWidth/2, windowWidth/2, sc);
            dlc.setPreferredSize(new Dimension(frame.getWidth()/2, frame.getHeight()));
            dlc.setLocation(frame.getWidth()/2, 0);
            dlc.setShowSnakeId(false);
            frame.add(dlc, BorderLayout.CENTER);
        }
        frame.pack();
        frame.setVisible(true);
    }
    
    
    public void initializeJControls()
    {
        //********************** FPS Controls ***************************
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
        setFPSJB = new JButton();
        setFPSJB.setText("Change Speed");
        setFPSJB.addActionListener(new ButtonActionListener());
        openNNJB = new JButton();
        openNNJB.setText("Open");
        openNNJB.addActionListener(new ButtonActionListener());
        restartJB = new JButton();
        restartJB.setText("Restart");
        restartJB.addActionListener(new ButtonActionListener());
        startJB.setEnabled(false);
        stopJB.setEnabled(false);
        slowerJB.setEnabled(false);
        fasterJB.setEnabled(false);
        setFPSJB.setEnabled(false);
        restartJB.setEnabled(false);
        
	fpsJL = new JLabel();
	fpsJL.setText("Speed: ");
        fpsJTF = new JTextField();
        fpsJTF.setPreferredSize(new Dimension(40,30));
        fpsJTF.setText(Integer.toString(FPS));
        buttonsJPanel.add(openNNJB);
        buttonsJPanel.add(startJB);
        buttonsJPanel.add(stopJB);
        buttonsJPanel.add(slowerJB);
        buttonsJPanel.add(fpsJL);
        buttonsJPanel.add(fpsJTF);
        buttonsJPanel.add(fasterJB);
        buttonsJPanel.add(setFPSJB);
        buttonsJPanel.add(restartJB);
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
            else if ( event.getSource().equals(openNNJB))
            {
                jFileChooser = new JFileChooser();
                if ( jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION )
                {
                    File file = jFileChooser.getSelectedFile();
                    nn = new NeuralNetwork(file.getAbsolutePath());
                    gameover();
                }
            }
            else if ( event.getSource().equals(restartJB))
            {
                gameover();
            }
	}
    }
}
