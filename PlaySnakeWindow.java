import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.BorderLayout;
/**
 *
 * @author Jamie
 */
public class PlaySnakeWindow implements Window
{
    JFrame frame;
    
    JTextField fpsJTF;
    JPanel buttonsJPanel;
    JLabel fpsJL; // displays the current fps
    JButton startJB, stopJB; // pauses and plays snake game
    JButton slowerJB, fasterJB; // controls the fps
    JButton setFPSJB;
    
    boolean isPaused;
    MyKeyListener mkl; // user input
    DrawLayerComponent dlc;
    int windowWidth, windowHeight, FPS, ST, N;
    int space[][]; // this is what the ai will see. 0 = empty space, 1 = occupied, 3 = food
    SnakeContainer sc[]; // holds the snake 
    //java.util.Random rand = new java.util.Random();
    
    public PlaySnakeWindow()
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
        
        mkl = new MyKeyListener();
        frame.addKeyListener(mkl);
	newgame();
    }
    
    public void update()
    {
	if ( !isPaused )
	{
            sc[0].updateDirection(mkl.getDirection());
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
	newgame();
    }
    
    public void newgame()
    {
	isPaused = true;
	initializeJControls();
        
        sc = new SnakeContainer[1];
        
        sc[0] = new SnakeContainer(0,0, N, (int)(windowHeight * 0.85));
        sc[0].setHumanInput(true);
        dlc = new DrawLayerComponent(windowWidth/2, windowWidth/2, sc);
        dlc.setPreferredSize(new Dimension(frame.getWidth()/2, frame.getHeight()));
        dlc.setLocation(frame.getWidth()/2, 0);
        dlc.setShowSnakeId(false);
        frame.add(dlc, BorderLayout.CENTER);
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
	fpsJL = new JLabel();
	fpsJL.setText("Speed: ");
        fpsJTF = new JTextField();
        fpsJTF.setPreferredSize(new Dimension(40,30));
        fpsJTF.setText(Integer.toString(FPS));
        buttonsJPanel.add(startJB);
        buttonsJPanel.add(stopJB);
        buttonsJPanel.add(slowerJB);
        buttonsJPanel.add(fpsJL);
        buttonsJPanel.add(fpsJTF);
        buttonsJPanel.add(fasterJB);
        buttonsJPanel.add(setFPSJB);
        frame.add(buttonsJPanel, BorderLayout.SOUTH);
        startJB.addKeyListener(mkl);
        stopJB.addKeyListener(mkl);
        slowerJB.addKeyListener(mkl);
        fasterJB.addKeyListener(mkl);
        setFPSJB.addKeyListener(mkl);
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
                    if (FPS > 75 ) FPS = 75;
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
