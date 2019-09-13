import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
/**
 *
 * @author Jamie
 */
public class PlaySnakeWindow implements Window
{
    int highscore = 0;
    JFrame frame;
    
    JTextField fpsJTF;
    JPanel buttonsJPanel;
    JLabel fpsJL; // displays the current fps
    JButton startJB, stopJB; // pauses and plays snake game
    
    boolean isPaused;
    MyKeyListener mkl; // user input
    SnakeContainerJComponent scjc;
    int windowWidth, windowHeight, FPS, ST, N;
    int space[][]; // this is what the ai will see. 0 = empty space, 1 = occupied, 3 = food
    SnakeContainer sc[]; // holds the snake 
    
    JLabel scoreJL;
    
    public PlaySnakeWindow()
    {
	FPS = 5; // frames per second
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
    
    @Override
    public int getFPS() { return FPS; }
    
    @Override
    public int getST() { return ST; }
    
    @Override
    public void update()
    {
	if ( !isPaused )
	{
            sc[0].updateDirection(mkl.getDirection());
            sc[0].update();
            FPS = 5 + sc[0].getTotalFoodConsumed();
            ST = 1000 / FPS;
	    if ( sc[0].isGameOver() ) gameover();
            
            if ( sc[0].getTotalFoodConsumed() > highscore ) highscore = sc[0].getTotalFoodConsumed();
            scoreJL.setText("SCORE: " + sc[0].getTotalFoodConsumed() + "       HIGHSCORE: " + highscore);
	}
    }
    
    @Override
    public void draw()
    {
	frame.repaint();
    }
    
    public void gameover()
    {
	frame.getContentPane().removeAll();
	newgame();
    }
    
    final public void newgame()
    {
	isPaused = true;
        initializeMenu();
	initializeJControls();
        if ( sc != null && sc[0].getTotalFoodConsumed() > highscore ) highscore = sc[0].getTotalFoodConsumed();
        sc = new SnakeContainer[1];
        sc[0] = new SnakeContainer(0,0, N, (int)(windowHeight * 0.85));
        sc[0].setHumanInput(true);
        scjc = new SnakeContainerJComponent(sc[0].getSize(), sc, null);
        scjc.setPreferredSize(new Dimension(frame.getWidth()/2, frame.getHeight()));
        scjc.setLocation(frame.getWidth()/2, 0);
        scjc.setShowSnakeId(false);
        
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        scoreJL = new JLabel();
        scoreJL.setText("SCORE: " + sc[0].getTotalFoodConsumed() + "       HIGHSCORE: " + highscore);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(scoreJL, c);

        scjc.setMinimumSize(scjc.getSize());
        gridbag.setConstraints(scjc, c);
        
        panel.setLayout(gridbag);
        panel.add(scoreJL);
        panel.add(scjc);
        frame.add(panel);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void initializeMenu()
    {
        frame.add(WindowMenuBar.getWindowMenuBar(frame), BorderLayout.NORTH);
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
	fpsJL = new JLabel();
	fpsJL.setText("Speed: ");
        fpsJTF = new JTextField();
        fpsJTF.setPreferredSize(new Dimension(40,30));
        fpsJTF.setText(Integer.toString(FPS));
        buttonsJPanel.add(startJB);
        buttonsJPanel.add(stopJB);
        frame.add(buttonsJPanel, BorderLayout.SOUTH);
        startJB.addKeyListener(mkl);
        stopJB.addKeyListener(mkl);
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
	}
    }
}
